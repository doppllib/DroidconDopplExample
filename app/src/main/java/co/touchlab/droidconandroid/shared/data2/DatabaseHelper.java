package co.touchlab.droidconandroid.shared.data2;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import co.touchlab.droidconandroid.shared.data2.dao.BlockDao;
import co.touchlab.droidconandroid.shared.data2.dao.EventDao;
import co.touchlab.droidconandroid.shared.data2.dao.EventSpeakerDao;
import co.touchlab.droidconandroid.shared.data2.dao.UserAccountDao;

public class DatabaseHelper {

    private static DatabaseHelper instance;
    private DroidconDatabase db;

    private DatabaseHelper(Context context) {
        db = Room.databaseBuilder(context, DroidconDatabase.class, "droidcon").build();
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }

        return instance;
    }

    public BlockDao getBlockDao() {
        return db.blockDao();
    }

    public UserAccountDao getUserAccountDao() {
        return db.userAccountDao();
    }

    public EventSpeakerDao getEventSpeakerDao() {
        return db.eventSpeakerDao();
    }

    public EventDao getEventDao() {
        return db.eventDao();
    }

    public void deleteEventsNotIn(Set<Long> goodStuff) {
        final EventDao dao = getEventDao();
        final List<Event> allEvents = dao.getEvents();

        final Iterator<Event> iterator = allEvents.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (goodStuff.contains(event.id))
                iterator.remove();
        }

        if (allEvents.size() > 0)
            dao.deleteAll(allEvents);
    }


}