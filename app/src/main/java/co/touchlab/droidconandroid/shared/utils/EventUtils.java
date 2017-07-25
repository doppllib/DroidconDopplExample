package co.touchlab.droidconandroid.shared.utils;
import android.text.TextUtils;

import java.util.List;

import co.touchlab.droidconandroid.shared.data.Block;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.presenter.ScheduleBlockHour;

public class EventUtils
{
    public static void styleEventRow(ScheduleBlockHour scheduleBlockHour, List dataSet, EventRow row, boolean allEvents)
    {
        boolean isFirstInBlock = ! scheduleBlockHour.hourStringDisplay.isEmpty();
        row.setTimeGap(isFirstInBlock);

        if(scheduleBlockHour.getScheduleBlock().isBlock())
        {
            Block block = (Block) scheduleBlockHour.scheduleBlock;
            row.setTitleText(block.name);
            row.setTimeText(scheduleBlockHour.hourStringDisplay.toLowerCase());
            row.setSpeakerText("");
            row.setDescription(block.description);
            row.setLiveNowVisible(false);
            row.setRsvpVisible(false, false);
            row.setRsvpConflict(false);
        }
        else
        {
            Event event = (Event) scheduleBlockHour.scheduleBlock;
            row.setTimeText(scheduleBlockHour.hourStringDisplay.toLowerCase());
            row.setTitleText(event.name);
            row.setSpeakerText(event.allSpeakersString());
            row.setDescription(event.description);
            row.setLiveNowVisible(event.isNow());
            row.setRsvpVisible(allEvents && event.isRsvped(), event.isPast());
            row.setRsvpConflict(allEvents && hasConflict(event, dataSet));
        }
    }

    public static boolean hasConflict(Event event, List dataSet)
    {
        if(event.isRsvped() && ! event.isPast())
        {
            for(Object o : dataSet)
            {
                if(o instanceof ScheduleBlockHour &&
                        ((ScheduleBlockHour) o).scheduleBlock instanceof Event)
                {
                    Event e = (Event) ((ScheduleBlockHour) o).scheduleBlock;
                    if(event.id != e.id && ! TextUtils.isEmpty(e.rsvpUuid) &&
                            event.startDateLong < e.endDateLong &&
                            event.endDateLong > e.startDateLong)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public interface EventRow
    {
        void setTimeGap(boolean b);

        void setTitleText(String s);

        void setTimeText(String s);

        void setSpeakerText(String s);

        void setDescription(String s);

        void setLiveNowVisible(boolean b);

        void setRsvpVisible(boolean rsvp, boolean past);

        void setRsvpConflict(boolean b);
    }
}
