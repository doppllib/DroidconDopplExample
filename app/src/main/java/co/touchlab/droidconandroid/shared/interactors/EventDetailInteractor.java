package co.touchlab.droidconandroid.shared.interactors;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.droidconandroid.shared.data2.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data2.Event;
import co.touchlab.droidconandroid.shared.data2.EventInfo;
import co.touchlab.droidconandroid.shared.data2.UserAccount;
import co.touchlab.droidconandroid.shared.data2.Venue;
import io.reactivex.Single;

/**
 * Created by kgalligan on 4/7/16.
 */
public class EventDetailInteractor {
    public final  long           eventId;
    private final DatabaseHelper helper;

    public EventDetailInteractor(DatabaseHelper helper, long eventId) {
        this.helper = helper;
        this.eventId = eventId;
    }

    public Single<EventInfo> getEventInfo() {
        Single<Event> eventSingle = getEvent();
        Single<List<Event>> allEvents = helper.getEvents();
        Single<List<UserAccount>> speakersSingle = getEventSpeakers();

        return Single.zip(eventSingle, speakersSingle, allEvents, this::createEventInfo);

    }

    private Single<List<UserAccount>> getEventSpeakers()
    {
        return helper.getEventSpeakers(eventId)
                .toObservable()
                .flatMapIterable(list -> list)
                .map(eventSpeaker -> eventSpeaker.userAccountId)
                .flatMap(userId -> helper.getUserAccountForId(userId).toObservable())
                .toList();
    }

    public Single<Venue> getEventVenue() {
        return getEvent().map(event -> event.venue);
    }

    private Single<Event> getEvent()
    {
        return helper.getEventForId(eventId);
    }

    @NonNull
    private EventInfo createEventInfo(Event event, List<UserAccount> eventSpeakers, List<Event> allEvents)
    {
        EventInfo info = new EventInfo();
        List<UserAccount> speakerList = new ArrayList<>();
        for(UserAccount speaker : eventSpeakers)
        {
            speakerList.add(speaker);
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
