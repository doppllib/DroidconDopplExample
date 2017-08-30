package co.touchlab.droidconandroid.alerts

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.util.Log
import co.touchlab.droidconandroid.EventDetailActivity
import co.touchlab.droidconandroid.R
import co.touchlab.droidconandroid.ScheduleActivity
import co.touchlab.droidconandroid.shared.viewmodel.AppManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {

    companion object {
        private val TAG = "NotificationService"
        val TYPE = "type"
        val EVENT_ID = "eventId"
        val VERSION_CODE = "versionCode"
    }

    /**
     * Message types:
     * 1. Notification messages delivered when your app is in the background. In this case,
     *    the notification is delivered to the device’s system tray. A user tap on a notification
     *    opens the app launcher by default.
     * 2. Messages with both notification and data payload, both background and foreground.
     *    In this case, the notification is delivered to the device’s system tray, and the data
     *    payload is delivered in the extras of the intent of your launcher Activity.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            val data = remoteMessage.data
            val type = data[TYPE]
            val notification = remoteMessage.notification
            Log.d(TAG, "Received firebase message: " + type)

            if (notification != null) {
                when (type) {
                    "updateSchedule" -> {
                        AppManager.getInstance().appComponent.refreshScheduleInteractor().refreshFromServer()
                    }
                    "event" -> {
                        val eventId = data[EVENT_ID]!!.toLong()
                        sendEventNotification(notification, eventId, NotificationUtils.EVENT_CHANNEL_ID)
                    }
                    "version" -> {
                        val packageInfo = packageManager.getPackageInfo(packageName, 0)
                        val versionCode = packageInfo.versionCode
                        val checkCode = data[VERSION_CODE]!!.toInt()
                        if (versionCode < checkCode) {
                            var intent = Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + packageName))

                            if (intent.resolveActivity(packageManager) == null) {
                                intent = Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=" + packageName))
                            }
                            sendIntentNotification(notification, intent, NotificationUtils.VERSION_CHANNEL_ID)
                        }
                    }
                    "general" -> {
                        val intent = Intent(this, ScheduleActivity::class.java)
                        sendIntentNotification(notification, intent, NotificationUtils.GENERAL_CHANNEL_ID)
                    }
                }
            }
        } catch(e: Exception) {
            Log.e(TAG, "onMessageReceived error: ", e)
        }
    }

    private fun sendEventNotification(notification: RemoteMessage.Notification, eventId: Long, channelId: String) {
        val intent = EventDetailActivity.createIntent(this, eventId)
        sendIntentNotification(notification, intent, channelId)
    }

    private fun sendIntentNotification(notification: RemoteMessage.Notification, intent: Intent, channelId: String) {
        val title = if (notification.title.isNullOrBlank()) getString(R.string.app_name) else notification.title
        val message = if (notification.body.isNullOrBlank()) getString(R.string.version_message) else notification.body

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_smallicon_color)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setChannelId(channelId)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}