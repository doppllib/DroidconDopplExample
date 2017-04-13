package co.touchlab.droidconandroid

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.android.threading.eventbus.EventBusExt
import co.touchlab.droidconandroid.shared.data.AppPrefs
import co.touchlab.droidconandroid.shared.data.Event
import co.touchlab.droidconandroid.shared.data.Track
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper
import co.touchlab.droidconandroid.shared.presenter.ConferenceDayHolder
import co.touchlab.droidconandroid.shared.presenter.ScheduleBlockHour
import co.touchlab.droidconandroid.ui.EventAdapter
import co.touchlab.droidconandroid.ui.EventClickListener
import kotlinx.android.synthetic.main.fragment_schedule_data.*
import java.util.*

private const val ALL_EVENTS = "ALL_EVENTS"
private const val DAY = "DAY"
private const val POSITION = "POSITION"
private const val TABLET_COLUMNS = 2

fun createScheduleDataFragment(all: Boolean, day: Long, position: Int): ScheduleDataFragment {
    val scheduleDataFragment = ScheduleDataFragment()
    val args = Bundle()
    args.putBoolean(ALL_EVENTS, all)
    args.putLong(DAY, day)
    args.putInt(POSITION, position)
    scheduleDataFragment.arguments = args
    return scheduleDataFragment
}

class ScheduleDataFragment() : Fragment() {
    //Extension property for casting adapter
    val RecyclerView.eventAdapter: EventAdapter
        get() = adapter as EventAdapter

    val shouldShowNotif: Boolean
    get() {
        return AppPrefs.getInstance(context).showNotifCard
                && !arguments.getBoolean(ALL_EVENTS, true)
                && arguments.getInt(POSITION,0) == 0
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_schedule_data, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        if (resources.getBoolean(R.bool.is_tablet))
        {
            eventList.layoutManager = GridLayoutManager(activity, TABLET_COLUMNS)
        }
        else
        {
            eventList.layoutManager = LinearLayoutManager(activity)
        }

        eventList.adapter = EventAdapter( arguments.getBoolean(ALL_EVENTS, true)
                , emptyList()
                , ScheduleEventClickListener()
                , shouldShowNotif)
    }

    override fun onResume() {
        super.onResume()
        EventBusExt.getDefault()!!.register(this)
    }

    override fun onPause()
    {
        super.onPause()
        EventBusExt.getDefault()!!.unregister(this)
    }

    private fun updateAdapter(data: Array<out ScheduleBlockHour>) {
        eventList.eventAdapter.updateEvents(data.asList())
    }

    fun filter(track: Track) {
        eventList.eventAdapter.toggleTrackFilter(track)
    }

    fun  updateNotifCard() {
        eventList.eventAdapter.updateNotificationCard(shouldShowNotif)
    }

    @Suppress("unused")
    fun onEventMainThread(dayHolders: Array<ConferenceDayHolder>) {
        val dayString = ConferenceDataHelper.dateToDayString(Date(arguments.getLong(DAY)))
        for (holder in dayHolders) {
            if (holder.dayString?.equals(dayString) ?: false) {
                updateAdapter(holder.hourHolders)
                break
            }
        }
    }

    private inner class ScheduleEventClickListener : EventClickListener {
        override fun onEventClick(event: Event) {
            EventDetailActivity.callMe(activity, event.id, event.category)
        }
    }
}