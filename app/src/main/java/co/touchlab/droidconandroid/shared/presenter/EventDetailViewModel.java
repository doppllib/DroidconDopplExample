package co.touchlab.droidconandroid.shared.presenter;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.google.j2objc.annotations.Weak;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.EventInfo;
import co.touchlab.droidconandroid.shared.data.Venue;
import co.touchlab.droidconandroid.shared.interactors.EventDetailInteractor;
import co.touchlab.droidconandroid.shared.interactors.EventVideoDetailsInteractor;
import co.touchlab.droidconandroid.shared.interactors.RsvpInteractor;
import co.touchlab.droidconandroid.shared.interactors.UpdateAlertsInteractor;
import co.touchlab.droidconandroid.shared.network.dao.EventVideoDetails;
import co.touchlab.droidconandroid.shared.tasks.persisted.RsvpJob;
import co.touchlab.droidconandroid.shared.utils.AnalyticsEvents;
import co.touchlab.droidconandroid.shared.utils.AnalyticsHelper;
import co.touchlab.droidconandroid.shared.utils.SlackUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static co.touchlab.droidconandroid.shared.presenter.AppManager.getContext;

/**
 * Created by kgalligan on 4/25/16.
 */
public class EventDetailViewModel extends ViewModel {
    @Weak
    private EventDetailHost host;
    private final EventDetailInteractor detailInteractor;
    private final EventVideoDetailsInteractor videoInteractor;
    private final RsvpInteractor rsvpInteractor;
    private final UpdateAlertsInteractor alertsInteractor;
    private CompositeDisposable disposables = new CompositeDisposable();

    private EventDetailViewModel(EventDetailInteractor detailInteractor,
                                 EventVideoDetailsInteractor videoInteractor,
                                 RsvpInteractor rsvpInteractor, UpdateAlertsInteractor alertsInteractor) {
        this.detailInteractor = detailInteractor;
        this.videoInteractor = videoInteractor;
        this.rsvpInteractor = rsvpInteractor;
        this.alertsInteractor = alertsInteractor;
    }

    public void register(EventDetailHost host) {
        this.host = host;
    }

    public void getDetails() {
        disposables.add(getEventDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventInfo -> {
                    AnalyticsHelper.recordAnalytics(AnalyticsEvents.OPEN_EVENT, eventInfo.event);
                    host.dataRefresh(eventInfo);
                }, e -> host.reportError("Error getting event details")));
    }

    public void callStartVideo(String link, String cover) {
        AnalyticsHelper.recordAnalytics(AnalyticsEvents.STREAM_CLICKED);
    }

    private Observable<EventInfo> getEventDetails() {
        return Observable.zip(
                detailInteractor.getEventInfo().toObservable(),
                videoInteractor.getVideoDetails(),
                this::getEventAndVideoInfo);
    }

    private EventInfo getEventAndVideoInfo(EventInfo eventInfo, EventVideoDetails videoDetails) {
        eventInfo.videoDetails = videoDetails;
        return eventInfo;
    }

    public void unregister() {
        disposables.clear();
        host = null;
    }

    public void toggleRsvp(boolean rsvp) {
        if (rsvp) {
            rsvpInteractor.addRsvp()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(event -> {
                                host.updateRsvp(event);
                                alertsInteractor.alert();
                            },
                            e -> Log.e("Error", "Error trying to add rsvp"));

        } else {
            rsvpInteractor.removeRsvp()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(event -> {
                                host.updateRsvp(event);
                                alertsInteractor.alert();
                            },
                            e -> Log.e("Error", "Error trying to remove rsvp"));
        }
    }

    public void setEventbriteEmail(String email, String link, String cover) {
        AppPrefs.getInstance(getContext()).setEventbriteEmail(email);
        callStartVideo(link, cover);
    }

    // FIXME: I think we're getting rid of Slack stuff
    public void openSlack() {
        disposables.add(detailInteractor.getEventVenue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::openSlackForVenue,
                        e -> host.reportError("Error trying to open Slack")));
    }

    private void openSlackForVenue(Venue venue) {
        String slackLink = SlackUtils.createSlackLink(venue);
        String slackLinkHttp = SlackUtils.createSlackLinkHttp(venue);
        host.openSlack(slackLink,
                slackLinkHttp,
                AppPrefs.getInstance(getContext()).getShowSlackDialog());
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final EventVideoDetailsInteractor videoInteractor;
        private final EventDetailInteractor detailInteractor;
        private final RsvpInteractor rsvpInteractor;
        private final UpdateAlertsInteractor alertsInteractor;

        public Factory(EventDetailInteractor detailInteractor,
                       EventVideoDetailsInteractor videoInteractor,
                       RsvpInteractor rsvpInteractor, UpdateAlertsInteractor alertsInteractor) {
            this.videoInteractor = videoInteractor;
            this.detailInteractor = detailInteractor;
            this.rsvpInteractor = rsvpInteractor;
            this.alertsInteractor = alertsInteractor;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new EventDetailViewModel(detailInteractor, videoInteractor, rsvpInteractor, alertsInteractor);
        }
    }
}
