package co.touchlab.droidconandroid.alerts

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import co.touchlab.droidconandroid.DroidconApplication
import co.touchlab.droidconandroid.EventDetailActivity
import co.touchlab.droidconandroid.R

const val EXTRA_EVENT_NAME = "EXTRA_EVENT_NAME"
const val EXTRA_EVENT_ID = "EXTRA_EVENT_ID"
const val EXTRA_EVENT_CATEGORY = "EXTRA_EVENT_CATEGORY"
const val ALERT_ACTION = "co.touchlab.droidconandroid.DISPLAY_NOTIFICATION"

class AlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (ALERT_ACTION == intent.action) {

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val eventID = intent.getLongExtra(EXTRA_EVENT_ID, -1)
            if(eventID.toInt() == -1)
            {
                notificationManager.cancelAll()
            }
            else
            {
                val eventCategory = intent.getStringExtra(EXTRA_EVENT_CATEGORY)
                val eventName = intent.getStringExtra(EXTRA_EVENT_NAME)
                val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                val notificationIntent = EventDetailActivity.createIntent(context, eventCategory, eventID)
                val stackBuilder = TaskStackBuilder.create(context)
                stackBuilder.addParentStack(EventDetailActivity::class.java)
                stackBuilder.addNextIntent(notificationIntent)

                val pendingIntent = stackBuilder.getPendingIntent(13342, PendingIntent.FLAG_UPDATE_CURRENT)

                val notification = NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_smallicon_color)
                        .setContentTitle(context.getString(R.string.notif_title))
                        .setContentText(context.getString(R.string.notif_body, eventName))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setChannelId(NotificationUtils.EVENT_CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .build()

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(0, notification)
            }
        }
    }
}