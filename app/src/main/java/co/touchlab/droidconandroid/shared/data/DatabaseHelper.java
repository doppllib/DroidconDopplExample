package co.touchlab.droidconandroid.shared.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import co.touchlab.droidconandroid.shared.data.dao.EventDao;
import co.touchlab.droidconandroid.shared.data.dao.UserAccountDao;
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

    public String getRsvpUuidForEventWithId(long eventId)
    {
        return db.eventDao().getRsvpUuidForEventWithId(eventId);
    }

    public Single<List<Event>> getEvents()
    {
        return Single.fromCallable(this :: getEventsList);
    }

    private List<Event> getEventsList()
    {
        return db.eventDao().getEvents();
    }

    public List<Event> getEventsWithSpeakersList()
    {
        return combineEventsWithSpeakers(getEventsList());
    }

    private List<Event> combineEventsWithSpeakers(List<Event> events)
    {
        for(Event event : events)
        {
            event.speakerList = db.eventSpeakerDao().getEventSpeakers(event.id);
        }

        return events;
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

    public void convertAndSaveUserAccount(NetworkUserAccount ua, UserAccount userAccount)
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