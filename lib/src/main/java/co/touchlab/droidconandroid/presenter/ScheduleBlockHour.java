package co.touchlab.droidconandroid.presenter;
import co.touchlab.droidconandroid.data.ScheduleBlock;

/**
 * Created by kgalligan on 4/25/16.
 */
public class ScheduleBlockHour
{
    public final String hourStringDisplay;
    public final ScheduleBlock scheduleBlock;

    public ScheduleBlockHour(String hourStringDisplay, ScheduleBlock scheduleBlock)
    {
        this.hourStringDisplay = hourStringDisplay;
        this.scheduleBlock = scheduleBlock;
    }

    public String getHourStringDisplay()
    {
        return hourStringDisplay;
    }

    public ScheduleBlock getScheduleBlock()
    {
        return scheduleBlock;
    }
}
