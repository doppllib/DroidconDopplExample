package co.touchlab.droidconandroid.shared.network.dao;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.droidconandroid.shared.data.Block;

/**
 * Created by kgalligan on 7/19/14.
 */
public class Convention
{
    public Long   id;
    public String description;
    public String locationName;

    public String startDate;

    public String endDate;

    @NonNull
    public List<NetworkVenue> venues = new ArrayList<NetworkVenue>();

    @NonNull
    public List<Block> blocks = new ArrayList<>();
}
