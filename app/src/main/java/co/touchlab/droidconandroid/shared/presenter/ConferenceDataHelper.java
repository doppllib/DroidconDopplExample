package co.touchlab.droidconandroid.shared.presenter;
import android.content.Context;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import co.touchlab.droidconandroid.shared.data.Block;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.ScheduleBlock;
import co.touchlab.droidconandroid.shared.utils.TimeUtils;
import co.touchlab.squeaky.dao.Dao;
import co.touchlab.squeaky.stmt.Where;

/**
 * Created by kgalligan on 4/17/16.
 */
public class ConferenceDataHelper
{
    final static SimpleDateFormat dateFormat;
    final static SimpleDateFormat timeFormat;

    static {
        dateFormat = TimeUtils.makeDateFormat("MM/dd/yyyy");
        timeFormat = TimeUtils.makeDateFormat("h:mma");
    }

    public static String dateToDayString(Date d)
    {
        return dateFormat.format(d);
    }

    public static ConferenceDayHolder[] listDays(Context context, boolean allEvents) throws SQLException
    {
        final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        final Dao<Event> eventDao = databaseHelper.getEventDao();
        final Dao<Block> blockDao = databaseHelper.getBlockDao();

        List<ScheduleBlock> all = new ArrayList<>();

        all.addAll(blockDao.queryForAll().list());
        List<Event> eventList = null;

        if(allEvents)
        {
            eventList = eventDao.queryForAll().list();
        }
        else
        {
            Where<Event> where = new Where<Event>(eventDao);
            eventList = where.isNotNull("rsvpUuid").query().list();
        }

        for(Event event : eventList)
        {
            eventDao.fillForeignCollection(event, "speakerList");
        }

        all.addAll(eventList);

        Collections.sort(all, new Comparator<ScheduleBlock>()
        {
            @Override
            public int compare(ScheduleBlock o1, ScheduleBlock o2)
            {
                final long compTimes = o1.getStartLong() - o2.getStartLong();
                if(compTimes != 0) return compTimes > 0
                        ? 1
                        : - 1;

                if(o1.isBlock() && o2.isBlock()) return 0;

                if(o1.isBlock()) return 1;
                if(o2.isBlock()) return - 1;

                return ((Event) o1).venue.name.compareTo(((Event) o2).venue.name);
            }
        });

        TreeMap<String, List<ScheduleBlockHour>> allTheData = new TreeMap<>();
        String lastHourDisplay = "";
        List<ScheduleBlockHour> blockHours = new ArrayList<>();

        for(ScheduleBlock scheduleBlock : all)
        {
            final Date startDateObj = new Date(scheduleBlock.getStartLong());
            final String startDate = dateFormat.format(startDateObj);
            List<ScheduleBlockHour> blockHourList = allTheData
                    .get(startDate);
            if(blockHourList == null)
            {
                blockHourList = new ArrayList<>();
                allTheData.put(startDate, blockHourList);
            }

            final String startTime = timeFormat.format(startDateObj);
            final boolean newHourDisplay = ! lastHourDisplay.equals(startTime);
            blockHourList.add(new ScheduleBlockHour(newHourDisplay ? startTime : "", scheduleBlock));
            lastHourDisplay = startTime;
        }

        List<ConferenceDayHolder> dayHolders = new ArrayList<>();

        for(String dateString : allTheData.keySet())
        {
            final List<ScheduleBlockHour> hourBlocksMap = allTheData
                    .get(dateString);

            final ConferenceDayHolder conferenceDayHolder = new ConferenceDayHolder(dateString, hourBlocksMap.toArray(new ScheduleBlockHour[hourBlocksMap.size()]));
            dayHolders.add(conferenceDayHolder);
        }

        return dayHolders.toArray(new ConferenceDayHolder[dayHolders.size()]);
    }
}
