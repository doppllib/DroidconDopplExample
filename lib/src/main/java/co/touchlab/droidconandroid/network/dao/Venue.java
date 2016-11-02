package co.touchlab.droidconandroid.network.dao;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgalligan on 7/19/14.
 */
public class Venue extends co.touchlab.droidconandroid.data.Venue
{
    @NotNull
    public List<Event> events = new ArrayList<Event>();
}
