package co.touchlab.droidconandroid.ui

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import co.touchlab.droidconandroid.R
import co.touchlab.droidconandroid.SponsorsListFragment
import co.touchlab.droidconandroid.shared.interactors.SponsorsInteractor

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
        return when (position) {
            // Emoji not supported in xml for anything below Android 6.0
            SponsorsInteractor.SPONSOR_GENERAL -> context.getString(R.string.sponsors_tab_general) + String(Character.toChars(UNICODE_COOL_EMOJI))
            SponsorsInteractor.SPONSOR_STREAMING -> context.getString(R.string.sponsors_tab_streaming) + String(Character.toChars(UNICODE_TV_EMOJI))
            SponsorsInteractor.SPONSOR_PARTY -> context.getString(R.string.sponsors_tab_party) + String(Character.toChars(UNICODE_PARTY_EMOJI))
            else -> super.getPageTitle(position)
        }
    }

    companion object {
        private val SPONSOR_COUNT = 3
        private val UNICODE_COOL_EMOJI = 0x1F60E
        private val UNICODE_TV_EMOJI = 0x1F4FA
        private val UNICODE_PARTY_EMOJI = 0x1F389
    }
}