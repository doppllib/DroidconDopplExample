package co.touchlab.droidconandroid.shared.network.sessionize;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {
        Session.class,
        Speaker.class,
        SessionSpeakerJoin.class
}, version = SessionizeDatabase.DATABASE_VERSION)
public abstract class SessionizeDatabase extends RoomDatabase
{
    static final int DATABASE_VERSION = 1;

    public abstract ScheduleDao getScheduleDao();
}
