package co.touchlab.droidconandroid.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import co.touchlab.droidconandroid.R
import co.touchlab.droidconandroid.Sponsor2WelcomeFragment
import co.touchlab.droidconandroid.SponsorWelcomeFragment
import co.touchlab.droidconandroid.WelcomeFragment

class WelcomePagerAdapter(fm: FragmentManager, val short: Boolean) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        if (short) {
            when (position) {
                0 -> return SponsorWelcomeFragment()
                1 -> return Sponsor2WelcomeFragment()
            }
            throw IllegalStateException("Too many fragments")
        } else {
            when (position) {
                0 -> return WelcomeFragment.newInstance(R.color.welcome_background_0, R.drawable.welcome_0, R.color.white, R.string.welcome_0_title, R.string.welcome_0_desc)
                1 -> return WelcomeFragment.newInstance(R.color.welcome_background_1, R.drawable.welcome_1, R.color.white, R.string.welcome_1_title, R.string.welcome_1_desc)
            }
            throw IllegalStateException("Too many fragments")
        }
    }

    override fun getCount(): Int {
        return if (short) SHORT_COUNT else FRAGMENT_COUNT
    }

    companion object {
        private val FRAGMENT_COUNT = 2
        private val SHORT_COUNT = 2
    }
}