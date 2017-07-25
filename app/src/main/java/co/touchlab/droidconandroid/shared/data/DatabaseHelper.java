package co.touchlab.droidconandroid.shared.data;

import android.arch.persistence.room.Room;
import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import co.touchlab.droidconandroid.shared.data.dao.EventDao;
import co.touchlab.droidconandroid.shared.data.dao.UserAccountDao;
import io.reactivex.Completable;
import io.reactivex.Single;

public class DatabaseHelper
{

    private static DatabaseHelper   instance;
    private        DroidconDatabase db;

    private DatabaseHelper(Context context)
    {
        db = Room.databaseBuilder(context, DroidconDatabase.class, "droidcon")
                .build();
    }

    @NotNull
    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DatabaseHelper(context);
        }

        return instance;
    }

    public void deleteEventsNotIn(Set<Long> goodStuff)
    {
        final EventDao dao = db.eventDao();
        final List<Event> allEvents = dao.getEvents();

        final Iterator<Event> iterator = allEvents.iterator();
        while(iterator.hasNext())
        {
            Event event = iterator.next();
            if(goodStuff.contains(event.id))
            {
                iterator.remove();
            }
        }

        if(allEvents.size() > 0)
        {
            dao.deleteAll(allEvents);
        }
    }

    public Single<Event> getEventForId(long eventId)
    {
        return Single.fromCallable(() -> db.eventDao().getEventForId(eventId));
    }

    public String getRsvpUuidForEventWithId(long eventId)
    {
        return db.eventDao().getRsvpUuidForEventWithId(eventId);
    }

    public List<Event> getEventsWithRsvpsNotNull()
    {
        return db.eventDao().rsvpUuidNotNull();
    }

    public Single<List<Event>> getEvents()
    {
        return Single.fromCallable(this :: getEventsList);
    }

    public List<Event> getEventsList()
    {
        return db.eventDao().getEvents();
    }

    public void createEvent(Event event)
    {
        db.eventDao().createOrUpdate(event);
    }

    public Completable updateEvent(Event event)
    {
        return Completable.fromAction(() -> db.eventDao().updateEvent(event));
    }

    public void deleteEvents(List<Event> events)
    {
        db.eventDao().deleteAll(events);
    }

    public List<Block> getBlocksList()
    {
        return db.blockDao().getBlocks();
    }

    public void updateBlock(Block block)
    {
        db.blockDao().createOrUpdate(block);
    }

    public Completable deleteBlocks(List<Block> blocks)
    {
        return Completable.fromAction(() -> db.blockDao().deleteAll(blocks));
    }

    public EventSpeaker getSpeakerForEventWithId(long eventId, long userId)
    {
        return db.eventSpeakerDao().getSpeakerForEventWithId(eventId, userId);
    }

    public void updateSpeaker(EventSpeaker speaker)
    {
        db.eventSpeakerDao().createOrUpdate(speaker);
    }

    public Single<List<EventSpeaker>> getEventSpeakers(long eventId)
    {
        return Single.fromCallable(() -> db.eventSpeakerDao().getEventSpeakers(eventId));
    }

    public Single<UserAccount> getUserAccountForId(long userId)
    {
        return Single.fromCallable(() -> getUserAccount(userId));
    }

    public UserAccount getUserAccount(long userId)
    {
        return db.userAccountDao().getUserAccount(userId);
    }

    public Completable saveUserAccount(UserAccount userAccount)
    {
        UserAccountDao dao = db.userAccountDao();
        return Completable.fromAction(() -> dao.createOrUpdate(userAccount));
    }

    public void saveAccount(UserAccount userAccount)
    {
        UserAccountDao dao = db.userAccountDao();
        dao.createOrUpdate(userAccount);
    }
}