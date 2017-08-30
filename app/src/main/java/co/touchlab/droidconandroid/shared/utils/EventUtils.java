package co.touchlab.droidconandroid.shared.utils;
import android.text.TextUtils;

import java.util.List;
import java.util.Locale;

import co.touchlab.droidconandroid.shared.data.Block;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.viewmodel.HourBlock;

public class EventUtils
{
    public static void styleEventRow(HourBlock scheduleBlockHour, List dataSet, EventRow row, boolean allEvents)
    {
        boolean isFirstInBlock = ! scheduleBlockHour.getHourStringDisplay().isEmpty();
        row.setTimeGap(isFirstInBlock);

        if(scheduleBlockHour.getTimeBlock().isBlock())
        {
            Block block = (Block) scheduleBlockHour.getTimeBlock();
            row.setTitleText(block.name);
            row.setTimeText(scheduleBlockHour.getHourStringDisplay().toLowerCase(Locale.getDefault()));
            row.setSpeakerText("");
            row.setDescription(block.description);
            row.setLiveNowVisible(false);
            row.setRsvpVisible(false, false);
            row.setRsvpConflict(false);
        }
        else
        {
            Event event = (Event) scheduleBlockHour.getTimeBlock();
            row.setTimeText(scheduleBlockHour.getHourStringDisplay().toLowerCase(Locale.getDefault()));
            row.setTitleText(event.name);
            row.setSpeakerText(event.allSpeakersString());
            row.setDescription(event.description);
            row.setLiveNowVisible(event.isNow());
            row.setRsvpVisible(allEvents && event.isRsvped(), event.isPast());
            row.setRsvpConflict(allEvents && hasConflict(event, dataSet));
        }
    }

    private static boolean hasConflict(Event event, List dataSet)
    {
        if(event.isRsvped() && ! event.isPast())
        {
            for(Object o : dataSet)
            {
                if(o instanceof HourBlock && ((HourBlock) o).getTimeBlock() instanceof Event)
                {
                    Event e = (Event) ((HourBlock) o).getTimeBlock();
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
