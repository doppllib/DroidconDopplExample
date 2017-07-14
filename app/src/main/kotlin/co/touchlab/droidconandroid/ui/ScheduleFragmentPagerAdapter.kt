package co.touchlab.droidconandroid.ui

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import co.touchlab.droidconandroid.ScheduleDataFragment
import co.touchlab.droidconandroid.formatDate
import co.touchlab.droidconandroid.shared.data.Track

/**
 * Created by sufeizhao on 7/11/17.
 */
class ScheduleFragmentPagerAdapter(private var fm: FragmentManager, private var dates: List<Long>, private var allEvents: Boolean) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return dates.size
    }

    override fun getItem(position: Int): ScheduleDataFragment {
        return ScheduleDataFragment.newInstance(allEvents, dates[position], position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return dates[position].formatDate(TIME_FORMAT)
    }

    fun updateFrags(track: Track) {
        for (fragment in fm.fragments) {
            (fragment as? ScheduleDataFragment)?.filter(track)
        }
    }

    fun updateNotifCard() {
        for (fragment in fm.fragments) {
            (fragment as? ScheduleDataFragment)?.updateNotifCard()
        }
    }

    companion object {
        private val TIME_FORMAT = "MMM dd"
    }
}