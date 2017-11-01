package co.touchlab.droidconandroid.shared.viewmodel;

/**
 * Created by kgalligan on 4/17/16.
 */
public class DaySchedule
{
    private final String      dayString;
    private final HourBlock[] hourHolders;
    private final HourBlockCollection hourBlockCollection;

    public DaySchedule(String dayString, HourBlock[] hourHolders)
    {
        this.dayString = dayString;
        this.hourHolders = hourHolders;
        this.hourBlockCollection = new HourBlockCollection(hourHolders);
    }

    public String getDayString()
    {
        return dayString;
    }

    public HourBlock[] getHourHolders()
    {
        return hourHolders;
    }

    public HourBlockCollection getHourBlockCollection()
    {
        return hourBlockCollection;
    }

    public static class HourBlockCollection
    {
        private final HourBlock[] hourHolders;

        public HourBlockCollection(HourBlock[] hourHolders)
        {
            this.hourHolders = hourHolders;
        }

        public HourBlock get(int i)
        {
            return hourHolders[i];
        }

        public int size()
        {
            return hourHolders.length;
        }
    }
}
