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

import javax.inject.Inject;
import javax.inject.Singleton;

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
@Singleton
public class ConferenceDataHelper
{
    private final static SimpleDateFormat DATE_FORMAT = TimeUtils.makeDateFormat("MM/dd/yyyy");
    private final static SimpleDateFormat TIME_FORMAT = TimeUtils.makeDateFormat("h:mma");
    private final AppPrefs       appPrefs;
    private final DatabaseHelper helper;

    @Inject
    public ConferenceDataHelper(AppPrefs appPrefs, DatabaseHelper helper)
    {
        this.appPrefs = appPrefs;
        this.helper = helper;
    }

    public static String dateToDayString(Date d)
    {
        return DATE_FORMAT.format(d);
    }

    public Single<List<TimeBlock>> getDays()
    {
        return Single.fromCallable(this :: getDaySchedules);
    }

    private List<TimeBlock> getDaySchedules()
    {
        List<TimeBlock> eventAndBlockList = new ArrayList<>();
        List<Event> eventList = helper.getEventsWithSpeakersList();

        eventAndBlockList.addAll(helper.getBlocksList());
        eventAndBlockList.addAll(eventList);
        return eventAndBlockList;
    }

    public int sortTimeBlocks(TimeBlock o1, TimeBlock o2)
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

    public TreeMap<String, List<HourBlock>> formatHourBlocks(List<TimeBlock> eventAndBlockList)
    {
        TreeMap<String, List<HourBlock>> dateWithBlocksTreeMap = new TreeMap<>();
        String lastHourDisplay = "";

        for(TimeBlock timeBlock : eventAndBlockList)
        {
            final Date startDateObj = new Date(timeBlock.getStartLong());
            final String startDate = DATE_FORMAT.format(startDateObj);
            List<HourBlock> blockHourList = dateWithBlocksTreeMap.get(startDate);
            if(blockHourList == null)
            {
                blockHourList = new ArrayList<>();
                dateWithBlocksTreeMap.put(startDate, blockHourList);
            }

            final String startTime = TIME_FORMAT.format(startDateObj);
            final boolean newHourDisplay = ! lastHourDisplay.equals(startTime);
            blockHourList.add(new HourBlock(newHourDisplay ? startTime : "", timeBlock));
            lastHourDisplay = startTime;
        }
        return dateWithBlocksTreeMap;
    }

    public List<DaySchedule> convertMapToDaySchedule(TreeMap<String, List<HourBlock>> dateWithBlocksTreeMap)
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

    public Completable saveConvention(final Convention convention)
    {
        return Completable.fromAction(() -> saveConventionData(convention));
    }

    private void saveConventionData(final Convention convention)
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
                        eventSpeaker.name = oldSpeaker.name;
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
