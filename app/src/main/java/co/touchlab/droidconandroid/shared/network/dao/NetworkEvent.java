package co.touchlab.droidconandroid.shared.network.dao;

import android.arch.persistence.room.Ignore;

import java.util.List;

import co.touchlab.droidconandroid.shared.data2.Event;

/**
 * Created by kgalligan on 7/19/14.
 */
public class NetworkEvent extends Event
{
    @Ignore
    public List<NetworkUserAccount> speakers;
}
