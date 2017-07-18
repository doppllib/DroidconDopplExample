package co.touchlab.droidconandroid.shared.interactors;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.EventInfo;
import co.touchlab.droidconandroid.shared.data.EventSpeaker;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.data.Venue;
import co.touchlab.squeaky.dao.Dao;
import io.reactivex.Single;

/**
 * Created by kgalligan on 4/7/16.
 */
public class EventDetailInteractor {
    public final long eventId;
    private final DatabaseHelper helper;

    public EventDetailInteractor(DatabaseHelper helper, long eventId) {
        this.helper = helper;
        this.eventId = eventId;
    }

    public Single<EventInfo> getEventInfo() {
        Dao<Event> eventDao = helper.getEventDao();
        Single<Event> eventSingle = getEvent(eventDao);
        Single<List<Event>> allEvents = Single.fromCallable(() -> eventDao.queryForAll().list());
        Single<List<EventSpeaker>> speakersSingle =
                Single.fromCallable(() -> helper.getEventSpeakerDao()
                        .queryForEq("event_id", eventId)
                        .list());

        return Single.zip(eventSingle, speakersSingle, allEvents, this::createEventInfo);

    }

    public Single<Venue> getEventVenue() {
        Dao<Event> eventDao = helper.getEventDao();
        return getEvent(eventDao).map(event -> event.venue);
    }

    private Single<Event> getEvent(Dao<Event> eventDao) {
        return Single.fromCallable(() -> eventDao.queryForId(eventId));
    }

    @NonNull
    private EventInfo createEventInfo(Event event, List<EventSpeaker> eventSpeakers, List<Event> allEvents) {
        EventInfo info = new EventInfo();
        List<UserAccount> speakerList = new ArrayList<>();
        for (EventSpeaker speaker : eventSpeakers) {
            speakerList.add(speaker.getUserAccount());
        }
        info.event = event;
        info.speakers = speakerList;
        info.conflict = hasConflict(event, allEvents);
        return info;
    }

    private static boolean hasConflict(Event event, List<Event> allEvents) {
        for (Event existingEvent : allEvents) {
            if (event.id != existingEvent.id
                    && !TextUtils.isEmpty(existingEvent.rsvpUuid)
                    && event.startDateLong < existingEvent.endDateLong
                    && event.endDateLong > existingEvent.startDateLong)
                return true;
        }

        return false;
    }
}
