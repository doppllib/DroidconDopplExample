package co.touchlab.droidconandroid.shared.data;

import java.util.Date;

import co.touchlab.droidconandroid.shared.utils.TimeUtils;
import co.touchlab.squeaky.field.DatabaseField;
import co.touchlab.squeaky.table.DatabaseTable;

/**
 * Created by izzyoji :) on 8/12/15.
 */
@DatabaseTable
public class Block implements ScheduleBlock
{
    @DatabaseField(id = true)
    public long id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String description;

    @DatabaseField
    public Long startDateLong;

    @DatabaseField
    public Long endDateLong;

    @Override
    public boolean isBlock()
    {
        return true;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
