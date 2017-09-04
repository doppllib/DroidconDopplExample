package co.touchlab.droidconandroid.shared.viewmodel;

import co.touchlab.droidconandroid.shared.data.TimeBlock;

/**
 * Created by kgalligan on 4/25/16.
 */
public class HourBlock
{
    private final String    hourStringDisplay;
    private final TimeBlock timeBlock;

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
