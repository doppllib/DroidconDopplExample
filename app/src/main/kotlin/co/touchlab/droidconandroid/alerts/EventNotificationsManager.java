package co.touchlab.droidconandroid.alerts;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.TimeBlock;
import co.touchlab.droidconandroid.shared.interactors.RefreshScheduleInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;


import static co.touchlab.droidconandroid.alerts.AlertReceiverKt.ALERT_ACTION;
import static co.touchlab.droidconandroid.alerts.AlertReceiverKt.EXTRA_EVENT_CATEGORY;
import static co.touchlab.droidconandroid.alerts.AlertReceiverKt.EXTRA_EVENT_ID;
import static co.touchlab.droidconandroid.alerts.AlertReceiverKt.EXTRA_EVENT_NAME;

/**
 * Created by kgalligan on 9/23/17.
 */
public class EventNotificationsManager
{
    private final AppPrefs appPrefs;
    private final Application context;
    private final RefreshScheduleInteractor refreshScheduleInteractor;
    private final CompositeDisposable cd = new CompositeDisposable();

    private static final String             INTENT_CATEGORY    = "android.intent.category.DEFAULT";
    private static final int                ALARM_REQUEST_CODE = 100;
    private static final int                ALARM_CANCEL_REQUEST_CODE = 101;

    public EventNotificationsManager(AppPrefs appPrefs, Application application, RefreshScheduleInteractor refreshScheduleInteractor)
    {
        this.appPrefs = appPrefs;
        this.context = application;
        this.refreshScheduleInteractor = refreshScheduleInteractor;
        appPrefs.observeAllowNotifications()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> resetObserver());
    }

    public void resetObserver()
    {
        cd.clear();
        cd.add(refreshScheduleInteractor.getRawTimeUpdates()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this :: createAlertNextEvent));
    }

    private void createAlertNextEvent(List<TimeBlock> timeBlocks)
    {
        cancelAlerts();

        Event nextEvent = null;

        if(appPrefs.getAllowNotificationsUi())
        {
            for(TimeBlock timeBlock : timeBlocks)
            {
                if(timeBlock instanceof Event)
                {
                    Event event = (Event) timeBlock;

                    if(! event.isRsvped()) continue;

                    if(event.isNow() || event.isPast()) continue;

                    Long startDateSoon = event.getStartDateSoon();

                    //Alarms are inexact, so give a buffer before to avoid double setting alarm
                    if(startDateSoon == null || System.currentTimeMillis() > (startDateSoon - (5 * 60 * 1000)))
                        continue;

                    if(nextEvent == null || nextEvent.isAfter(event))
                    {
                        nextEvent = event;
                    }
                }
            }
        }

        if(nextEvent != null)
            alertEvent(nextEvent);
    }


    private void alertEvent(Event nextEvent)
    {
        Log.w(EventNotificationsManager.class.getSimpleName(), "Setting alert for "+ nextEvent.name);
        //Show notification
        Intent notificationIntent = new Intent(ALERT_ACTION)
                .addCategory(INTENT_CATEGORY)
                .putExtra(EXTRA_EVENT_NAME, nextEvent.name)
                .putExtra(EXTRA_EVENT_ID, nextEvent.id)
                .putExtra(EXTRA_EVENT_CATEGORY, nextEvent.category);

        PendingIntent broadcast = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        safeSetAlarm(nextEvent.getStartDateSoon() - rewindTime(), broadcast);

        //Cancel notification
        Intent cancelIntent = new Intent(ALERT_ACTION)
                .addCategory(INTENT_CATEGORY);

        PendingIntent cancelBroadcast = PendingIntent.getBroadcast(context,
                ALARM_CANCEL_REQUEST_CODE,
                cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        safeSetAlarm(nextEvent.startDateLong - rewindTime(), cancelBroadcast);
    }

    public static long rewindTime()
    {
        return (1000*60*60*24*4);
    }

    private void safeSetAlarm(long time, PendingIntent intent)
    {
        //don't set alarms for very near future (1 minute)
        if(System.currentTimeMillis() > time - (60 * 1000))
            return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, intent);
    }

    private void cancelAlerts()
    {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        Intent notificationIntent = new Intent(ALERT_ACTION);
        notificationIntent.addCategory(INTENT_CATEGORY);

        PendingIntent broadcast = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_NO_CREATE);
        if(broadcast != null)
        {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(broadcast);
            broadcast.cancel();
        }
    }
}
