package co.touchlab.droidconandroid.shared.tasks;

import java.util.concurrent.TimeUnit;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDayHolder;
import co.touchlab.droidconandroid.shared.presenter.ScheduleBlockHour;

/**
 * Created by samuelhill on 7/5/16.
 */
public class UpdateAlertsInteractor {
    private final DatabaseHelper helper;
    private final AppPrefs prefs;
    public Event nextEvent = null;

    public static final long ALERT_BUFFER = TimeUnit.MINUTES.toMillis(5);

    public UpdateAlertsInteractor(DatabaseHelper helper, AppPrefs prefs) {
        this.helper = helper;
        this.prefs = prefs;
    }

    public void run() throws Throwable {
        if (prefs.getAllowNotifications()) {
            ConferenceDayHolder[] conferenceDayHolders = ConferenceDataHelper.listDays(helper,
                    false);
            for (ConferenceDayHolder day : conferenceDayHolders) {
                for (ScheduleBlockHour hour : day.hourHolders) {
                    if (hour.scheduleBlock instanceof Event) {
                        Event event = (Event) hour.scheduleBlock;
                        if (event.getStartLong() - ALERT_BUFFER > System.currentTimeMillis()) {
                            nextEvent = event;
                            return;
                        }
                    }
                }
            }
        }
    }

}
