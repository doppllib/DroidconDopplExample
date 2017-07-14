package co.touchlab.droidconandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import co.touchlab.droidconandroid.ScheduleActivity.Companion.callMe

/**
 * Created by kgalligan on 7/27/14.
 */
class UserDetailActivity : AppCompatActivity(), UserDetailFragment.Companion.FinishListener {
    companion object {
        @JvmField
        val USER_ID = "USER_ID"

        fun callMe(activity: Activity, userId: Long) {
            val intent = Intent(activity, UserDetailActivity::class.java)
            intent.putExtra(USER_ID, userId)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
    }

    override fun onFragmentFinished() {
        if (isTaskRoot) {
            callMe(this)
        }
        finish()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
    }

    override fun onBackPressed() {
        onFragmentFinished()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onFragmentFinished()

        return super.onOptionsItemSelected(item)
    }
}