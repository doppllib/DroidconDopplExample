package co.touchlab.droidconandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import co.touchlab.droidconandroid.ScheduleActivity.Companion.callMe
import co.touchlab.droidconandroid.shared.data.AppPrefs
import co.touchlab.droidconandroid.shared.presenter.AppManager
import co.touchlab.droidconandroid.ui.WelcomePagerAdapter
import kotlinx.android.synthetic.main.activity_welcome.*

/**
 *
 * Created by izzyoji :) on 7/21/15.
 */

class WelcomeActivity : AppCompatActivity() {
    companion object {
        private val LAST_INDEX = 1

        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            return intent
        }
    }

    val appPrefs: AppPrefs by lazy {
        AppManager.getInstance().appComponent.prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        viewPager.adapter = WelcomePagerAdapter(supportFragmentManager)
        indicator.setViewPager(viewPager)
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageSelected(position: Int) {
                if (LAST_INDEX > 2) {
                    if (position >= LAST_INDEX - 1) {
                        advanceTv.setTextColor(ContextCompat.getColor(this@WelcomeActivity, R.color.orange))
                        indicator.fillColor = ContextCompat.getColor(this@WelcomeActivity, R.color.orange)
                    } else {
                        advanceTv.setTextColor(ContextCompat.getColor(this@WelcomeActivity, R.color.white))
                        indicator.fillColor = ContextCompat.getColor(this@WelcomeActivity, R.color.white)
                    }
                }

                if (position == LAST_INDEX) {
                    advanceTv.setText(R.string.lets_go)
                } else {
                    advanceTv.setText(R.string.next)
                }
            }
        })

        advanceTv.setOnClickListener {
            val position = viewPager.currentItem
            when (position) {
                LAST_INDEX -> {
                    callMe(this@WelcomeActivity)
                    finish()
                }
                else -> {
                    indicator.setCurrentItem(position + 1)
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        appPrefs.setHasSeenWelcome()
    }
}
