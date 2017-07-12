package co.touchlab.droidconandroid.ui

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import co.touchlab.droidconandroid.R
import co.touchlab.droidconandroid.SponsorsListFragment
import co.touchlab.droidconandroid.shared.tasks.SponsorsTask

/**
 * Created by sufeizhao on 7/11/17.
 */
class SponsorFragmentPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return SPONSOR_COUNT
    }

    override fun getItem(position: Int): SponsorsListFragment {
        return SponsorsListFragment.newInstance(position)
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            // Emoji not supported in xml for anything below Android 6.0
            SponsorsTask.SPONSOR_GENERAL -> return context.getString(R.string.sponsors_tab_general) + String(Character.toChars(0x1F60E))
            SponsorsTask.SPONSOR_STREAMING -> return context.getString(R.string.sponsors_tab_streaming) + String(Character.toChars(0x1F4FA))
            SponsorsTask.SPONSOR_PARTY -> return context.getString(R.string.sponsors_tab_party) + String(Character.toChars(0x1F389))
            else -> return super.getPageTitle(position)
        }
    }

    companion object {
        private val SPONSOR_COUNT = 3
    }
}