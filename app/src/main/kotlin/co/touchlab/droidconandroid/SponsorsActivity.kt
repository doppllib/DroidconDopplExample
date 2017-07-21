package co.touchlab.droidconandroid

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import co.touchlab.droidconandroid.ScheduleActivity.Companion.ALPHA_OPAQUE
import co.touchlab.droidconandroid.ui.SponsorFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_sponsors.*

class SponsorsActivity : AppCompatActivity() {
    companion object {
        fun callMe(activity: Activity) {
            val intent = Intent(activity, SponsorsActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sponsors)

        initToolbar()
        initTabs()
        initPager()
    }

    private fun initToolbar() {
        // set action bar
        sponsors_toolbar.title = ""
        setSupportActionBar(sponsors_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // set colors
        sponsors_toolbar_title.setTextColor(ContextCompat.getColor(this, R.color.tab_text_dark))
        val menuIconDark = sponsors_toolbar.navigationIcon?.mutate()
        menuIconDark?.mutate()?.setColorFilter(ContextCompat.getColor(this, R.color.tab_text_dark), PorterDuff.Mode.SRC_IN)
        menuIconDark?.alpha = ALPHA_OPAQUE
        sponsors_toolbar.navigationIcon = menuIconDark

        // set color of bg image
        sponsors_toolbar_backdrop.setColorFilter(ContextCompat.getColor(this, R.color.glyph_foreground_dark))

        // Adjust toolbar alpha on scroll
        sponsors_appbar.setExpanded(true)
        sponsors_appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange > 0) {
                val percentage = verticalOffset.calculateAlphaPercentage(appBarLayout.totalScrollRange)
                sponsors_toolbar.alpha = percentage
            }
        }
    }

    private fun initTabs() {
        // Add General and Party tabs - type will be distinguished in Sponsors Task
        sponsors_tabs.addTab(sponsors_tabs.newTab().setTag(getString(R.string.sponsors_tab_general)))
        sponsors_tabs.addTab(sponsors_tabs.newTab().setTag(getString(R.string.sponsors_tab_party)))

        // Style tab text and indicator color
        sponsors_tabs.setTabTextColors(ContextCompat.getColor(this, R.color.tab_inactive_text_dark), ContextCompat.getColor(this, R.color.tab_text_dark))
        sponsors_tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tab_accent_dark))
    }

    private fun initPager() {
        sponsors_view_pager.adapter = SponsorFragmentPagerAdapter(this@SponsorsActivity, supportFragmentManager)
        sponsors_view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(sponsors_tabs))
        sponsors_view_pager.setPageMarginDrawable(ContextCompat.getDrawable(this@SponsorsActivity, R.drawable.div_empty_16dp))
        sponsors_view_pager.pageMargin = resources.getDimensionPixelSize(R.dimen.pager_margin)
        sponsors_view_pager.offscreenPageLimit = 2
        sponsors_tabs.setupWithViewPager(sponsors_view_pager)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}