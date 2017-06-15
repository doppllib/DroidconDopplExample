package co.touchlab.droidconandroid.shared.data2;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import co.touchlab.droidconandroid.shared.data.staff.EventAttendee;
import co.touchlab.droidconandroid.shared.data2.dao.BlockDao;
import co.touchlab.droidconandroid.shared.data2.dao.EventAttendeeDao;
import co.touchlab.droidconandroid.shared.data2.dao.EventDao;
import co.touchlab.droidconandroid.shared.data2.dao.EventSpeakerDao;
import co.touchlab.droidconandroid.shared.data2.dao.UserAccountDao;
import co.touchlab.droidconandroid.shared.data2.dao.VenueDao;

@Database(entities = {EventAttendee.class, Block.class, Event.class, EventSpeaker.class, UserAccount.class, Venue.class}, version = DroidconDatabase.DATABASE_VERSION)
public abstract class DroidconDatabase extends RoomDatabase {
    static final int DATABASE_VERSION = 1;

    public abstract BlockDao blockDao();

    public abstract UserAccountDao userAccountDao();

    public abstract VenueDao venueDao();

    public abstract EventSpeakerDao eventSpeakerDao();

    public abstract EventDao eventDao();

    public abstract EventAttendeeDao eventAttendeeDao();
}
