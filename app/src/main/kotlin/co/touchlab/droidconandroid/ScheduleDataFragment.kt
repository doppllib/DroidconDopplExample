package co.touchlab.droidconandroid

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.droidconandroid.shared.data.AppPrefs
import co.touchlab.droidconandroid.shared.data.DatabaseHelper
import co.touchlab.droidconandroid.shared.data.Event
import co.touchlab.droidconandroid.shared.data.Track
import co.touchlab.droidconandroid.shared.presenter.*
import co.touchlab.droidconandroid.shared.interactors.UpdateAlertsInteractor
import co.touchlab.droidconandroid.ui.EventAdapter
import co.touchlab.droidconandroid.ui.EventClickListener
import kotlinx.android.synthetic.main.fragment_schedule_data.*
import java.util.*

class ScheduleDataFragment : Fragment(), ConferenceDataHost {

    private val viewModel: ScheduleDataViewModel by lazy {
        val interactor = (activity as ScheduleActivity).interactor
        val factory = ScheduleDataViewModel.Factory(interactor)
        ViewModelProviders.of(this, factory)[ScheduleDataViewModel::class.java]
    }

    val helper: DatabaseHelper by lazy {
        DatabaseHelper.getInstance(activity)
    }

    val updateAlertsInteractor: UpdateAlertsInteractor by lazy {
        val prefs = AppPrefs.getInstance(activity)
        UpdateAlertsInteractor(helper, prefs)
    }

    val RecyclerView.eventAdapter: EventAdapter
        get() = adapter as EventAdapter

    val shouldShowNotif: Boolean
        get() {
            return AppPrefs.getInstance(context).showNotifCard
                    && !arguments.getBoolean(ALL_EVENTS, true)
                    && arguments.getInt(POSITION, 0) == 0
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule_data, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (resources.getBoolean(R.bool.is_tablet)) {
            eventList.layoutManager = GridLayoutManager(activity, TABLET_COLUMNS)
        } else {
            eventList.layoutManager = LinearLayoutManager(activity)
        }

        eventList.adapter = EventAdapter(activity,
                arguments.getBoolean(ALL_EVENTS, true),
                ScheduleEventClickListener(),
                shouldShowNotif)
    }

    override fun onResume() {
        super.onResume()
        viewModel.register(this)
        viewModel.refreshData()
    }

    override fun onPause() {
        super.onPause()
        viewModel.unregister()
    }

    private fun updateAdapter(data: Array<out HourBlock>) {
        eventList.eventAdapter.updateEvents(data.asList())
    }

    fun updateNotifCard() {
        eventList.eventAdapter.updateNotificationCard(shouldShowNotif)
    }

    private inner class ScheduleEventClickListener : EventClickListener {
        override fun onEventClick(event: Event) {
            EventDetailActivity.callMe(activity, event.id, event.category)
        }
    }

    override fun loadCallback(daySchedules: Array<DaySchedule>) {
        val dayString = ConferenceDataHelper.dateToDayString(Date(arguments.getLong(DAY)))
        for (holder in daySchedules) {
            if (holder.dayString?.equals(dayString) ?: false) {
                updateAdapter(holder.hourHolders)
                break
            }
        }
        updateAlertsInteractor.alert()
    }

    companion object {
        private val ALL_EVENTS = "ALL_EVENTS"
        private val DAY = "DAY"
        private val POSITION = "POSITION"
        private val TABLET_COLUMNS = 2

        fun newInstance(all: Boolean, day: Long, position: Int): ScheduleDataFragment {
            val scheduleDataFragment = ScheduleDataFragment()
            val args = Bundle()
            args.putBoolean(ALL_EVENTS, all)
            args.putLong(DAY, day)
            args.putInt(POSITION, position)
            scheduleDataFragment.arguments = args
            return scheduleDataFragment
        }
    }
}