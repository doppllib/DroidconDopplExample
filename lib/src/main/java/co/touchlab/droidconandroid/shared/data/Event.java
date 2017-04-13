package co.touchlab.droidconandroid.shared.data;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.touchlab.droidconandroid.shared.utils.TimeUtils;
import co.touchlab.squeaky.field.DatabaseField;
import co.touchlab.squeaky.field.ForeignCollectionField;
import co.touchlab.squeaky.table.DatabaseTable;

/**
 * Created by kgalligan on 6/28/14.
 */
@DatabaseTable
public class Event implements ScheduleBlock
{
    @DatabaseField(id = true)
    public long id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String description;

    @DatabaseField
    public String category;

    @NotNull
    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    public Venue venue;

    @DatabaseField
    public Long startDateLong;

    @DatabaseField
    public Long endDateLong;

    @DatabaseField
    public boolean publicEvent;

    @DatabaseField
    public Integer rsvpLimit;

    @DatabaseField
    public Integer rsvpCount;

    @DatabaseField
    public String rsvpUuid;

    @ForeignCollectionField(foreignFieldName = "event")
    public List<EventSpeaker> speakerList;

    @DatabaseField
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

    @NotNull
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

    public List<EventSpeaker> getSpeakerList()
    {
        return speakerList;
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

    public String allSpeakersString()
    {
        List<String> names =new ArrayList<>();
        for(EventSpeaker eventSpeaker : speakerList)
        {
            UserAccount userAccount = eventSpeaker.userAccount;
            if(userAccount != null)
                names.add(userAccount.name);
        }

        return TextUtils.join(", ", names);
    }
}
