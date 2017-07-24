package co.touchlab.droidconandroid.shared.presenter;

import android.content.Context;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.Block;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.EventSpeaker;
import co.touchlab.droidconandroid.shared.data.TimeBlock;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.data.Venue;
import co.touchlab.droidconandroid.shared.network.dao.Convention;
import co.touchlab.droidconandroid.shared.utils.StringUtils;
import co.touchlab.droidconandroid.shared.utils.TimeUtils;
import co.touchlab.droidconandroid.shared.utils.UserDataHelper;
import co.touchlab.squeaky.dao.Dao;
import co.touchlab.squeaky.stmt.Where;
import io.reactivex.Single;

/**
 * Created by kgalligan on 4/17/16.
 */
public class ConferenceDataHelper
{
    final static SimpleDateFormat dateFormat;
    final static SimpleDateFormat timeFormat;

    static
    {
        dateFormat = TimeUtils.makeDateFormat("MM/dd/yyyy");
        timeFormat = TimeUtils.makeDateFormat("h:mma");
    }

    public static String dateToDayString(Date d)
    {
        return dateFormat.format(d);
    }

    public static Single<DaySchedule[]> getDays(DatabaseHelper helper, boolean allEvents)
    {
        return Single.fromCallable(() -> getDaySchedules(helper, allEvents));
    }

    public static DaySchedule[] getDaySchedules(DatabaseHelper databaseHelper, boolean allEvents) throws SQLException
    {
        final Dao<Event> eventDao = databaseHelper.getEventDao();
        final Dao<Block> blockDao = databaseHelper.getBlockDao();

        List<TimeBlock> eventAndBlockList = new ArrayList<>();
        List<Event> eventList;

        if(allEvents)
        {
            eventList = eventDao.queryForAll().list();
        }
        else
        {
            Where<Event> where = new Where<>(eventDao);
            eventList = where.isNotNull("rsvpUuid").query().list();
        }

        for(Event event : eventList)
        {
            eventDao.fillForeignCollection(event, "speakerList");
        }

        eventAndBlockList.addAll(blockDao.queryForAll().list());
        eventAndBlockList.addAll(eventList);

        Collections.sort(eventAndBlockList, ConferenceDataHelper:: sortTimeBlocks);
        TreeMap<String, List<HourBlock>> dateWithBlocksTreeMap = formatHourBlocks(eventAndBlockList);
        List<DaySchedule> dayScheduleList = convertMapToDaySchedule(dateWithBlocksTreeMap);

        return dayScheduleList.toArray(new DaySchedule[dayScheduleList.size()]);
    }

    private static int sortTimeBlocks(TimeBlock o1, TimeBlock o2)
    {
        final long compTimes = o1.getStartLong() - o2.getStartLong();
        if(compTimes != 0)
        {
            return compTimes > 0 ? 1 : - 1;
        }

        if(o1.isBlock() && o2.isBlock())
        {
            return 0;
        }

        if(o1.isBlock())
        {
            return 1;
        }
        if(o2.isBlock())
        {
            return - 1;
        }

        return ((Event) o1).venue.name.compareTo(((Event) o2).venue.name);
    }

    private static TreeMap<String, List<HourBlock>> formatHourBlocks(List<TimeBlock> eventAndBlockList)
    {
        TreeMap<String, List<HourBlock>> dateWithBlocksTreeMap = new TreeMap<>();
        String lastHourDisplay = "";

        for(TimeBlock timeBlock : eventAndBlockList)
        {
            final Date startDateObj = new Date(timeBlock.getStartLong());
            final String startDate = dateFormat.format(startDateObj);
            List<HourBlock> blockHourList = dateWithBlocksTreeMap.get(startDate);
            if(blockHourList == null)
            {
                blockHourList = new ArrayList<>();
                dateWithBlocksTreeMap.put(startDate, blockHourList);
            }

            final String startTime = timeFormat.format(startDateObj);
            final boolean newHourDisplay = ! lastHourDisplay.equals(startTime);
            blockHourList.add(new HourBlock(newHourDisplay ? startTime : "", timeBlock));
            lastHourDisplay = startTime;
        }
        return dateWithBlocksTreeMap;
    }

    private static List<DaySchedule> convertMapToDaySchedule(TreeMap<String, List<HourBlock>> dateWithBlocksTreeMap)
    {
        List<DaySchedule> dayScheduleList = new ArrayList<>();

        for(String dateString : dateWithBlocksTreeMap.keySet())
        {
            final List<HourBlock> hourBlocksMap = dateWithBlocksTreeMap.get(dateString);
            final DaySchedule daySchedule = new DaySchedule(dateString,
                    hourBlocksMap.toArray(new HourBlock[hourBlocksMap.size()]));
            dayScheduleList.add(daySchedule);
        }

        return dayScheduleList;
    }

