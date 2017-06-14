package co.touchlab.droidconandroid.shared.data2;

import android.arch.persistence.room.Database;

import co.touchlab.droidconandroid.shared.data.staff.EventAttendee;

@Database(entities = {EventAttendee.class, Block.class, Event.class, EventSpeaker.class, UserAccount.class, Venue.class}, version = DroidconDatabase.DATABASE_VERSION)
public abstract class DroidconDatabase {
    static final int DATABASE_VERSION = 1;


}
