package co.touchlab.droidconandroid.shared.interactors;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper;
import co.touchlab.droidconandroid.shared.presenter.DaySchedule;
import co.touchlab.droidconandroid.shared.presenter.HourBlock;
import co.touchlab.droidconandroid.shared.utils.EventBusExt;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by samuelhill on 7/5/16.
 */
public class UpdateAlertsInteractor {
    private final DatabaseHelper helper;
    private final AppPrefs prefs;
    public Event event;

    public static final long ALERT_BUFFER = TimeUnit.MINUTES.toMillis(5);

    public UpdateAlertsInteractor(DatabaseHelper helper, AppPrefs prefs) {
        this.helper = helper;
        this.prefs = prefs;
    }

    public void alert() {
        if (prefs.getAllowNotifications()) {
            ConferenceDataHelper.getDays(helper, false)
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
            for(HourBlock hour : day.hourHolders)
            {
                if(hour.timeBlock instanceof Event)
                {
                    Event event = (Event) hour.timeBlock;
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
