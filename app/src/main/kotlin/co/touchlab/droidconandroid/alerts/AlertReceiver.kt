package co.touchlab.droidconandroid.alerts

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.NotificationCompat
import co.touchlab.droidconandroid.EventDetailActivity
import co.touchlab.droidconandroid.R
import co.touchlab.droidconandroid.shared.data.AppPrefs
import co.touchlab.droidconandroid.shared.data.DatabaseHelper
import co.touchlab.droidconandroid.shared.tasks.UpdateAlertsInteractor

const val EXTRA_EVENT_NAME = "EXTRA_EVENT_NAME"
const val EXTRA_EVENT_ID = "EXTRA_EVENT_ID"
const val EXTRA_EVENT_CATEGORY = "EXTRA_EVENT_CATEGORY"
const val ALERT_ACTION = "co.touchlab.droidconandroid.DISPLAY_NOTIFICATION"

class AlertReceiver : BroadcastReceiver() {
    private lateinit var helper: DatabaseHelper
    private lateinit var prefs: AppPrefs

    override fun onReceive(context: Context, intent: Intent) {

        helper = DatabaseHelper.getInstance(context)
        prefs = AppPrefs.getInstance(context)
        val updateAlertsInteractor = UpdateAlertsInteractor(helper, prefs)

        if (ALERT_ACTION == intent.action) {

            val eventName = intent.getStringExtra(EXTRA_EVENT_NAME)
            val eventID = intent.getLongExtra(EXTRA_EVENT_ID, -1)
            val eventCategory = intent.getStringExtra(EXTRA_EVENT_CATEGORY)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notificationIntent = EventDetailActivity.createIntent(context, eventCategory, eventID)
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(EventDetailActivity::class.java)
            stackBuilder.addNextIntent(notificationIntent)

            val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(context.getString(R.string.notif_title))
                    .setContentText(context.getString(R.string.notif_body, eventName))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }

        //This receiver also gets triggered for time changes. Always update the alarms here
//        TaskQueue.loadQueueDefault(context).execute(UpdateAlertsInteractor())
        updateAlertsInteractor.alert()
    }
}