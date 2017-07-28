package co.touchlab.droidconandroid.shared.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;


/**
 * Created by kgalligan on 7/28/14.
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = Event.class, parentColumns = "id", childColumns = "event_id", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = UserAccount.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE)
})
public class EventSpeaker {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "event_id")
    public long eventId;

    @ColumnInfo(name = "user_id")
    public long userAccountId;

    public String name;

    public int displayOrder;

    public long getEventId()
    {
        return eventId;
    }

    public long getUserAccountId()
    {
        return userAccountId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
