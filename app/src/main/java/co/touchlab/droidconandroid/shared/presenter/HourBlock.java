package co.touchlab.droidconandroid.shared.presenter;

import co.touchlab.droidconandroid.shared.data2.TimeBlock;

/**
 * Created by kgalligan on 4/25/16.
 */
public class HourBlock
{
    public final String    hourStringDisplay;
    public final TimeBlock timeBlock;

    public HourBlock(String hourStringDisplay, TimeBlock timeBlock)
    {
        this.hourStringDisplay = hourStringDisplay;
        this.timeBlock = timeBlock;
    }

    public String getHourStringDisplay()
    {
        return hourStringDisplay;
    }

    public TimeBlock getTimeBlock()
    {
        return timeBlock;
    }
}
