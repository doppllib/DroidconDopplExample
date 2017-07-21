package co.touchlab.droidconandroid.alerts

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.annotation.UiThread
import co.touchlab.droidconandroid.shared.data.Event
import co.touchlab.droidconandroid.shared.interactors.UpdateAlertsInteractor

private const val ALARM_REQUEST_CODE = 100
private const val INTENT_CATEGORY = "android.intent.category.DEFAULT"

@UiThread
fun scheduleAlert(context: Context, nextEvent: Event?) {
    if (nextEvent == null)
        clearAlerts(context)
    else
        setAlarm(context, nextEvent)
}

private fun setAlarm(context: Context, nextEvent: Event) {
    val notificationIntent = Intent(ALERT_ACTION)
            .addCategory(INTENT_CATEGORY)
            .putExtra(EXTRA_EVENT_NAME, nextEvent.name)
            .putExtra(EXTRA_EVENT_ID, nextEvent.id)
            .putExtra(EXTRA_EVENT_CATEGORY, nextEvent.category)

    val broadcast = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.set(AlarmManager.RTC_WAKEUP, nextEvent.startDateLong - UpdateAlertsInteractor.ALERT_BUFFER, broadcast)
}

private fun clearAlerts(context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()

    val notificationIntent = Intent(ALERT_ACTION)
    notificationIntent.addCategory(INTENT_CATEGORY)
    val broadcast = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_NO_CREATE)
    broadcast?.let {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(it)
        it.cancel()
    }
}
