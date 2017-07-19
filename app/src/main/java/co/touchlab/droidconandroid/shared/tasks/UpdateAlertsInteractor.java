package co.touchlab.droidconandroid.shared.tasks;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDayHolder;
import co.touchlab.droidconandroid.shared.presenter.ScheduleBlockHour;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by samuelhill on 7/5/16.
 */
public class UpdateAlertsInteractor {
    private final DatabaseHelper helper;
    private final AppPrefs prefs;
    private PublishSubject<Event> alerts = PublishSubject.create(); // This might need to be a Relay

    public static final long ALERT_BUFFER = TimeUnit.MINUTES.toMillis(5);

    public UpdateAlertsInteractor(DatabaseHelper helper, AppPrefs prefs) {
        this.helper = helper;
        this.prefs = prefs;
    }

    public Observable<Event> getAlerts() {
        return alerts;
    }

    public void run() {
        if (prefs.getAllowNotifications()) {
            ConferenceDataHelper.getDays(helper, false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::update,
                            e -> Log.e("UpdateError", "Error retrieving data"));
        }
    }

    private void update(ConferenceDayHolder[] conferenceDayHolders) {
        for (ConferenceDayHolder day : conferenceDayHolders) {
            for (ScheduleBlockHour hour : day.hourHolders) {
                if (hour.scheduleBlock instanceof Event) {
                    Event event = (Event) hour.scheduleBlock;
                    if (event.getStartLong() - ALERT_BUFFER > System.currentTimeMillis()) {
                        alerts.onNext(event);
                        return;
                    }
                }
            }
        }
    }

}