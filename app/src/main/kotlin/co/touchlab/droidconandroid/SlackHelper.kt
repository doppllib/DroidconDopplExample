package co.touchlab.droidconandroid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Toast
import co.touchlab.droidconandroid.data.AppPrefs
import kotlinx.android.synthetic.main.view_slack_dialog.view.*

class SlackHelper {
    companion object {
        fun openSlack(activity: Activity, deepLink: String, httpLink: String, showSlackDialog: Boolean) {
            if (showSlackDialog) {
                val bodyView = LayoutInflater.from(activity).inflate(R.layout.view_slack_dialog, null)
                val builder = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
                builder.setTitle("Almost there...")
                builder.setView(bodyView)
                builder.setNegativeButton("Yep, take me to Slack", { dialogInterface, i ->
                    AppPrefs.getInstance(activity).showSlackDialog = ! bodyView.dont_ask.isChecked
                    launchSlack(activity, deepLink, httpLink)
                })
                builder.setPositiveButton("Not yet", { dialogInterface, i ->
                    launchSlack(activity, "http://sched.droidcon.nyc/dataTest/slackInvite")
                })
                val dialog = builder.show()
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(activity, R.color.text_gray))

            }
            else
            {
                launchSlack(activity, deepLink, httpLink)
            }
        }

        private fun launchSlack(activity: Activity, deepLink: String, httpLink: String = "http://sched.droidcon.nyc/dataTest/slackInvite") {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
            if(intent.resolveActivity(activity.packageManager) == null)
            {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(httpLink))
                if(intent.resolveActivity(activity.packageManager) == null)
                {
                    // shouldn't get here, but just in case
                    Toast.makeText(activity, "Error opening Slack link", Snackbar.LENGTH_SHORT).show()
                    return
                }
            }
            activity.startActivity(intent)
        }
    }
}


