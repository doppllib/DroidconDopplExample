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
import co.touchlab.droidconandroid.shared.data.Event
import co.touchlab.droidconandroid.shared.interactors.UpdateAlertsInteractor
import co.touchlab.droidconandroid.shared.presenter.*
import co.touchlab.droidconandroid.ui.EventAdapter
import co.touchlab.droidconandroid.ui.EventClickListener
import kotlinx.android.synthetic.main.fragment_schedule_data.*
import java.util.*

class ScheduleDataFragment : Fragment(), ScheduleDataViewModel.Host {

    private val viewModel: ScheduleDataViewModel by lazy {
        ViewModelProviders.of(this, ScheduleDataViewModel.factory())[ScheduleDataViewModel::class.java]
    }

    val updateAlertsInteractor: UpdateAlertsInteractor by lazy {
        AppManager.getInstance().appComponent.updateAlertsInteractor()
    }

    val RecyclerView.eventAdapter: EventAdapter
        get() = adapter as EventAdapter

    val shouldShowNotif: Boolean
        get() {
            return AppManager.getInstance().appComponent.prefs.showNotifCard
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
        viewModel.register(this, true)
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

    fun switchToAgenda() {
        viewModel.register(this, false)
    }

    fun switchToConference() {
        viewModel.register(this, true)
    }

    private inner class ScheduleEventClickListener : EventClickListener {
        override fun onEventClick(event: Event) {
            EventDetailActivity.callMe(activity, event.id, event.category)
        }
    }

    override fun loadCallback(daySchedule: Array<DaySchedule>) {
        val dayString = ConferenceDataHelper.dateToDayString(Date(arguments.getLong(DAY)))

        for (daySchedule in daySchedule) {
            if(daySchedule.dayString?.equals(dayString) ?: false)
            {
                updateAdapter(daySchedule.hourHolders)
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