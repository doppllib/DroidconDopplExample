package co.touchlab.droidconandroid.shared.interactors;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.UUID;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.network.RsvpRequest;
import co.touchlab.droidconandroid.shared.network.SponsorsRequest;
import co.touchlab.droidconandroid.shared.network.dao.JustId;
import co.touchlab.droidconandroid.shared.utils.AnalyticsEvents;
import co.touchlab.droidconandroid.shared.utils.AnalyticsHelper;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

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
            if(event.rsvpUuid == null)
            {
                Observable<JustId> response = request.unRsvp(event.id, appPrefs.getUserUniqueUuid());
                response.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(id -> Log.d("sz", "unrsvped = " + id.getId()));
                AnalyticsHelper.recordAnalytics(AnalyticsEvents.UNRSVP_EVENT, event.getId());
            }
            else
            {
                Observable<JustId> response = request.rsvp(event.id, appPrefs.getUserUniqueUuid());
                response.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(id -> Log.d("sz", "rsvped = " + id.getId()));
                AnalyticsHelper.recordAnalytics(AnalyticsEvents.RSVP_EVENT, event.getId());
            }
            return event;
        });
    }

    @NonNull
    private Event setRsvp(Event event, String uuid)
    {
        event.rsvpUuid = uuid;
        return event;
    }
}