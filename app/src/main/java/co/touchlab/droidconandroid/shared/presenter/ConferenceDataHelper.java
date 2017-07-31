package co.touchlab.droidconandroid.shared.presenter;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import co.touchlab.droidconandroid.shared.network.dao.Convention;
import co.touchlab.droidconandroid.shared.network.dao.NetworkEvent;
import co.touchlab.droidconandroid.shared.network.dao.NetworkUserAccount;
import co.touchlab.droidconandroid.shared.network.dao.NetworkVenue;
import co.touchlab.droidconandroid.shared.utils.StringUtils;
import co.touchlab.droidconandroid.shared.utils.TimeUtils;
import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by kgalligan on 4/17/16.
 */
public class ConferenceDataHelper
{
    private final static SimpleDateFormat dateFormat;
    private final static SimpleDateFormat timeFormat;

    static
    {
        dateFormat = TimeUtils.makeDateFormat("MM/dd/yyyy");
        timeFormat = TimeUtils.makeDateFormat("h:mma");
    }

    public static String dateToDayString(Date d)
    {
        return dateFormat.format(d);
    }

    public static Single<List<TimeBlock>> getDays(DatabaseHelper helper)
    {
        return Single.fromCallable(() -> getDaySchedules(helper));
    }

    private static List<TimeBlock> getDaySchedules(DatabaseHelper databaseHelper) throws SQLException
    {
        List<TimeBlock> eventAndBlockList = new ArrayList<>();
        List<Event> eventList = databaseHelper.getEventsList();

        eventAndBlockList.addAll(databaseHelper.getBlocksList());
        eventAndBlockList.addAll(eventList);
        return eventAndBlockList;
    }

    public static int sortTimeBlocks(TimeBlock o1, TimeBlock o2)
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

    public static TreeMap<String, List<HourBlock>> formatHourBlocks(List<TimeBlock> eventAndBlockList)
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

    public static List<DaySchedule> convertMapToDaySchedule(TreeMap<String, List<HourBlock>> dateWithBlocksTreeMap)
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

    public static Completable saveConvention(final DatabaseHelper helper, final AppPrefs appPrefs, final Convention convention)
    {
        return Completable.fromAction(() -> saveConventionData(helper, appPrefs, convention));
    }

    private static void saveConventionData(final DatabaseHelper helper, final AppPrefs appPrefs, final Convention convention)
    {

        if(convention == null)
        {
            throw new IllegalStateException("No convention results");
        }

        appPrefs.setConventionStartDate(convention.startDate);
        appPrefs.setConventionEndDate(convention.endDate);

        List<NetworkVenue> newVenueList = convention.venues;
        List<Block> newBlockList = convention.blocks;
        Set<Long> eventIdList = new HashSet<>();

        try
        {
            for(NetworkVenue newVenue : newVenueList)
            {
                for(NetworkEvent newEvent : newVenue.events)
                {
                    eventIdList.add(newEvent.id);
                    String matchingRsvpUuid = helper.getRsvpUuidForEventWithId(newEvent.id);
                    newEvent.venue = newVenue;

                    if(StringUtils.isEmpty(newEvent.startDate) ||
                            StringUtils.isEmpty(newEvent.endDate))
                    {
                        continue;
                    }

                    newEvent.startDateLong = TimeUtils.parseTime(newEvent.startDate);
                    newEvent.endDateLong = TimeUtils.parseTime(newEvent.endDate);

                    if(matchingRsvpUuid != null)
                    {
                        newEvent.rsvpUuid = matchingRsvpUuid;
                    }

                    helper.createEvent(newEvent);
                    int speakerCount = 0;

                    for(NetworkUserAccount newSpeaker : newEvent.speakers)
                    {
                        UserAccount oldSpeaker = helper.getUserAccount(newSpeaker.id);

                        if(oldSpeaker == null)
                        {
                            oldSpeaker = new UserAccount();
                        }

                        helper.convertAndSaveUserAccount(newSpeaker, oldSpeaker);

                        EventSpeaker eventSpeaker = helper.getSpeakerForEventWithId(newEvent.id,
                                newSpeaker.id);

                        if(eventSpeaker == null)
                        {
                            eventSpeaker = new EventSpeaker();
                        }

                        eventSpeaker.eventId = newEvent.id;
                        eventSpeaker.userAccountId = oldSpeaker.id;
                        eventSpeaker.displayOrder = speakerCount++;
                        helper.updateSpeaker(eventSpeaker);
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
                helper.deleteBlocks(helper.getBlocksList());

                // parse and save new blocks
                for(Block newBlock : newBlockList)
                {
                    newBlock.startDateLong = TimeUtils.parseTime(newBlock.startDate);
                    newBlock.endDateLong = TimeUtils.parseTime(newBlock.endDate);
                    helper.updateBlock(newBlock);
                }
            }

        }
        catch(ParseException e)
        {
            throw new RuntimeException(e);
        }
    }
}
