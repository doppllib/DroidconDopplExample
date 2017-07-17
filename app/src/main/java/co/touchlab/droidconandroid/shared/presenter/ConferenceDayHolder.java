package co.touchlab.droidconandroid.shared.presenter;

import co.touchlab.droidconandroid.shared.data.ScheduleBlock;

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

    public String getDayString()
    {
        return dayString;
    }

    public ScheduleBlockHour[] getHourHolders()
    {
        return hourHolders;
    }

}
