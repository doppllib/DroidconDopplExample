package co.touchlab.droidconandroid

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateUtils
import android.view.View
import co.touchlab.android.threading.eventbus.EventBusExt
import co.touchlab.android.threading.tasks.TaskQueue
import co.touchlab.droidconandroid.shared.data.AppPrefs
import co.touchlab.droidconandroid.shared.interactors.RefreshScheduleInteractor
import co.touchlab.droidconandroid.shared.presenter.*
import co.touchlab.droidconandroid.shared.tasks.UpdateAlertsTask
import co.touchlab.droidconandroid.shared.tasks.persisted.RefreshScheduleJob
import co.touchlab.droidconandroid.shared.tasks.UpdateAlertsInteractor
import co.touchlab.droidconandroid.shared.utils.TimeUtils
import co.touchlab.droidconandroid.ui.*
import kotlinx.android.synthetic.main.activity_schedule.*
import java.util.*

class ScheduleActivity : AppCompatActivity() {

    private var allEvents = true
    private lateinit var viewModel: ConferenceDataViewModel
    // temporary till we daggerize
    val interactor: RefreshScheduleInteractor by lazy { RefreshScheduleInteractor(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ConferenceDataViewModel.Factory(interactor, allEvents, AppPrefs.getInstance(this))
        viewModel = ViewModelProviders.of(this, factory)[ConferenceDataViewModel::class.java]

        when (AppManager.findStartScreen()) {
            AppManager.AppScreens.Welcome -> {
                startActivity(WelcomeActivity.getLaunchIntent(this@ScheduleActivity, false))
                finish()
                return
            }
            else -> {
                if (savedInstanceState != null) {
                    allEvents = savedInstanceState.getBoolean(ALL_EVENTS)
                }

                setContentView(R.layout.activity_schedule)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setupToolbar()
        setupNavigationDrawer()
        adjustToolBarAndDrawers()

        EventBusExt.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()

        Handler().post(RefreshRunnable())

        // will refresh data from server only if it is old
        viewModel.refreshFromServer()

        if (isTablet()) {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, drawer_recycler)
        } else {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, drawer_recycler)
            drawer_layout.closeDrawer(drawer_recycler)
        }
    }

    override fun onBackPressed() {
        when {
            !isTablet() && drawer_layout.isDrawerOpen(drawer_recycler) ->
                drawer_layout.closeDrawer(drawer_recycler)
            else -> super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(ALL_EVENTS, allEvents)
    }

    override fun onStop() {
        EventBusExt.getDefault().unregister(this)
        super.onStop()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(!isTablet())
        supportActionBar?.setHomeButtonEnabled(!isTablet())

        schedule_backdrop.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.superglyph_outline360x114dp))

        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange > 0) {
                val percentage = verticalOffset.calculateAlphaPercentage(appBarLayout.totalScrollRange)
                schedule_toolbar_title.alpha = percentage
                schedule_toolbar_profile.alpha = percentage
                schedule_toolbar_notif.alpha = percentage
                schedule_placeholder_emoji.alpha = percentage
            }
        }
        appbar.setExpanded(true)

        schedule_profile_touch.setOnClickListener {}

        schedule_toolbar_notif.setOnClickListener {
            val prefs = AppPrefs.getInstance(this)
            updateNotifications(!prefs.allowNotifications)
        }
    }

    private fun setupNavigationDrawer() {

        if (isTablet()) {
            drawer_layout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent))
        } else {
            val drawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
            drawer_layout.addDrawerListener(drawerToggle)
            drawerToggle.syncState()
        }

        drawer_recycler.adapter = DrawerAdapter(this, getDrawerItems(), object : DrawerClickListener {
            override fun onNavigationItemClick(position: Int, titleRes: Int) {
                if (!isTablet()) drawer_layout.closeDrawer(drawer_recycler)

                when (titleRes) {
                    R.string.explore -> {
                        allEvents = true
                        appbar.setExpanded(true)
                    }
                    R.string.my_schedule -> {
                        allEvents = false
                        appbar.setExpanded(true)
                    }
                    R.string.chat_on_slack -> {
                        SlackHelper.openSlack(this@ScheduleActivity, viewModel.slackLink,
                                viewModel.slackLinkHttp,
                                viewModel.shouldShowSlackDialog())
                    }

                    R.string.about -> AboutActivity.callMe(this@ScheduleActivity)
                    R.string.sponsors -> SponsorsActivity.callMe(this@ScheduleActivity)
                }

                Handler().post(RefreshRunnable())
                (drawer_recycler.adapter as DrawerAdapter).setSelectedPosition(position)
                adjustToolBarAndDrawers()
            }

            override fun onHeaderItemClick() {}
        })
        drawer_recycler.layoutManager = LinearLayoutManager(this)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, filter_wrapper)
    }

    private fun getDrawerItems(): List<Any> {
        val drawerItems = ArrayList<Any>()
        drawerItems.add("header_placeholder")
        drawerItems.add(NavigationItem(R.string.explore, R.drawable.vic_event_black_24dp))
        drawerItems.add(NavigationItem(R.string.my_schedule, R.drawable.vic_clock_black_24dp))
        drawerItems.add(NavigationItem(R.string.chat_on_slack, R.drawable.vic_slack_24dp, true))
        drawerItems.add("divider_placeholder")
        drawerItems.add(NavigationItem(R.string.sponsors, R.drawable.vic_star_circle))
        drawerItems.add(NavigationItem(R.string.about, R.drawable.vic_info_outline_black_24dp))
        return drawerItems
    }

    private fun adjustToolBarAndDrawers() {
        if (allEvents) {
            (drawer_recycler.adapter as DrawerAdapter).setSelectedPosition(POSITION_EXPLORE)
            schedule_toolbar_title.setText(R.string.app_name)
            schedule_backdrop.setColorFilter(ContextCompat.getColor(this, R.color.glyph_foreground_dark))
            schedule_backdrop.setBackgroundColor(ContextCompat.getColor(this, R.color.glyph_background_dark))
            schedule_toolbar_title.setTextColor(ContextCompat.getColor(this, R.color.tab_text_dark))
            tabs.setTabTextColors(ContextCompat.getColor(this, R.color.tab_inactive_text_dark), ContextCompat.getColor(this, R.color.tab_text_dark))
            tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tab_accent_dark))
            val menuIconDark = toolbar.navigationIcon?.mutate()
            menuIconDark?.mutate()?.setColorFilter(ContextCompat.getColor(this, R.color.tab_text_dark), PorterDuff.Mode.SRC_IN)
            menuIconDark?.alpha = ALPHA_OPAQUE
            toolbar.navigationIcon = menuIconDark

            schedule_toolbar_notif.visibility = View.GONE
        } else {
            (drawer_recycler.adapter as DrawerAdapter).setSelectedPosition(POSITION_MY_SCHEDULE)
            schedule_toolbar_title.setText(R.string.my_schedule)
            schedule_backdrop.setColorFilter(ContextCompat.getColor(this, R.color.glyph_foreground_light))
            schedule_backdrop.setBackgroundColor(ContextCompat.getColor(this, R.color.glyph_background_light))
            schedule_toolbar_title.setTextColor(ContextCompat.getColor(this, R.color.tab_text_light))
            tabs.setTabTextColors(ContextCompat.getColor(this, R.color.tab_inactive_text_light), ContextCompat.getColor(this, R.color.tab_text_light))
            tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tab_accent_light))
            val menuIconLight = toolbar.navigationIcon?.mutate()
            menuIconLight?.setColorFilter(ContextCompat.getColor(this, R.color.tab_text_light), PorterDuff.Mode.SRC_IN)
            menuIconLight?.alpha = ALPHA_OPAQUE
            toolbar.navigationIcon = menuIconLight

            schedule_toolbar_notif.visibility = View.VISIBLE
        }

        if (AppPrefs.getInstance(this).allowNotifications)
            schedule_toolbar_notif.setImageResource(R.drawable.vic_notifications_active_black_24dp)
        else
            schedule_toolbar_notif.setImageResource(R.drawable.vic_notifications_none_black_24dp)
    }

    private fun updateNotifications(allow: Boolean) {
        val prefs = AppPrefs.getInstance(this)
        prefs.allowNotifications = allow
        prefs.showNotifCard = false
        (view_pager.adapter as ScheduleFragmentPagerAdapter).updateNotifCard()
        TaskQueue.loadQueueDefault(this).execute(UpdateAlertsInteractor())
        adjustToolBarAndDrawers()
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.is_tablet)
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    fun onEventMainThread(eventDetailJob: RefreshScheduleJob) {
        Handler().post(RefreshRunnable())
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    fun onEventMainThread(notificationEvent: UpdateAllowNotificationEvent) {
        //Have to handle the notification card way out here so it can update both fragments.
        //Set the app prefs and bounce it back down to the adapter
        updateNotifications(notificationEvent.allow)
    }

    inner class RefreshRunnable : Runnable {
        override fun run() {
            val dates: ArrayList<Long> = ArrayList()
            val startString: String? = AppPrefs.getInstance(this@ScheduleActivity).conventionStartDate
            val endString: String? = AppPrefs.getInstance(this@ScheduleActivity).conventionEndDate

            if (!startString.isNullOrBlank() && !endString.isNullOrBlank()) {
                var start: Long = TimeUtils.sanitize(TimeUtils.DATE_FORMAT.get().parse(startString))
                val end: Long = TimeUtils.sanitize(TimeUtils.DATE_FORMAT.get().parse(endString))

                while (start <= end) {
                    dates.add(start)
                    start += DateUtils.DAY_IN_MILLIS
                }

                if (view_pager.adapter == null) {
                    view_pager.adapter = ScheduleFragmentPagerAdapter(
                            supportFragmentManager,
                            dates,
                            allEvents)
                    view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
                    tabs.setupWithViewPager(view_pager)
                }
            }
        }
    }

    companion object {
        private val POSITION_EXPLORE = 1
        private val POSITION_MY_SCHEDULE = 2
        private val ALL_EVENTS = "all_events"
        @JvmField
        val ALPHA_OPAQUE = 255

        fun callMe(c: Context) {
            c.startActivity(Intent(c, ScheduleActivity::class.java))
        }
    }
}
