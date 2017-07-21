package co.touchlab.droidconandroid.shared.data2;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import co.touchlab.droidconandroid.shared.data2.dao.EventDao;

public class DatabaseHelper {

    private static DatabaseHelper instance;
    private DroidconDatabase db;

    private DatabaseHelper(Context context) {
        db = Room.databaseBuilder(context, DroidconDatabase.class, "droidcon").build();
    }

    @NonNull
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }

        return instance;
    }

    public void deleteEventsNotIn(Set<Long> goodStuff) {
        final EventDao dao = db.eventDao();
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