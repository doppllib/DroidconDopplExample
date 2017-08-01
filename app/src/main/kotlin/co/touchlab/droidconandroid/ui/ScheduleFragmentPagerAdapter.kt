package co.touchlab.droidconandroid.ui

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import co.touchlab.droidconandroid.ScheduleDataFragment
import co.touchlab.droidconandroid.formatDate

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

    fun updateNotifCard() {
        for (fragment in fm.fragments) {
            (fragment as? ScheduleDataFragment)?.updateNotifCard()
        }
    }

    fun switchToAgenda() {
        for (fragment in fm.fragments) {
            (fragment as? ScheduleDataFragment)?.switchToAgenda()
        }
    }

    fun switchToConference() {
        for (fragment in fm.fragments) {
            (fragment as? ScheduleDataFragment)?.switchToConference()
        }
    }

    companion object {
        private val TIME_FORMAT = "MMM dd"
    }
}