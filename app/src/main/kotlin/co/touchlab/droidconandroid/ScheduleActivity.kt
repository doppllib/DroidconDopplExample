package co.touchlab.droidconandroid

import android.app.NotificationManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
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
import co.touchlab.droidconandroid.alerts.NotificationService
import co.touchlab.droidconandroid.alerts.NotificationUtils
import co.touchlab.droidconandroid.shared.data.AppPrefs
import co.touchlab.droidconandroid.shared.interactors.RefreshScheduleInteractor
import co.touchlab.droidconandroid.shared.interactors.UpdateAlertsInteractor
import co.touchlab.droidconandroid.shared.presenter.AppManager
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataViewModel
import co.touchlab.droidconandroid.shared.utils.EventBusExt
import co.touchlab.droidconandroid.shared.utils.TimeUtils
import co.touchlab.droidconandroid.ui.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_schedule.*
import java.util.*

class ScheduleActivity : AppCompatActivity() {

    private var allEvents = true
    private val viewModel: ConferenceDataViewModel by lazy {
        val factory = ConferenceDataViewModel.Factory(allEvents)
        AppManager.getInstance().appComponent.inject(factory)
        ViewModelProviders.of(this, factory)[ConferenceDataViewModel::class.java]
    }

    val updateAlertsInteractor: UpdateAlertsInteractor by lazy {
        AppManager.getInstance().appComponent.updateAlertsInteractor()
    }

    val appPrefs: AppPrefs by lazy {
        AppManager.getInstance().appComponent.prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().subscribeToTopic(ALL_TOPIC)
        FirebaseMessaging.getInstance().subscribeToTopic(ANDROID_TOPIC)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val utils = NotificationUtils(this, notificationManager)
            utils.createChannels()
        }

        // Firebase Messaging adds data payload to launch activity if notification is sent while
        // app is in the background. Check for extras here:
        if (intent.extras != null && intent.extras[NotificationService.TYPE] != null) {
            val type = intent.extras[NotificationService.TYPE]

            when (type) {
                "updateSchedule" -> AppManager.getInstance().appComponent.refreshScheduleInteractor().refreshFromServer()
                "event" -> {
                    val eventId = intent.extras[NotificationService.EVENT_ID].toString().toLong()
                    val intent = EventDetailActivity.createIntent(this, eventId)
                    startActivity(intent)
                }
                "version" -> {
                    val packageInfo = packageManager.getPackageInfo(packageName, 0)
                    val versionCode = packageInfo.versionCode
                    val checkCode = intent.extras[NotificationService.VERSION_CODE].toString().toInt()
                    if (versionCode < checkCode) {
                        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName))

                        if (intent.resolveActivity(packageManager) == null) {
                            intent = Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName))
                        }
                        startActivity(intent)
                    }
                }
            }
        }

        when (goToScreen()) {
            AppManager.AppScreens.Welcome -> {
                startActivity(WelcomeActivity.getLaunchIntent(this@ScheduleActivity))
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

    private fun goToScreen(): AppManager.AppScreens {
        val hasSeenWelcome = appPrefs.hasSeenWelcome
        if (!hasSeenWelcome) {
            return AppManager.AppScreens.Welcome
        } else {
            return AppManager.AppScreens.Schedule
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
                schedule_toolbar_notif.alpha = percentage
            }
        }
        appbar.setExpanded(true)

        schedule_toolbar_notif.setOnClickListener {
            updateNotifications(!appPrefs.allowNotifications)
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
                        (view_pager.adapter as ScheduleFragmentPagerAdapter).switchToConference()
                        appbar.setExpanded(true)
                    }
                    R.string.my_schedule -> {
                        allEvents = false
                        (view_pager.adapter as ScheduleFragmentPagerAdapter).switchToAgenda()
                        appbar.setExpanded(true)
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
    }

    private fun getDrawerItems(): List<Any> {
        val drawerItems = ArrayList<Any>()
        drawerItems.add("header_placeholder")
        drawerItems.add(NavigationItem(R.string.explore, R.drawable.vic_event_black_24dp))
        drawerItems.add(NavigationItem(R.string.my_schedule, R.drawable.vic_clock_black_24dp))
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

        if (appPrefs.allowNotifications)
            schedule_toolbar_notif.setImageResource(R.drawable.vic_notifications_active_black_24dp)
        else
            schedule_toolbar_notif.setImageResource(R.drawable.vic_notifications_none_black_24dp)
    }

    private fun updateNotifications(allow: Boolean) {
        appPrefs.allowNotifications = allow
        appPrefs.showNotifCard = false
        (view_pager.adapter as ScheduleFragmentPagerAdapter).updateNotifCard()
        updateAlertsInteractor.alert()
        adjustToolBarAndDrawers()
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.is_tablet)
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    fun onEventMainThread(eventDetailJob: RefreshScheduleInteractor) {
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
            val startString: String? = appPrefs.conventionStartDate
            val endString: String? = appPrefs.conventionEndDate

            if (!startString.isNullOrBlank() && !endString.isNullOrBlank()) {
                var start: Long = TimeUtils.sanitize(TimeUtils.LOCAL_DATE_FORMAT.get().parse(startString))
                val end: Long = TimeUtils.sanitize(TimeUtils.LOCAL_DATE_FORMAT.get().parse(endString))

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
        private val ALL_TOPIC = "all"
        private val ANDROID_TOPIC = "android"

        @JvmField
        val ALPHA_OPAQUE = 255

        fun callMe(c: Context) {
            c.startActivity(Intent(c, ScheduleActivity::class.java))
        }
    }
}
