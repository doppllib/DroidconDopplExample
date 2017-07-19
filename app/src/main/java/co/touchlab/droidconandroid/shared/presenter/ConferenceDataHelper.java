package co.touchlab.droidconandroid.shared.presenter;
import android.content.Context;

import java.sql.SQLException;
import java.text.ParseException;
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
import co.touchlab.droidconandroid.shared.data.ScheduleBlock;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.data.Venue;
import co.touchlab.droidconandroid.shared.network.dao.Convention;
import co.touchlab.droidconandroid.shared.utils.StringUtils;
import co.touchlab.droidconandroid.shared.utils.TimeUtils;
import co.touchlab.droidconandroid.shared.utils.UserDataHelper;
import co.touchlab.squeaky.dao.Dao;
import co.touchlab.squeaky.stmt.Where;

/**
 * Created by kgalligan on 4/17/16.
 */
public class ConferenceDataHelper
{
    public static ConferenceDayHolder[] listDays(DatabaseHelper databaseHelper, boolean allEvents) throws SQLException
    {
        final Dao<Event> eventDao = databaseHelper.getEventDao();
        final Dao<Block> blockDao = databaseHelper.getBlockDao();

        List<ScheduleBlock> all = new ArrayList<>();

        all.addAll(blockDao.queryForAll().list());
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

        all.addAll(eventList);

        Collections.sort(all, (o1, o2) ->
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
        });

        TreeMap<String, List<ScheduleBlockHour>> allTheData = new TreeMap<>();
        String lastHourDisplay = "";

        for(ScheduleBlock scheduleBlock : all)
        {
            final Date startDateObj = new Date(scheduleBlock.getStartLong());
            final String startDate = TimeUtils.SIMPLE_DATE_FORMAT.format(startDateObj);
            List<ScheduleBlockHour> blockHourList = allTheData.get(startDate);
            if(blockHourList == null)
            {
                blockHourList = new ArrayList<>();
                allTheData.put(startDate, blockHourList);
            }

            final String startTime = TimeUtils.SIMPLE_TIME_FORMAT.format(startDateObj);
            final boolean newHourDisplay = ! lastHourDisplay.equals(startTime);
            blockHourList.add(new ScheduleBlockHour(newHourDisplay ? startTime : "", scheduleBlock));
            lastHourDisplay = startTime;
        }

        List<ConferenceDayHolder> dayHolders = new ArrayList<>();

        for(String dateString : allTheData.keySet())
        {
            final List<ScheduleBlockHour> hourBlocksMap = allTheData.get(dateString);

            final ConferenceDayHolder conferenceDayHolder = new ConferenceDayHolder(dateString,
                    hourBlocksMap.toArray(new ScheduleBlockHour[hourBlocksMap.size()]));
            dayHolders.add(conferenceDayHolder);
        }

        return dayHolders.toArray(new ConferenceDayHolder[dayHolders.size()]);
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

            appPrefs.setConventionStartDate(convention.startDate);
            appPrefs.setConventionEndDate(convention.endDate);

            List<co.touchlab.droidconandroid.shared.network.dao.Venue> venues = convention.venues;
            List<co.touchlab.droidconandroid.shared.network.dao.Block> blocks = convention.blocks;
            Set<Long> foundEvents = new HashSet<>();
            try
            {

                for(co.touchlab.droidconandroid.shared.network.dao.Venue venue : venues)
                {
                    venueDao.createOrUpdate(venue);
                    for(co.touchlab.droidconandroid.shared.network.dao.Event event : venue.events)
                    {
                        foundEvents.add(event.id);
                        Event dbEvent = eventDao.queryForId(event.id);
                        event.venue = venue;

                        if(StringUtils.isEmpty(event.startDate) ||
                                StringUtils.isEmpty(event.endDate))
                        {
                            continue;
                        }

                        event.startDateLong = TimeUtils.parseTime(event.startDate);
                        event.endDateLong = TimeUtils.parseTime(event.endDate);

                        if(dbEvent != null)
                        {
                            event.rsvpUuid = dbEvent.rsvpUuid;
                        }

                        eventDao.createOrUpdate(event);
                        int speakerCount = 0;

                        for(co.touchlab.droidconandroid.shared.network.dao.UserAccount ua : event.speakers)
                        {
                            UserAccount userAccount = userAccountDao.queryForId(ua.id);

                            if(userAccount == null)
                            {
                                userAccount = new UserAccount();
                            }

                            UserDataHelper.userAccountToDb(ua, userAccount);
                            userAccountDao.createOrUpdate(userAccount);
                            Where<EventSpeaker> where = new Where<>(eventSpeakerDao);
                            List resultList = where.and()
                                    .eq("event_id", event.id)
                                    .eq("userAccount_id", userAccount.id)
                                    .query()
                                    .list();

                            EventSpeaker eventSpeaker = ((resultList.size() == 0)
                                    ? new EventSpeaker()
                                    : (EventSpeaker) resultList.get(0));

                            eventSpeaker.event = event;
                            eventSpeaker.userAccount = userAccount;
                            eventSpeaker.displayOrder = speakerCount++;

                            eventSpeakerDao.createOrUpdate(eventSpeaker);
                        }
                    }

                }

                // don't clear the db if no events are returned
                if(! foundEvents.isEmpty())
                {
                    helper.deleteEventsNotIn(foundEvents);
                }

                if(blocks.size() > 0)
                {
                    //Dump all of them first
                    blockDao.delete(blockDao.queryForAll().list());

                    for(co.touchlab.droidconandroid.shared.network.dao.Block block : blocks)
                    {
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
            return null;
        });
    }
}
