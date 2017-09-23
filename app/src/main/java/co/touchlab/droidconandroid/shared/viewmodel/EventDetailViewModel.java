package co.touchlab.droidconandroid.shared.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import javax.inject.Inject;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.EventInfo;
import co.touchlab.droidconandroid.shared.interactors.EventDetailInteractor;
import co.touchlab.droidconandroid.shared.interactors.RsvpInteractor;
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
    public interface Host
    {
        void dataRefresh(EventInfo eventInfo);
        void reportError(String error);
        void updateRsvp(Event event);
    }

    private EventDetailViewModel(EventDetailInteractor detailInteractor, RsvpInteractor rsvpInteractor)
    {
        this.detailInteractor = detailInteractor;
        this.rsvpInteractor = rsvpInteractor;
    }

    public void wire(Host host, long eventId)
    {
        disposables.add(getEventDetails(eventId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventInfo ->
                {
                    AnalyticsHelper.recordAnalytics(AnalyticsEvents.OPEN_EVENT, eventInfo.getEvent());
                    host.dataRefresh(eventInfo);
                }, e -> {
                    CrashReport.logException(e);
                    host.reportError("Error getting event details");
                }));
    }

    private Observable<EventInfo> getEventDetails(long eventId)
    {
        return detailInteractor.getEventInfo(eventId).toObservable();
    }

    private final EventDetailInteractor  detailInteractor;
    private final RsvpInteractor         rsvpInteractor;
    private CompositeDisposable disposables = new CompositeDisposable();

    public void unwire()
    {
        disposables.clear();
    }

    public void setRsvp(boolean shouldRsvp, long eventId, Host host)
    {
        if(shouldRsvp)
        {
            disposables.add(rsvpInteractor.addRsvp(eventId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(event ->
                    {
                        host.updateRsvp(event);
                    }, e -> Log.e("Error", "Error trying to add rsvp")));

        }
        else
        {
            disposables.add(rsvpInteractor.removeRsvp(eventId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(event ->
                    {
                        host.updateRsvp(event);
                    }, e -> Log.e("Error", "Error trying to remove rsvp")));
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {
        @Inject
        EventDetailInteractor  detailInteractor;
        @Inject
        RsvpInteractor         rsvpInteractor;

        private Factory()
        {

        }

        public Factory(EventDetailInteractor detailInteractor, RsvpInteractor rsvpInteractor)
        {
            this.detailInteractor = detailInteractor;
            this.rsvpInteractor = rsvpInteractor;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new EventDetailViewModel(detailInteractor, rsvpInteractor);
        }
    }

    public static Factory factory()
    {
        Factory factory = new EventDetailViewModel.Factory();
        AppManager.getInstance().getAppComponent().inject(factory);

        return factory;
    }

    public static EventDetailViewModel forIos()
    {
        return factory().create(EventDetailViewModel.class);
    }
}
