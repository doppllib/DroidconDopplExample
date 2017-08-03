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
import co.touchlab.droidconandroid.shared.data.DatabaseHelper
import co.touchlab.droidconandroid.shared.presenter.AppManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {

    companion object {
        private val TAG = "NotificationService"
        private val DROIDCON = "Droidcon"
        private val DESIGN = "Design"
        private val TYPE = "type"
        private val EVENT_ID = "eventId"
        private val VERSION_CODE = "versionCode"
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

            when (type) {
                "updateSchedule" -> TODO("Inject Refresh interactor and call refreshFromServer()")
                "event" -> {
                    val message = remoteMessage.notification.body
                    val eventId = data[EVENT_ID]!!.toLong()
                    DatabaseHelper.getInstance(this)
                            .getEventForId(eventId)
                            .filter { it != null }
                            .subscribe { event ->
                                val title = if (remoteMessage.notification.title.isNullOrBlank()) DROIDCON else remoteMessage.notification.title
                                val category = if (event.category.isNullOrBlank()) DESIGN else event.category
                                sendEventNotification(title!!, message!!, eventId, category)
                            }
                }
                "version" -> {
                    val pInfo = packageManager.getPackageInfo(packageName, 0)

                    val versionCode = pInfo.versionCode
                    val checkCode = data[VERSION_CODE]!!.toInt()
                    if (versionCode < checkCode) {
                        var intent = Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + packageName))

                        if (intent.resolveActivity(packageManager) == null) {
                            intent = Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName))
                        }

                        sendIntentNotification(getString(R.string.app_name),
                                "Please update your app",
                                intent)
                    }
                }
                "general" -> {
                    // Check if message contains a notification payload.
                    if (notification != null) {
                        sendNotification(notification)
                    }
                }
            }
        } catch(e: Exception) {
            Log.e(TAG, "onMessageReceived: ", e)
            AppManager.getPlatformClient().logException(e)
        }
    }

    private fun sendNotification(notification: RemoteMessage.Notification) {
        val intent = Intent(this, ScheduleActivity::class.java)
        val title = if (notification.title.isNullOrBlank()) DROIDCON else notification.title
        sendIntentNotification(title!!, notification.body!!, intent)
        //what if body is null?
    }

    private fun sendEventNotification(title: String, message: String, eventId: Long, category: String) {
        val intent = EventDetailActivity.createIntent(this, category, eventId)
        sendIntentNotification(title, message, intent)
    }

    private fun sendIntentNotification(title: String, message: String, intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}