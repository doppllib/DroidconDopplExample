package co.touchlab.droidconandroid.shared.data;

import co.touchlab.squeaky.field.DatabaseField;
import co.touchlab.squeaky.table.DatabaseTable;

/**
 * Created by kgalligan on 7/28/14.
 */
@DatabaseTable
public class EventSpeaker
{
    @DatabaseField(generatedId = true)
    public Integer id;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    public Event event;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    public UserAccount userAccount;

    @DatabaseField
    public int displayOrder;

    public Event getEvent()
    {
        return event;
    }

    public UserAccount getUserAccount()
    {
        return userAccount;
    }

    public int getDisplayOrder()
    {
        return displayOrder;
    }
}
