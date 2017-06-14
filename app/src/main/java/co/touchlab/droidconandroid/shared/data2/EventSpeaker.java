package co.touchlab.droidconandroid.shared.data2;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;


/**
 * Created by kgalligan on 7/28/14.
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = Event.class, parentColumns = "id", childColumns = "event_id"),
        @ForeignKey(entity = UserAccount.class, parentColumns = "id", childColumns = "user_id")
})
public class EventSpeaker {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "event_id")
    public int eventId;

    @ColumnInfo(name = "user_id")
    public int userAccountId;

    public int displayOrder;

    public int getEventId() {
        return eventId;
    }

    public int getUserAccountId() {
        return userAccountId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