    public static void saveConventionData(final Context context, final Convention convention)
    {
        if(convention == null)
        {
            throw new IllegalStateException("No convention results");
        }

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        AppPrefs appPrefs = AppPrefs.getInstance(context);
        helper.performTransactionOrThrowRuntime(() ->
        {
            Dao<Event> eventDao = helper.getEventDao();
            Dao<Venue> venueDao = helper.getVenueDao();
            Dao<Block> blockDao = helper.getBlockDao();
            Dao<UserAccount> userAccountDao = helper.getUserAccountDao();
            Dao<EventSpeaker> eventSpeakerDao = helper.getEventSpeakerDao();

            //extract
            appPrefs.setConventionStartDate(convention.startDate); // Can be moved outside the transaction
            appPrefs.setConventionEndDate(convention.endDate);

            List<co.touchlab.droidconandroid.shared.network.dao.Venue> newVenueList = convention.venues;
            List<co.touchlab.droidconandroid.shared.network.dao.Block> newBlockList = convention.blocks;
            Set<Long> eventIdList = new HashSet<>();
            try
            {
                for(co.touchlab.droidconandroid.shared.network.dao.Venue newVenue : newVenueList)
                {
                    venueDao.createOrUpdate(newVenue); //will skip
                    for(co.touchlab.droidconandroid.shared.network.dao.Event newEvent : newVenue.events)
                    {
                        eventIdList.add(newEvent.id);
                        Event oldEvent = eventDao.queryForId(newEvent.id); // just to check for rsvp, can be moved
                        newEvent.venue = newVenue;

                        if(StringUtils.isEmpty(newEvent.startDate) ||
                                StringUtils.isEmpty(newEvent.endDate))
                        {
                            continue;
                        }

                        newEvent.startDateLong = TimeUtils.parseTime(newEvent.startDate);
                        newEvent.endDateLong = TimeUtils.parseTime(newEvent.endDate);

                        // use elvis
                        if(oldEvent != null)
                        {
                            newEvent.rsvpUuid = oldEvent.rsvpUuid;
                        }

                        eventDao.createOrUpdate(newEvent);
                        int speakerCount = 0;

                        for(co.touchlab.droidconandroid.shared.network.dao.UserAccount newSpeaker : newEvent.speakers)
                        {
                            UserAccount oldSpeaker = userAccountDao.queryForId(newSpeaker.id);

                            if(oldSpeaker == null)
                            {
                                oldSpeaker = new UserAccount();
                            }

                            // make 1 method update&save
                            UserDataHelper.userAccountToDb(newSpeaker, oldSpeaker);
                            userAccountDao.createOrUpdate(oldSpeaker);

                            // get list of eventSpeaker, does not need to be a list
                            Where<EventSpeaker> where = new Where<>(eventSpeakerDao);
                            List speakerEventList = where.and()
                                    .eq("event_id", newEvent.id) //event already updated
                                    .eq("userAccount_id", oldSpeaker.id) //event already updated
                                    .query()
                                    .list();
                            EventSpeaker eventSpeaker = ((speakerEventList.size() == 0)
                                    ? new EventSpeaker() : (EventSpeaker) speakerEventList.get(0));

                            // make 1 method, update&save
                            eventSpeaker.event = newEvent;
                            eventSpeaker.userAccount = oldSpeaker;
                            eventSpeaker.displayOrder = speakerCount++;
                            eventSpeakerDao.createOrUpdate(eventSpeaker);
                        }
                    }

                }

                // clear db if events are returned
                if(! eventIdList.isEmpty())
                {
                    helper.deleteEventsNotIn(eventIdList);
                }

                if(newBlockList.size() > 0)
                {
                    //Dump all old blocks first
                    blockDao.delete(blockDao.queryForAll().list());

                    // parse and save new blocks
                    for(co.touchlab.droidconandroid.shared.network.dao.Block block : newBlockList)
                    {
                        // reconsider if formatting needs to be done here
                        block.startDateLong = TimeUtils.parseTime(block.startDate);
                        block.endDateLong = TimeUtils.parseTime(block.endDate);
                        blockDao.createOrUpdate(block);
                    }
                }

            }
            catch(SQLException | ParseException e)
            {
                throw new RuntimeException(e);
            }
            // callable?!
            return null;
        });
    }
}
