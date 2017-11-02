package co.touchlab.droidconandroid.shared.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.text.TextUtils;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.touchlab.droidconandroid.alerts.EventNotificationsManager;
import co.touchlab.droidconandroid.shared.utils.TimeUtils;

/**
 * Created by kgalligan on 6/28/14.
 */
@Entity
public class Event implements TimeBlock
{
    public static final int SOONTIME = 5 * 60 * 1000;
    @PrimaryKey
    public long id;

    public String name;

    public String description;

    public String category;

    @Embedded(prefix = "venue_")
    public Venue venue;

    public Long startDateLong;

    public Long endDateLong;

    public boolean publicEvent;

    public Integer rsvpLimit;

    public Integer rsvpCount;

    public String rsvpUuid;

    public Integer vote;

    public String startDate;

    public String endDate;

    @Ignore
    public List<EventSpeaker> speakerList;

    public List<EventSpeaker> getSpeakerList() {
        return speakerList;
    }

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

    @NonNull
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

    public boolean isSoon()
    {
        return !isNow() && !isPast() && startDateLong != null && System.currentTimeMillis() > (startDateLong -
                SOONTIME);
    }

    public Long getStartDateSoon()
    {
        return startDateLong == null ? null : startDateLong - SOONTIME;
    }

    public boolean isPast()
    {
        return endDateLong != null && System.currentTimeMillis() > getAdjustedEndDateLong();
    }

    public boolean isNow()
    {
        return startDateLong != null && endDateLong != null &&
                System.currentTimeMillis() < endDateLong &&
                System.currentTimeMillis() > startDateLong;
    }

    private Long getAdjustedStartDateLong()
    {
        return startDateLong == null ? null : startDateLong - EventNotificationsManager.rewindTime();
    }

    private Long getAdjustedEndDateLong()
    {
        return endDateLong == null ? null : endDateLong - EventNotificationsManager.rewindTime();
    }

    public String getAdjustedEndDateString()
    {
        return new SimpleDateFormat("MM/dd HH:mm").format(new Date(getAdjustedEndDateLong()));
    }

    public boolean isAfter(Event event)
    {
        //Don't know?
        return ! (startDateLong == null || event.startDateLong == null) &&
                startDateLong > event.startDateLong;
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
        return TimeUtils.LOCAL_DATE_FORMAT.get().format(new Date(startDateLong));
    }

    public String getEndFormatted()
    {
        return TimeUtils.LOCAL_DATE_FORMAT.get().format(new Date(endDateLong));
    }

    public String getRsvpUuid()
    {
        return rsvpUuid;
    }

    public void setRsvpUuid(String rsvpUuid)
    {
        this.rsvpUuid = rsvpUuid;
    }


    public String allSpeakersString()
    {
        List<String> names = new ArrayList<>();
        for(EventSpeaker eventSpeaker : speakerList)
        {
            if(eventSpeaker != null)
            {
                names.add(eventSpeaker.name);
            }
        }

        return TextUtils.join(", ", names);
    }
}
