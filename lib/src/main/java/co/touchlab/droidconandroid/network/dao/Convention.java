package co.touchlab.droidconandroid.network.dao;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.squeaky.field.DatabaseField;


/**
 * Created by kgalligan on 7/19/14.
 */
public class Convention
{
    public Long   id;
    public String description;
    public String locationName;

    @DatabaseField
    public String startDate;

    @DatabaseField
    public String endDate;

    @NotNull
    public List<Venue> venues = new ArrayList<Venue>();

    @NotNull
    public List<Block> blocks = new ArrayList<>();
}
