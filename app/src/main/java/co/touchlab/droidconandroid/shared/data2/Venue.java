package co.touchlab.droidconandroid.shared.data2;

import android.arch.persistence.room.Ignore;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by kgalligan on 6/28/14.
 */
public class Venue
{
    public String name;

    public String description;

    @Nonnull
    @Ignore
    public List<Event> events = new ArrayList<Event>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
