package co.touchlab.droidconandroid.shared.interactors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.network.RsvpRequest;
import co.touchlab.droidconandroid.shared.utils.AnalyticsEvents;
import co.touchlab.droidconandroid.shared.utils.AnalyticsHelper;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kgalligan on 4/7/16.
 */
public class RsvpInteractor
{
    private final DatabaseHelper helper;
    private final RsvpRequest    request;
    private final AppPrefs       appPrefs;

    @Inject
    public RsvpInteractor(DatabaseHelper helper, RsvpRequest request, AppPrefs appPrefs)
    {
        this.helper = helper;
        this.request = request;
        this.appPrefs = appPrefs;
    }

    public Single<Event> addRsvp(Long eventId)
    {
        return helper.getEventForId(eventId)
                .filter(event -> event != null && event.rsvpUuid == null)
                .map(event -> setRsvp(event, appPrefs.getUserUniqueUuid()))
                .flatMapSingle(this :: updateDatabase)
                .flatMap(this :: updateBackend);
    }

    public Single<Event> removeRsvp(Long eventId)
    {
        return helper.getEventForId(eventId)
                .filter(event -> event != null)
                .map(event -> setRsvp(event, null))
                .flatMapSingle(this :: updateDatabase)
                .flatMap(this :: updateBackend);
    }

    private Single<Event> updateDatabase(Event event)
    {
        return Single.fromCallable(() ->
        {
            helper.updateEvent(event).subscribe();
            return event;
        });
    }

    private Single<Event> updateBackend(Event event)
    {
        if(event.rsvpUuid == null)
        {
            AnalyticsHelper.recordAnalytics(AnalyticsEvents.UNRSVP_EVENT, event.getId());
            return request.unRsvp(event.id, appPrefs.getUserUniqueUuid())
                    .flatMap(ignore -> Observable.just(event))
                    .firstOrError();
        }
        else
        {
            AnalyticsHelper.recordAnalytics(AnalyticsEvents.RSVP_EVENT, event.getId());
            return request.rsvp(event.id, appPrefs.getUserUniqueUuid())
                    .flatMap(ignore -> Observable.just(event))
                    .firstOrError();
        }
    }

    @NonNull
    private Event setRsvp(Event event, String uuid)
    {
        event.rsvpUuid = uuid;
        return event;
    }
}