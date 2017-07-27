package co.touchlab.droidconandroid.shared.presenter;

/**
 * Created by kgalligan on 4/17/16.
 */
public class DaySchedule
{
    public final String      dayString;
    public final HourBlock[] hourHolders;

    public DaySchedule(String dayString, HourBlock[] hourHolders)
    {
        this.dayString = dayString;
        this.hourHolders = hourHolders;
    }

    public String getDayString()
    {
        return dayString;
    }

    public HourBlock[] getHourHolders()
    {
        return hourHolders;
    }
}
