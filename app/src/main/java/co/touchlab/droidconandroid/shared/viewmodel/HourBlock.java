package co.touchlab.droidconandroid.shared.viewmodel;

import android.support.annotation.NonNull;

import co.touchlab.droidconandroid.shared.data.Block;
import co.touchlab.droidconandroid.shared.data.Event;
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

    public Event getEvent()
    {
        if(timeBlock instanceof Event)
            return (Event)timeBlock;
        else
            return null;
    }

    public Block getBlock()
    {
        if(timeBlock instanceof Block)
            return (Block)timeBlock;
        else
            return null;
    }

    @NonNull
    public String getName(){return timeBlock == null ? hourStringDisplay : timeBlock.getName();}

    @NonNull
    public String getSpeakers(){return timeBlock == null ? "" : timeBlock.getSpeakers();}
}
