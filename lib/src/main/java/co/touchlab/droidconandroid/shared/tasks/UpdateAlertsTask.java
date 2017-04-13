package co.touchlab.droidconandroid.shared.tasks;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDayHolder;
import co.touchlab.droidconandroid.shared.presenter.ScheduleBlockHour;

/**
 * Created by samuelhill on 7/5/16.
 */
public class UpdateAlertsTask extends Task
{
    public Event nextEvent = null;

    public static final long ALERT_BUFFER = TimeUnit.MINUTES.toMillis(5);

    @Override
    protected void run(Context context) throws Throwable
    {
        if(AppPrefs.getInstance(context).getAllowNotifications())
        {
            ConferenceDayHolder[] conferenceDayHolders = ConferenceDataHelper.listDays(context,
                    false);
            for(ConferenceDayHolder day : conferenceDayHolders)
            {
                for(ScheduleBlockHour hour : day.hourHolders)
                {
                    if(hour.scheduleBlock instanceof Event)
                    {
                        Event event = (Event) hour.scheduleBlock;
                        if(event.getStartLong() - ALERT_BUFFER > System.currentTimeMillis())
                        {
                            nextEvent = event;
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
    }
}
