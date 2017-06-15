package co.touchlab.droidconandroid.shared.data2;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import javax.annotation.Nonnull;

import co.touchlab.droidconandroid.shared.utils.TimeUtils;

/**
 * Created by kgalligan on 6/28/14.
 */
@Entity
public class Event implements ScheduleBlock
{
    @PrimaryKey
    public long id;

    public String name;

    public String description;

    public String category;

    @Embedded
    public Venue venue;

    public Long startDateLong;

    public Long endDateLong;

    public boolean publicEvent;

    public Integer rsvpLimit;

    public Integer rsvpCount;

    public String rsvpUuid;

    public Integer vote;

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getCategory()
    {
        return category;
    }

    @Nonnull
    public Venue getVenue()
    {
        return venue;
    }

    public Long getStartDateLong()
    {
        return startDateLong;
    }

    public Long getEndDateLong()
    {
        return endDateLong;
    }

    public boolean isPublicEvent()
    {
        return publicEvent;
    }

    public Integer getRsvpLimit()
    {
        return rsvpLimit;
    }

    public Integer getRsvpCount()
    {
        return rsvpCount;
    }

    public Integer getVote()
    {
        return vote;
    }

    public boolean isRsvped()
    {
        return rsvpUuid != null;
    }

    public boolean isPast()
    {
        return endDateLong != null && System.currentTimeMillis() > endDateLong;
    }

    public boolean isNow()
    {
        return startDateLong != null && endDateLong != null && System
                .currentTimeMillis() < endDateLong && System.currentTimeMillis() > startDateLong;
    }

    @Override
    public boolean isBlock()
    {
        return false;
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
        return TimeUtils.DATE_FORMAT.get().format(new Date(startDateLong));
    }

    public String getEndFormatted()
    {
        return TimeUtils.DATE_FORMAT.get().format(new Date(endDateLong));
    }

    public String getRsvpUuid()
    {
        return rsvpUuid;
    }

    public void setRsvpUuid(String rsvpUuid)
    {
        this.rsvpUuid = rsvpUuid;
    }

}
