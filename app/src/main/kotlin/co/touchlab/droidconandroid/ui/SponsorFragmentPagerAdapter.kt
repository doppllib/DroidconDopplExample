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
            SponsorsTask.SPONSOR_GENERAL -> return context.getString(R.string.sponsors_tab_general)
            SponsorsTask.SPONSOR_STREAMING -> return context.getString(R.string.sponsors_tab_streaming)
            SponsorsTask.SPONSOR_PARTY -> return context.getString(R.string.sponsors_tab_party)
            else -> { // Note the block
                return super.getPageTitle(position)
            }
        }
    }

    companion object {
        private val SPONSOR_COUNT = 3
    }
}