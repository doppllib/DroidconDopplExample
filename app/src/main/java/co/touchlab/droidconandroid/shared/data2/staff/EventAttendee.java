package co.touchlab.droidconandroid.shared.data2.staff;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import co.touchlab.droidconandroid.shared.data2.Event;
import co.touchlab.droidconandroid.shared.data2.UserAccount;

/**
 * Created by kgalligan on 6/28/14.
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = Event.class, parentColumns = "id", childColumns = "event_id"),
        @ForeignKey(entity = UserAccount.class, parentColumns = "id", childColumns = "user_id")
})
public class EventAttendee {
    @PrimaryKey
    public Long id;

    @ColumnInfo(name = "event_id")
    public int event;

    @ColumnInfo(name = "user_id")
    public UserAccount userAccount;

    public Long startDate;

    public Long endDate;

    public String uuid;
}
