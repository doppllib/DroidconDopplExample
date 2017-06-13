package co.touchlab.droidconandroid.shared.network.dao;

import javax.annotation.Nonnull;

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

    @Nonnull
    public List<Venue> venues = new ArrayList<Venue>();

    @Nonnull
    public List<Block> blocks = new ArrayList<>();
}
