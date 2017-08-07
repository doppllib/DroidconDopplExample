package co.touchlab.droidconandroid.shared.interactors;

import android.text.TextUtils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.EventInfo;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.data.Venue;
import io.reactivex.Single;

/**
 * Created by kgalligan on 4/7/16.
 */
public class EventDetailInteractor
{
    private final DatabaseHelper helper;

    @Inject
    public EventDetailInteractor(DatabaseHelper helper)
    {
        this.helper = helper;
    }

    public Single<EventInfo> getEventInfo(long eventId)
    {
        Single<Event> eventSingle = getEvent(eventId);
        Single<List<Event>> allEvents = helper.getEvents();
        Single<List<UserAccount>> speakersSingle = getEventSpeakers(eventId);

        return Single.zip(eventSingle, speakersSingle, allEvents, this :: createEventInfo);

    }

    private Single<List<UserAccount>> getEventSpeakers(long eventId)
    {
        return helper.getEventSpeakers(eventId)
                .toObservable()
                .flatMapIterable(list -> list)
                .map(eventSpeaker -> eventSpeaker.userAccountId)
                .flatMap(userId -> helper.getUserAccountForId(userId).toObservable())
                .toList();
    }

    public Single<Venue> getEventVenue(long eventId)
    {
        return getEvent(eventId).map(event -> event.venue);
    }

    private Single<Event> getEvent(long eventId)
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

    private static boolean hasConflict(Event event, List<Event> allEvents)
    {
        for(Event existingEvent : allEvents)
        {
            if(event.id != existingEvent.id && ! TextUtils.isEmpty(existingEvent.rsvpUuid) &&
                    event.startDateLong < existingEvent.endDateLong &&
                    event.endDateLong > existingEvent.startDateLong)
            {
                return true;
            }
        }

        return false;
    }
}
