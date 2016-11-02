package co.touchlab.droidconandroid.ui

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.android.threading.eventbus.EventBusExt
import co.touchlab.droidconandroid.R
import co.touchlab.droidconandroid.data.Block
import co.touchlab.droidconandroid.data.Event
import co.touchlab.droidconandroid.data.Track
import co.touchlab.droidconandroid.presenter.ConferenceDataPresenter
import co.touchlab.droidconandroid.presenter.ScheduleBlockHour
import kotlinx.android.synthetic.main.item_event.view.*
import kotlinx.android.synthetic.main.item_notification.view.*
import java.util.*

/**
 *
 * Created by izzyoji :) on 8/6/15.
 */

private const val VIEW_TYPE_EVENT = 0
private const val VIEW_TYPE_BLOCK = 1
private const val VIEW_TYPE_PAST_EVENT = 2
private const val VIEW_TYPE_NOTIFICATION = 3
private const val VIEW_TYPE_NEW_ROW = 4

private const val HEADER_ITEMS_COUNT = 1

class EventAdapter(private val allEvents: Boolean
                   , initialFilters: List<String>
                   , private val eventClickListener: EventClickListener
                   , var showNotificationCard: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataSet: List<ScheduleBlockHour> = emptyList()
    private var filteredData: ArrayList<ScheduleBlockHour?> = ArrayList()
    private var currentTracks: ArrayList<String> = ArrayList(initialFilters)

    override fun getItemCount(): Int {
        return filteredData.size + if (showNotificationCard) HEADER_ITEMS_COUNT else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_EVENT -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
                return ScheduleBlockViewHolder(v)
            }
            VIEW_TYPE_PAST_EVENT, VIEW_TYPE_BLOCK -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_block, parent, false)
                return ScheduleBlockViewHolder(v)
            }
            VIEW_TYPE_NOTIFICATION -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
                return NotificationViewHolder(v)
            }
            VIEW_TYPE_NEW_ROW -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_new_row, parent, false)
                return NewRowViewHolder(v)
            }

            else -> throw UnsupportedOperationException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val adjustedPosition = position - if (showNotificationCard) HEADER_ITEMS_COUNT else 0
        if (holder is ScheduleBlockViewHolder) {
            val scheduleBlockHour = filteredData[adjustedPosition]

            if (scheduleBlockHour != null) {
                ConferenceDataPresenter.styleEventRow(scheduleBlockHour, dataSet, holder, allEvents)

                if (!scheduleBlockHour.scheduleBlock.isBlock) {
                    holder.setOnClickListener { eventClickListener.onEventClick(scheduleBlockHour.scheduleBlock as Event) }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        //Position 0 is always the notification
        if (position == 0 && showNotificationCard) return VIEW_TYPE_NOTIFICATION

        val adjustedPosition = position - if (showNotificationCard) HEADER_ITEMS_COUNT else 0

        if (filteredData[adjustedPosition] == null) {
            return VIEW_TYPE_NEW_ROW
        }

        val item = filteredData[adjustedPosition]?.scheduleBlock
        when (item) {
            is Event -> return if (item.isPast) VIEW_TYPE_PAST_EVENT else VIEW_TYPE_EVENT
            is Block -> return VIEW_TYPE_BLOCK
            else -> throw UnsupportedOperationException()
        }
    }

    fun toggleTrackFilter(track: Track) {
        val trackServerName = track.serverName
        if (!currentTracks.contains(trackServerName)) {
            currentTracks.add(trackServerName)
        } else {
            currentTracks.remove(trackServerName)
        }
        updateData()
    }

    private fun updateData() {
        filteredData.clear()
        if (currentTracks.isEmpty()) {
            for (item in dataSet) {
                val position = filteredData.size + if (showNotificationCard) HEADER_ITEMS_COUNT else 0
                if (!item.hourStringDisplay.isEmpty() && position % 2 != 0) {
                    // Insert an empty block to indicate a new row
                    filteredData.add(null)
                }
                filteredData.add(item)
            }
        } else {
            //TODO: Filter
            /*for (item in dataSet) {
                if(item is Block) {
                    filteredData.add(item)
                } else {
                    val event = item as Event
                    val category = event.category
                    if (!TextUtils.isEmpty(category) && currentTracks.contains(category)) {
                        filteredData.add(item)
                    }
                }
            }*/
        }

        notifyDataSetChanged()
    }

    fun updateEvents(data: List<ScheduleBlockHour>) {
        dataSet = data
        updateData()
    }

    fun updateNotificationCard(show: Boolean) {
        if (show == showNotificationCard)
            return

        showNotificationCard = show
        if (show)
            notifyItemInserted(0)
        else if (itemCount > 0)
            notifyItemRemoved(0)
    }

    inner abstract class ScheduleCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    inner class ScheduleBlockViewHolder(itemView: View) : ScheduleCardViewHolder(itemView), ConferenceDataPresenter.EventRow {

        override fun setTitleText(s: String?) {
            itemView.title.text = s
        }

        override fun setTimeText(s: String?) {
            itemView.time.text = s
        }

        override fun setDetailText(s: String?) {
            itemView.location_time.text = s
        }

        override fun setDescription(s: String?) {
            // Field only exists for tablet
            itemView.event_description?.text = s
        }

        override fun setRsvpVisible(rsvp: Boolean, past: Boolean) {
            val rsvpColor = if (past) ContextCompat.getColor(itemView.context, R.color.card_text_subtitle) else ContextCompat.getColor(itemView.context, R.color.accent)
            itemView.rsvp.setBackgroundColor(rsvpColor)
            itemView.rsvp.visibility = if (rsvp) View.VISIBLE else View.INVISIBLE
        }

        override fun setRsvpConflict(b: Boolean) {
            itemView.conflict_text.visibility = if (b) View.VISIBLE else View.INVISIBLE
            if (b) itemView.rsvp.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.red))
        }

        override fun setLiveNowVisible(b : Boolean) {
            itemView.live.visibility = if (b) View.VISIBLE else View.INVISIBLE
        }

        override fun setTimeGap(b : Boolean) {
            if (!isTablet(itemView)) {
                val topPadding = if (b) R.dimen.padding_small else R.dimen.padding_xmicro
                itemView.setPadding(itemView.paddingLeft,
                        itemView.context.resources.getDimensionPixelOffset(topPadding),
                        itemView.paddingRight,
                        itemView.paddingBottom)
            }
        }

        fun setOnClickListener(listener: () -> Unit) {
            itemView.card.setOnClickListener { listener() }
        }

        fun isTablet(itemView : View) : Boolean {
            return itemView.context.resources.getBoolean(R.bool.is_tablet)
        }
    }

    inner class NotificationViewHolder(itemView: View) : ScheduleCardViewHolder(itemView) {
        init {
            itemView.notify_accept.setOnClickListener {
                EventBusExt.getDefault().post(UpdateAllowNotificationEvent(true))
            }
            itemView.notify_decline.setOnClickListener {
                EventBusExt.getDefault().post(UpdateAllowNotificationEvent(false))
            }
        }
    }

    inner class NewRowViewHolder(itemView: View) : ScheduleCardViewHolder(itemView) {}
}

interface EventClickListener {
    fun onEventClick(event: Event)
}

data class UpdateAllowNotificationEvent(val allow: Boolean)

