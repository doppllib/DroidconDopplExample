package co.touchlab.droidconandroid

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.droidconandroid.shared.data.Event
import co.touchlab.droidconandroid.shared.viewmodel.*
import co.touchlab.droidconandroid.ui.EventAdapter
import co.touchlab.droidconandroid.ui.EventClickListener
import kotlinx.android.synthetic.main.fragment_schedule_data.*
import java.util.*

class ScheduleDataFragment : Fragment(), ScheduleDataViewModel.Host {

    private val viewModel: ScheduleDataViewModel by lazy {
        ViewModelProviders.of(this, ScheduleDataViewModel.factory())[ScheduleDataViewModel::class.java]
    }

    private var allEvents = true

    val RecyclerView.eventAdapter: EventAdapter
        get() = adapter as EventAdapter

    val shouldShowNotif: Boolean
        get() {
            return AppManager.getInstance().appComponent.prefs.showNotifCard
//                    && !allEvents
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

        allEvents = arguments.getBoolean(ALL_EVENTS, true)
        eventList.adapter = EventAdapter(activity,
                allEvents,
                ScheduleEventClickListener(),
                shouldShowNotif,
                AppManager.getInstance().appComponent.prefs)
    }

    override fun onResume() {
        super.onResume()
        viewModel.wire(this, allEvents)
    }

    override fun onPause() {
        super.onPause()
        arguments.putBoolean(ALL_EVENTS, allEvents)
        viewModel.unwire()
    }

    private fun updateAdapter(data: Array<out HourBlock>) {
        eventList.eventAdapter.updateEvents(data.asList())
    }

    fun switchToAgenda() {
        allEvents = false
        viewModel.wire(this, allEvents)
    }

    fun switchToConference() {
        allEvents = true
        viewModel.wire(this, allEvents)
    }

    private inner class ScheduleEventClickListener : EventClickListener {
        override fun onEventClick(event: Event) {
            EventDetailActivity.callMe(activity, event.id, event.category)
        }
    }

    override fun loadCallback(dayScheduleArray: Array<DaySchedule>) {
        Log.w("ScheduleDataFragment", "loadCallback: "+ dayScheduleArray.size)
        val dayString = ConferenceDataHelper.dateToDayString(Date(arguments.getLong(DAY)))

        for (daySchedule in dayScheduleArray) {
            if(daySchedule.dayString?.equals(dayString) ?: false)
            {
                updateAdapter(daySchedule.hourHolders)
            }
        }
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