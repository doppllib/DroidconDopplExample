package co.touchlab.droidconandroid.shared.data2;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import co.touchlab.droidconandroid.shared.data2.dao.EventDao;
import co.touchlab.droidconandroid.shared.data2.dao.UserAccountDao;
import io.reactivex.Completable;
import io.reactivex.Single;

public class DatabaseHelper
{

    private static DatabaseHelper   instance;
    private        DroidconDatabase db;

    private DatabaseHelper(Context context)
    {
        db = Room.databaseBuilder(context, DroidconDatabase.class, "droidcon").build();
    }

    @NonNull
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

    public Single<List<Event>> getEventsWithRsvps()
    {
        return Single.fromCallable(() -> db.eventDao().rsvpUuidNotNull());
    }

    public Single<List<Event>> getEvents()
    {
        return Single.fromCallable(() -> db.eventDao().getEvents());
    }

    public Completable createOrUpdateEvent(Event event)
    {
        return Completable.fromAction(() -> db.eventDao().createOrUpdate(event));
    }

    public Completable updateEvent(Event event)
    {
        return Completable.fromAction(() -> db.eventDao().updateEvent(event));
    }

    public Completable deleteEvents(List<Event> events)
    {
        return Completable.fromAction(() -> db.eventDao().deleteAll(events));
    }

    public Single<List<Block>> getBlocks()
    {
        return Single.fromCallable(() -> db.blockDao().getBlocks());
    }

    public Completable deleteBlocks(List<Block> blocks)
    {
        return Completable.fromAction(() -> db.blockDao().deleteAll(blocks));
    }

    public Single<List<EventSpeaker>> getEventSpeakers(long eventId, long userId)
    {
        return Single.fromCallable(() -> db.eventSpeakerDao().getEventSpeakers(eventId, userId));
    }

    public Single<List<EventSpeaker>> getEventSpeakers(long eventId)
    {
        return Single.fromCallable(() -> db.eventSpeakerDao().getEventSpeakers(eventId));
    }

    public Single<UserAccount> findUserByCode(long userId)
    {
        return Single.fromCallable(() -> db.userAccountDao().getUserAccount(userId));
    }

    // Putting networking models into db

    public Completable saveUserAccount(co.touchlab.droidconandroid.shared.network.dao.UserAccount ua)
    {
        UserAccount dbUa = userAccountToDb(ua);

        UserAccountDao dao = db.userAccountDao();
        return Completable.fromAction(() -> dao.createOrUpdate(dbUa));
    }

    @NonNull
    public UserAccount userAccountToDb(co.touchlab.droidconandroid.shared.network.dao.UserAccount ua)
    {
        UserAccount dbUa = new UserAccount();
        dbUa.id = ua.id;
        dbUa.name = ua.name;
        dbUa.profile = ua.profile;
        dbUa.avatarKey = ua.avatarKey;
        dbUa.userCode = ua.userCode;
        dbUa.company = ua.company;
        dbUa.twitter = ua.twitter;
        dbUa.linkedIn = ua.linkedIn;
        dbUa.website = ua.website;
        dbUa.following = ua.following;
        dbUa.gPlus = ua.gPlus;
        dbUa.phone = ua.phone;
        dbUa.email = ua.email;
        dbUa.coverKey = ua.coverKey;
        dbUa.facebook = ua.facebook;
        dbUa.emailPublic = ua.emailPublic;
        return dbUa;
    }


}