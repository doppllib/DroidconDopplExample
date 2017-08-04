package co.touchlab.droidconandroid.shared.presenter;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.google.j2objc.annotations.Weak;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.EventInfo;
import co.touchlab.droidconandroid.shared.interactors.EventDetailInteractor;
import co.touchlab.droidconandroid.shared.interactors.RsvpInteractor;
import co.touchlab.droidconandroid.shared.interactors.UpdateAlertsInteractor;
import co.touchlab.droidconandroid.shared.utils.AnalyticsEvents;
import co.touchlab.droidconandroid.shared.utils.AnalyticsHelper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by kgalligan on 4/25/16.
 */
public class EventDetailViewModel extends ViewModel
{
    @Weak
    private       EventDetailHost        host;
    private final EventDetailInteractor  detailInteractor;
    private final RsvpInteractor         rsvpInteractor;
    private final UpdateAlertsInteractor alertsInteractor;
    private CompositeDisposable disposables = new CompositeDisposable();

    private EventDetailViewModel(EventDetailInteractor detailInteractor, RsvpInteractor rsvpInteractor, UpdateAlertsInteractor alertsInteractor)
    {
        this.detailInteractor = detailInteractor;
        this.rsvpInteractor = rsvpInteractor;
        this.alertsInteractor = alertsInteractor;
    }

    public void register(EventDetailHost host)
    {
        this.host = host;
    }

    public void getDetails(Long eventId)
    {
        disposables.add(getEventDetails(eventId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventInfo ->
                {
                    AnalyticsHelper.recordAnalytics(AnalyticsEvents.OPEN_EVENT, eventInfo.event);
                    host.dataRefresh(eventInfo);
                }, e -> host.reportError("Error getting event details")));
    }

    private Observable<EventInfo> getEventDetails(Long eventId)
    {
        return detailInteractor.getEventInfo(eventId).toObservable();
    }

    public void unregister()
    {
        disposables.clear();
        host = null;
    }

    public void setRsvp(boolean shouldRsvp, Long eventId)
    {
        if(shouldRsvp)
        {
            rsvpInteractor.addRsvp(eventId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(event ->
                    {
                        host.updateRsvp(event);
                        alertsInteractor.alert();
                    }, e -> Log.e("Error", "Error trying to add rsvp"));

        }
        else
        {
            rsvpInteractor.removeRsvp(eventId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(event ->
                    {
                        host.updateRsvp(event);
                        alertsInteractor.alert();
                    }, e -> Log.e("Error", "Error trying to remove rsvp"));
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {
        @Inject
        EventDetailInteractor  detailInteractor;
        @Inject
        RsvpInteractor         rsvpInteractor;
        @Inject
        UpdateAlertsInteractor alertsInteractor;

        public Factory()
        {

        }

        public Factory(EventDetailInteractor detailInteractor, RsvpInteractor rsvpInteractor, UpdateAlertsInteractor alertsInteractor)
        {
            this.detailInteractor = detailInteractor;
            this.rsvpInteractor = rsvpInteractor;
            this.alertsInteractor = alertsInteractor;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new EventDetailViewModel(detailInteractor, rsvpInteractor, alertsInteractor);
        }
    }
}
