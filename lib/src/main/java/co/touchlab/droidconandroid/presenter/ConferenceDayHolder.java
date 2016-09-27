package co.touchlab.droidconandroid.presenter;
/**
 * Created by kgalligan on 4/17/16.
 */
public class ConferenceDayHolder
{
    public final String dayString;
    public final ScheduleBlockHour[] hourHolders;

    public ConferenceDayHolder(String dayString, ScheduleBlockHour[] hourHolders)
    {
        this.dayString = dayString;
        this.hourHolders = hourHolders;
    }


}
