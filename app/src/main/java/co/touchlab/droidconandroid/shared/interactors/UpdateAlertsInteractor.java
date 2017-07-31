package co.touchlab.droidconandroid.shared.interactors;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.presenter.DaySchedule;
import co.touchlab.droidconandroid.shared.presenter.HourBlock;
import co.touchlab.droidconandroid.shared.utils.EventBusExt;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by samuelhill on 7/5/16.
 */
public class UpdateAlertsInteractor {
    private final AppPrefs                  prefs;
    private final RefreshScheduleInteractor refreshInteractor;
    public        Event                     event;

    public static final long ALERT_BUFFER = TimeUnit.MINUTES.toMillis(5);

    public UpdateAlertsInteractor(AppPrefs prefs, RefreshScheduleInteractor refreshInteractor) {
        this.prefs = prefs;
        this.refreshInteractor = refreshInteractor;
    }

    public void alert() {
        if (prefs.getAllowNotifications()) {
            refreshInteractor.refreshFromDatabase();
            refreshInteractor.getDataStream(false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::update,
                            e -> Log.e("UpdateError", "Error retrieving data"));
        }
    }

    private void update(DaySchedule[] daySchedules)
    {
        for(DaySchedule day : daySchedules)
        {
            for(HourBlock hour : day.getHourHolders())
            {
                if(hour.getTimeBlock() instanceof Event)
                {
                    Event event = (Event) hour.getTimeBlock();
                    if (event.getStartLong() - ALERT_BUFFER > System.currentTimeMillis()) {
                        this.event = event;
                        EventBusExt.getDefault().post(this);
                        return;
                    }
                }
            }
        }
    }
}
