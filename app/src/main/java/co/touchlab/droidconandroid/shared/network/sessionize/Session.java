package co.touchlab.droidconandroid.shared.network.sessionize;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Session
{
    @PrimaryKey
    @NonNull
    private String        id;
    private String        title;
    private String        description;
    private String        startsAt;
    private String        endsAt;
    private boolean       isServiceSession;
    private boolean       isPlenumSession;
    @Ignore
    private List<Speaker> speakers;
    @Ignore
    private List<Integer> categoryItems;

    public Session(@NonNull String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getStartsAt()
    {
        return startsAt;
    }

    public void setStartsAt(String startsAt)
    {
        this.startsAt = startsAt;
    }

    public String getEndsAt()
    {
        return endsAt;
    }

    public void setEndsAt(String endsAt)
    {
        this.endsAt = endsAt;
    }

    public boolean isServiceSession()
    {
        return isServiceSession;
    }

    public void setServiceSession(boolean serviceSession)
    {
        isServiceSession = serviceSession;
    }

    public boolean isPlenumSession()
    {
        return isPlenumSession;
    }

    public void setPlenumSession(boolean plenumSession)
    {
        isPlenumSession = plenumSession;
    }

    public List<String> getSpeakers()
    {
        List<String> speakerIds = new ArrayList<>();
        for(Speaker speaker : speakers) {
            speakerIds.add(speaker.getId());
        }

        return speakerIds;
    }

    public void setSpeakers(List<Speaker> speakers)
    {
        this.speakers = speakers;
    }

    public List<Integer> getCategoryItems()
    {
        return categoryItems;
    }

    public void setCategoryItems(List<Integer> categoryItems)
    {
        this.categoryItems = categoryItems;
    }
}
