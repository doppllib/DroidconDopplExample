package co.touchlab.droidconandroid.shared.data2;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import co.touchlab.droidconandroid.shared.data2.dao.EventDao;
import co.touchlab.droidconandroid.shared.data2.dao.UserAccountDao;
import co.touchlab.droidconandroid.shared.network.dao.NetworkUserAccount;
import io.reactivex.Completable;
import io.reactivex.Single;

public class DatabaseHelper
{

    private static DatabaseHelper   instance;
    private        DroidconDatabase db;

    private DatabaseHelper(Context context)
    {
        db = Room.databaseBuilder(context, DroidconDatabase.class, "droidcon")
                .allowMainThreadQueries()
                .build();
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

    public Single<String> getRsvpUuidForEventWithId(long eventId)
    {
        return Single.fromCallable(() -> getRsvpUuidForEventWithId2(eventId));
    }

    public String getRsvpUuidForEventWithId2(long eventId)
    {
        return db.eventDao().getRsvpUuidForEventWithId(eventId);
    }

    public Single<List<Event>> getEventsWithRsvps()
    {
        return Single.fromCallable(() -> getEventsWithRsvpsNotNull());
    }

    public List<Event> getEventsWithRsvpsNotNull()
    {
        return db.eventDao().rsvpUuidNotNull();
    }

    public Single<List<Event>> getEvents()
    {
        return Single.fromCallable(() -> getEvents2());
    }

    public List<Event> getEvents2()
    {
        return db.eventDao().getEvents();
    }

    public Completable createOrUpdateEvent(Event event)
    {
        return Completable.fromAction(() -> createOrUpdateEvent2(event));
    }

    public void createOrUpdateEvent2(Event event)
    {
        db.eventDao().createOrUpdate(event);
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
        return Single.fromCallable(this :: getBlocks2);
    }

    public List<Block> getBlocks2()
    {
        return db.blockDao().getBlocks();
    }

    public Completable createOrUpdateBlock(Block block)
    {
        return Completable.fromAction(() -> updateBlock(block));
    }

    public void updateBlock(Block block)
    {
        db.blockDao().createOrUpdate(block);
    }

    public Completable deleteBlocks(List<Block> blocks)
    {
        return Completable.fromAction(() -> db.blockDao().deleteAll(blocks));
    }

    public Single<EventSpeaker> getEventSpeaker(long eventId, long userId)
    {
        return Single.fromCallable(() -> getSpeakerForEventWithId(eventId, userId));
    }

    public EventSpeaker getSpeakerForEventWithId(long eventId, long userId)
    {
        return db.eventSpeakerDao().getSpeakerForEventWithId(eventId, userId);
    }

    public Completable createOrUpdateEventSpeaker(EventSpeaker speaker)
    {
        return Completable.fromAction(() -> updateSpeaker(speaker));
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

    public Completable saveUserAccount(NetworkUserAccount ua, UserAccount userAccount)
    {
        UserAccount dbUa = userAccountToDb(ua, userAccount);

        UserAccountDao dao = db.userAccountDao();
        return Completable.fromAction(() -> dao.createOrUpdate(dbUa));
    }

    public Completable saveUserAccount(UserAccount userAccount)
    {
        UserAccountDao dao = db.userAccountDao();
        return Completable.fromAction(() -> dao.createOrUpdate(userAccount));
    }

    public void saveUserAccount2(NetworkUserAccount ua, UserAccount userAccount)
    {
        UserAccount dbUa = userAccountToDb(ua, userAccount);

        UserAccountDao dao = db.userAccountDao();
        dao.createOrUpdate(dbUa);
    }

    @NonNull
    public UserAccount userAccountToDb(NetworkUserAccount ua, UserAccount dbUa)
    {
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