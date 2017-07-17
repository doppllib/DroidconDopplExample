package co.touchlab.droidconandroid.shared.presenter;

import android.util.Log;

import com.google.j2objc.annotations.Weak;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.EventInfo;
import co.touchlab.droidconandroid.shared.data.Venue;
import co.touchlab.droidconandroid.shared.interactors.EventDetailInteractor;
import co.touchlab.droidconandroid.shared.network.dao.EventVideoDetails;
import co.touchlab.droidconandroid.shared.tasks.AddRsvpTask;
import co.touchlab.droidconandroid.shared.interactors.EventVideoDetailsInteractor;
import co.touchlab.droidconandroid.shared.tasks.RemoveRsvpTask;
import co.touchlab.droidconandroid.shared.utils.AnalyticsEvents;
import co.touchlab.droidconandroid.shared.utils.SlackUtils;
import co.touchlab.droidconandroid.shared.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static co.touchlab.droidconandroid.shared.presenter.AppManager.getContext;

/**
 * Created by kgalligan on 4/25/16.
 */
public class EventDetailPresenter {
    @Weak
    private EventDetailHost host;
    private EventDetailInteractor detailInteractor;
    private EventVideoDetailsInteractor videoInteractor;
    private CompositeDisposable disposables = new CompositeDisposable();

    public EventDetailPresenter(EventDetailInteractor detailInteractor, EventVideoDetailsInteractor videoInteractor) {
        this.detailInteractor = detailInteractor;
        this.videoInteractor = videoInteractor;
    }

    public void register(EventDetailHost host) {
        this.host = host;
    }

    public void getDetails() {
        disposables.add(getEventDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventInfo -> {
                    Log.d("Success", "Successfully got stuff, " + eventInfo.event.getName());
                    recordAnalytics(AnalyticsEvents.OPEN_EVENT, eventInfo.event);
                    host.dataRefresh(eventInfo);
                }, e -> {
                    Log.d("Failure", "Failed to get things, " + e.getLocalizedMessage());
                    host.reportError("Error getting event details");
                }));
    }

    public void callStartVideo(String link, String cover) {
        recordAnalytics(AnalyticsEvents.STREAM_CLICKED);
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

    private void recordAnalytics(String analyticsKey) {
        // Do nothing for now
    }

    private void recordAnalytics(String analyticsKey, Event event) {
        String eventName = event != null ? StringUtils.trimToEmpty(event.getName()) : "";
        AppManager.getPlatformClient().logEvent(
                analyticsKey,
                AnalyticsEvents.PARAM_ITEM_ID,
                Long.toString(event.getId()),
                AnalyticsEvents.PARAM_ITEM_NAME,
                eventName
        );
    }

    public void onEventMainThread(RemoveRsvpTask task) {
        getDetails();
    }

    public void onEventMainThread(AddRsvpTask task) {
        getDetails();
    }

    public void unregister() {
        disposables.clear();
        host = null;
    }

//    public void toggleRsvp()
//    {
//        if(! ready())
//        {
//            return;
//        }
//
//        long eventId = eventDetailInteractor.event.id;
//        if(eventDetailInteractor.event.isRsvped())
//        {
//            TaskQueue.loadQueueDefault(getContext())
//                    .execute(new RemoveRsvpTask(eventId));
//            recordAnalytics(AnalyticsEvents.UNRSVP_EVENT);
//        }
//        else
//        {
//            TaskQueue.loadQueueDefault(getContext()).execute(new AddRsvpTask(eventId));
//            recordAnalytics(AnalyticsEvents.RSVP_EVENT);
//        }
//    }

    public void setEventbriteEmail(String email, String link, String cover) {
        AppPrefs.getInstance(getContext()).setEventbriteEmail(email);
        callStartVideo(link, cover);
    }

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
}
