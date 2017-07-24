package co.touchlab.droidconandroid.shared.data.staff;

import co.touchlab.droidconandroid.shared.data2.Event;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.squeaky.field.DatabaseField;
import co.touchlab.squeaky.table.DatabaseTable;

/**
 * Created by kgalligan on 6/28/14.
 */
@DatabaseTable
public class EventAttendee
{
    @DatabaseField(id = true)
    public Long id;

    @DatabaseField(foreign = true, canBeNull = false)
    public Event event;

    @DatabaseField(foreign = true, canBeNull = false)
    public UserAccount userAccount;

    @DatabaseField
    public Long startDate;

    @DatabaseField
    public Long endDate;

    @DatabaseField
    public String uuid;
}
