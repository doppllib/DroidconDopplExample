package co.touchlab.droidconandroid

import android.app.NotificationManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import co.touchlab.droidconandroid.alerts.NotificationService
import co.touchlab.droidconandroid.alerts.NotificationUtils
import co.touchlab.droidconandroid.shared.data.AppPrefs
import co.touchlab.droidconandroid.shared.viewmodel.AppManager
import co.touchlab.droidconandroid.shared.viewmodel.ConferenceDataViewModel
import co.touchlab.droidconandroid.ui.*
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_schedule.*
import java.util.*

class ScheduleActivity : AppCompatActivity(), ConferenceDataViewModel.Host {
    override fun updateConferenceDates(dates: List<Long>) {
        if (view_pager.adapter == null) {
            view_pager.adapter = ScheduleFragmentPagerAdapter(
                    supportFragmentManager,
                    dates,
                    allEvents)
            view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
            tabs.setupWithViewPager(view_pager)
        }
    }

    private var allEvents = true
    private var selectedDrawerTitleRes = -1
    private var selectedDrawerPosition = -1
    private val cd = CompositeDisposable()
  
    private val viewModel: ConferenceDataViewModel by lazy {
        ViewModelProviders.of(this, ConferenceDataViewModel.factory(allEvents))[ConferenceDataViewModel::class.java]
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

        // Firebase Messaging console adds data payload to launch activity if notification is sent
        // while app is in the background. Check for extras here:
        if (intent.extras != null && intent.extras[NotificationService.EVENT_ID] != null) {
            val eventId = intent.extras[NotificationService.EVENT_ID].toString().toLong()
            val intent = EventDetailActivity.createIntent(this, eventId)
            startActivity(intent)
        }

        when (viewModel.goToScreen()) {
            ConferenceDataViewModel.AppScreens.Welcome -> {
                startActivity(WelcomeActivity.getLaunchIntent(this@ScheduleActivity))
                finish()
                return
            }
            else -> {
                if (savedInstanceState != null) {
                    allEvents = savedInstanceState.getBoolean(ALL_EVENTS)
                }

                setContentView(R.layout.activity_schedule)

                schedule_toolbar_notif.visibility = View.GONE

                viewModel.wire(this)

                cd.add(appPrefs.observeAllowNotifications().subscribe { b: Boolean ->
                    adjustToolBarAndDrawers()
                })
            }
        }

        if (savedInstanceState != null) {
            selectedDrawerPosition = savedInstanceState.getInt(SELECTED_DRAWER_POSITION, -1)
            selectedDrawerTitleRes = savedInstanceState.getInt(SELECTED_DRAWER_TITLE_RES, -1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unwire()
        cd.clear()
    }

    override fun onStart() {
        super.onStart()
        setupToolbar()
        setupNavigationDrawer()
    }

    override fun onResume() {
        super.onResume()

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
        outState.putInt(SELECTED_DRAWER_POSITION, selectedDrawerPosition)
        outState.putInt(SELECTED_DRAWER_TITLE_RES, selectedDrawerTitleRes)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(!isTablet())
        supportActionBar?.setHomeButtonEnabled(!isTablet())

        schedule_backdrop.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.bannerback))

        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange > 0) {
                val percentage = verticalOffset.calculateAlphaPercentage(appBarLayout.totalScrollRange)
                schedule_toolbar_title.alpha = percentage
                schedule_toolbar_notif.alpha = percentage
            }
        }
        appbar.setExpanded(true)

        schedule_toolbar_notif.setOnClickListener {
            appPrefs.setAllowNotifications(!appPrefs.allowNotificationsUi)
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
                selectNavigationItem(titleRes, position)
            }

            override fun onHeaderItemClick() {}
        })
        drawer_recycler.layoutManager = LinearLayoutManager(this)
        if (selectedDrawerPosition >= 0) {
            selectNavigationItem(selectedDrawerTitleRes, selectedDrawerPosition)
        }
    }

    private fun selectNavigationItem(titleRes: Int, position: Int) {
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

        selectedDrawerTitleRes = titleRes
        selectedDrawerPosition = position
        (drawer_recycler.adapter as DrawerAdapter).setSelectedPosition(position)
        adjustToolBarAndDrawers()
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
            if(drawer_recycler.adapter != null)
            {
                (drawer_recycler.adapter as DrawerAdapter).setSelectedPosition(POSITION_EXPLORE)
            }
            schedule_toolbar_title.setText(R.string.app_name)
            schedule_toolbar_notif.visibility = View.GONE
        } else {
            if(drawer_recycler.adapter != null) {

                (drawer_recycler.adapter as DrawerAdapter).setSelectedPosition(POSITION_MY_SCHEDULE)
            }
            schedule_toolbar_title.setText(R.string.my_schedule)
            schedule_toolbar_notif.visibility = View.VISIBLE
        }

        schedule_toolbar_title.setTextColor(ContextCompat.getColor(this, R.color.tab_text_dark))
        tabs.setTabTextColors(ContextCompat.getColor(this, R.color.tab_inactive_text_dark), ContextCompat.getColor(this, R.color.tab_text_dark))
        tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tab_accent_dark))
        val menuIconDark = toolbar.navigationIcon?.mutate()
        menuIconDark?.mutate()?.setColorFilter(ContextCompat.getColor(this, R.color.tab_text_dark), PorterDuff.Mode.SRC_IN)
        menuIconDark?.alpha = ALPHA_OPAQUE
        toolbar.navigationIcon = menuIconDark

        if (appPrefs.allowNotificationsUi)
            schedule_toolbar_notif.setImageResource(R.drawable.vic_notifications_active_black_24dp)
        else
            schedule_toolbar_notif.setImageResource(R.drawable.vic_notifications_none_black_24dp)
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.is_tablet)
    }

    companion object {
        private val POSITION_EXPLORE = 1
        private val POSITION_MY_SCHEDULE = 2
        private val ALL_EVENTS = "all_events"
        private val ALL_TOPIC = "all_2017"
        private val ANDROID_TOPIC = "android_2017"
        private val SELECTED_DRAWER_POSITION = "selected_drawer_position"
        private val SELECTED_DRAWER_TITLE_RES = "selected_drawer_title_res"

        @JvmField
        val ALPHA_OPAQUE = 255

        fun callMe(c: Context) {
            c.startActivity(Intent(c, ScheduleActivity::class.java))
        }
    }
}
