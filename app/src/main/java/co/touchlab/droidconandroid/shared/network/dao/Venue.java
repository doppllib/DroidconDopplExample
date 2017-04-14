package co.touchlab.droidconandroid.shared.network.dao;

import com.google.j2objc.annotations.Weak;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgalligan on 7/19/14.
 */
public class Venue extends co.touchlab.droidconandroid.shared.data.Venue
{
    @NotNull

    public List<Event> events = new ArrayList<Event>();
}
