package co.touchlab.droidconandroid.shared.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import co.touchlab.droidconandroid.shared.data.dao.BlockDao;
import co.touchlab.droidconandroid.shared.data.dao.EventDao;
import co.touchlab.droidconandroid.shared.data.dao.EventSpeakerDao;
import co.touchlab.droidconandroid.shared.data.dao.UserAccountDao;

@Database(entities = { Block.class, Event.class, EventSpeaker.class, UserAccount.class }, version = DroidconDatabase.DATABASE_VERSION)
public abstract class DroidconDatabase extends RoomDatabase {
    static final int DATABASE_VERSION = 1;

    public abstract BlockDao blockDao();

    public abstract UserAccountDao userAccountDao();

    public abstract EventSpeakerDao eventSpeakerDao();

    public abstract EventDao eventDao();

}
