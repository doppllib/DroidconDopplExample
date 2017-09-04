package co.touchlab.droidconandroid.shared.interactors;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.EventInfo;
import co.touchlab.droidconandroid.shared.data.UserAccount;
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
//        helper.flowEventInfo(eventId).subscribe(eventAndSpeakers -> {
//            System.out.println(eventAndSpeakers);
//        });
        Single<Event> eventSingle = helper.getEventForId(eventId);
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
                .flatMap(helper:: getUserAccountForId)
                .toList();
    }

    @NonNull
    private EventInfo createEventInfo(Event event, List<UserAccount> eventSpeakers, List<Event> allEvents)
    {
        //Flattened this out. Not sure we need to copy the array, but it was being done explicitly, so I kept.
        return new EventInfo(event, new ArrayList<>(eventSpeakers), hasConflict(event, allEvents));
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
