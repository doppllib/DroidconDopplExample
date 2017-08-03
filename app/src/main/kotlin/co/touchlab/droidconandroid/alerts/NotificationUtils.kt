package co.touchlab.droidconandroid.alerts

import android.annotation.TargetApi
import android.app.Notification
import android.content.Context
import android.content.ContextWrapper
import android.app.NotificationManager
import android.app.NotificationChannel
import android.graphics.Color
import android.os.Build
import co.touchlab.droidconandroid.R

@TargetApi(Build.VERSION_CODES.O)
class NotificationUtils(val context: Context, val manager: NotificationManager) : ContextWrapper(context) {

    companion object {
        val GENERAL_CHANNEL_ID = "co.touchlab.droidconandroid.alerts.GENERAL"
        val VERSION_CHANNEL_ID = "co.touchlab.droidconandroid.alerts.VERSION"
        val EVENT_CHANNEL_ID = "co.touchlab.droidconandroid.alerts.EVENT"
    }

    fun createChannels() {
        val generalName = context.getString(R.string.general_name)
        val generalDescription = context.getString(R.string.general_description)
        val generalUpdatesChannel = setUpChannel(GENERAL_CHANNEL_ID, generalName, generalDescription, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(generalUpdatesChannel)

        val versionName = context.getString(R.string.version_name)
        val versionDescription = context.getString(R.string.version_description)
        val appVersionChannel = setUpChannel(VERSION_CHANNEL_ID, versionName, versionDescription, NotificationManager.IMPORTANCE_LOW)
        manager.createNotificationChannel(appVersionChannel)

        val eventName = context.getString(R.string.event_name)
        val eventDescription = context.getString(R.string.event_description)
        val eventAlertChannel = setUpChannel(EVENT_CHANNEL_ID, eventName, eventDescription, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(eventAlertChannel)
    }

    private fun setUpChannel(id: String, name: String, des: String, importance: Int): NotificationChannel {
        return NotificationChannel(id, name, importance)
                .apply {
                    description = des
                    enableLights(true)
                    enableVibration(true)
                    lightColor = Color.GREEN
                    lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                }
    }
}