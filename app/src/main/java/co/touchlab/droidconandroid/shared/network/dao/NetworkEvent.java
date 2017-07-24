package co.touchlab.droidconandroid.shared.network.dao;

import android.arch.persistence.room.Ignore;

import java.util.List;

import co.touchlab.squeaky.field.DatabaseField;

/**
 * Created by kgalligan on 7/19/14.
 */
public class NetworkEvent extends co.touchlab.droidconandroid.shared.data.Event
{
    @DatabaseField
    public String startDate;

    @DatabaseField
    public String endDate;

    @Ignore
    public List<NetworkUserAccount> speakers;
}
