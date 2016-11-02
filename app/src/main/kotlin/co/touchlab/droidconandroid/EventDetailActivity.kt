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
class EventDetailActivity : AppCompatActivity()
{
    companion object
    {
        val EVENT_ID = "EVENT_ID"
        val TRACK_ID = "TRACK_ID"
        fun callMe(a: Activity, id: Long, category: String?)
        {
            val i = createIntent(a, category, id)
            a.startActivity(i)
        }

        fun createIntent(a: Context?, category: String?, id: Long): Intent {
            val i = Intent(a, EventDetailActivity::class.java)
            i.putExtra(EVENT_ID, id)
            i.putExtra(TRACK_ID, category)
            return i
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}