package co.touchlab.droidconandroid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

/**
 * Created by kgalligan on 7/27/14.
 */
class EventDetailActivity : AppCompatActivity() {
    companion object {
        private val EVENT_ID = "EVENT_ID"
        private val TRACK_ID = "TRACK_ID"

        fun callMe(activity: Activity, id: Long, category: String?) {
            val intent = createIntent(activity, category, id)
            activity.startActivity(intent)
        }

        fun createIntent(context: Context?, id: Long): Intent {
            val intent = Intent(context, EventDetailActivity::class.java)
            intent.putExtra(EVENT_ID, id)
            return intent
        }

        fun createIntent(context: Context?, category: String?, id: Long): Intent {
            val intent = Intent(context, EventDetailActivity::class.java)
            intent.putExtra(EVENT_ID, id)
            intent.putExtra(TRACK_ID, category)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}