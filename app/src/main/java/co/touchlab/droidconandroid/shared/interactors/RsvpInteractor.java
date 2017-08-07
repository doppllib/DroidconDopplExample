package co.touchlab.droidconandroid.shared.interactors;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.utils.AnalyticsEvents;
import co.touchlab.droidconandroid.shared.utils.AnalyticsHelper;
import io.reactivex.Single;

/**
 * Created by kgalligan on 4/7/16.
 */
public class RsvpInteractor
{
    private final DatabaseHelper helper;

    @Inject
    public RsvpInteractor(DatabaseHelper helper)
    {
        this.helper = helper;
    }

    public Single<Event> addRsvp(Long eventId)
    {
        return helper.getEventForId(eventId)
                .filter(event -> event != null && event.rsvpUuid == null)
                .map(event -> setRsvp(event, UUID.randomUUID().toString()))
                .flatMapSingle(this :: updateEvent);
    }

    public Single<Event> removeRsvp(Long eventId)
    {
        return helper.getEventForId(eventId)
                .filter(event -> event != null)
                .map(event -> setRsvp(event, null)).flatMapSingle(this :: updateEvent);
    }

    private Single<Event> updateEvent(Event event)
    {
        return Single.fromCallable(() ->
        {
            helper.updateEvent(event).subscribe();
            // TODO post to backend
            if(event.rsvpUuid == null)
            {
                AnalyticsHelper.recordAnalytics(AnalyticsEvents.UNRSVP_EVENT, event.getId());
            }
            else
            {
                AnalyticsHelper.recordAnalytics(AnalyticsEvents.RSVP_EVENT, event.getId());
            }
            return event;
        });
    }

    @NotNull
    private Event setRsvp(Event event, String uuid)
    {
        event.rsvpUuid = uuid;
        return event;
    }

}