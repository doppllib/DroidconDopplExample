package co.touchlab.droidconandroid.shared.data2;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import co.touchlab.droidconandroid.shared.utils.TimeUtils;

/**
 * Created by izzyoji :) on 8/12/15.
 */
@Entity
public class Block implements ScheduleBlock
{
    @PrimaryKey
    public long id;

    public String name;

    public String description;

    public Long startDateLong;

    public Long endDateLong;

    public String startDate;

    public String endDate;

    @Override
    public boolean isBlock()
    {
        return true;
    }

    @Override
    public Long getStartLong()
    {
        return startDateLong;
    }

    @Override
    public Long getEndLong()
    {
        return endDateLong;
    }

    public String getStartFormatted()
    {
        return TimeUtils.LOCAL_DATE_FORMAT.get().format(new Date(startDateLong));
    }

    public String getEndFormatted()
    {
        return TimeUtils.LOCAL_DATE_FORMAT.get().format(new Date(endDateLong));
    }
}
