package co.touchlab.droidconandroid.shared.network.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by kgalligan on 7/19/14.
 */
public class Venue extends co.touchlab.droidconandroid.shared.data.Venue
{
    @Nonnull

    public List<Event> events = new ArrayList<Event>();
}
