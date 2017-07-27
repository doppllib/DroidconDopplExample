package co.touchlab.droidconandroid.ui

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.droidconandroid.R
import co.touchlab.droidconandroid.isOdd
import co.touchlab.droidconandroid.setViewVisibility
import co.touchlab.droidconandroid.shared.data.Block
import co.touchlab.droidconandroid.shared.data.Event
import co.touchlab.droidconandroid.shared.presenter.HourBlock
import co.touchlab.droidconandroid.shared.utils.EventBusExt
import co.touchlab.droidconandroid.shared.utils.EventUtils
import kotlinx.android.synthetic.main.item_event.view.*
import kotlinx.android.synthetic.main.item_notification.view.*
import java.util.*

/**
 *
 * Created by izzyoji :) on 8/6/15.
 */

class EventAdapter(private val context: Context,
                   private val allEvents: Boolean,
                   private val eventClickListener: EventClickListener,
                   private var showNotificationCard: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataSet: List<HourBlock> = emptyList()
    private var filteredData: ArrayList<HourBlock?> = ArrayList()

    override fun getItemCount(): Int {
        return filteredData.size + adjustNotification
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EVENT -> {
                val v = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)
                ScheduleBlockViewHolder(v)
            }
            VIEW_TYPE_PAST_EVENT, VIEW_TYPE_BLOCK -> {
                val v = LayoutInflater.from(context).inflate(R.layout.item_block, parent, false)
                ScheduleBlockViewHolder(v)
            }
            VIEW_TYPE_NOTIFICATION -> {
                val v = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
                NotificationViewHolder(v)
            }
            VIEW_TYPE_NEW_ROW -> {
                val v = LayoutInflater.from(context).inflate(R.layout.item_new_row, parent, false)
                NewRowViewHolder(v)
            }
            else -> throw UnsupportedOperationException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ScheduleBlockViewHolder) {
            val adjustedPosition = position - adjustNotification
            val scheduleBlockHour = filteredData[adjustedPosition]

            scheduleBlockHour?.let {
                EventUtils.styleEventRow(scheduleBlockHour, dataSet, holder, allEvents)

                if (!scheduleBlockHour.timeBlock.isBlock) {
                    holder.setOnClickListener { eventClickListener.onEventClick(scheduleBlockHour.timeBlock as Event) }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        //Position 0 is always the notification
        if (position == 0 && showNotificationCard)
            return VIEW_TYPE_NOTIFICATION

        val adjustedPosition = position - adjustNotification
        if (filteredData[adjustedPosition] == null) {
            return VIEW_TYPE_NEW_ROW
        }

        val item = filteredData[adjustedPosition]?.timeBlock
        when (item) {
            is Event -> return if (item.isPast) VIEW_TYPE_PAST_EVENT else VIEW_TYPE_EVENT
            is Block -> return VIEW_TYPE_BLOCK
            else -> throw UnsupportedOperationException()
        }
    }

    private fun updateData() {
        filteredData.clear()
            for (item in dataSet) {
                val position = filteredData.size + adjustNotification
                if (item.hourStringDisplay.isNotBlank() && position.isOdd()) {
                    // Insert an empty block to indicate a new row
                    filteredData.add(null)
                }
                filteredData.add(item)
            }

        notifyDataSetChanged()
    }

    fun updateEvents(data: List<HourBlock>) {
        dataSet = data
        updateData()
    }

    fun updateNotificationCard(show: Boolean) {
        if (show == showNotificationCard) return

        showNotificationCard = show
        if (show)
            notifyItemInserted(0)
        else if (itemCount > 0)
            notifyItemRemoved(0)
    }

    private val adjustNotification: Int
        get() = if (showNotificationCard) HEADER_ITEMS_COUNT else 0

    inner abstract class ScheduleCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ScheduleBlockViewHolder(itemView: View) : ScheduleCardViewHolder(itemView), EventUtils.EventRow {

        override fun setTitleText(s: String?) { itemView.title.text = s }

        override fun setTimeText(s: String?) { itemView.time.text = s }

        override fun setSpeakerText(s: String?) {
            itemView.speaker.text = s
        }

        override fun setDescription(s: String?) {
            // Field only exists for tablet
            itemView.event_description?.text = s
        }

        override fun setRsvpVisible(rsvp: Boolean, past: Boolean) {
            val rsvpColor = if (past) ContextCompat.getColor(itemView.context, R.color.card_text_subtitle)
            else ContextCompat.getColor(itemView.context, R.color.accent)
            itemView.rsvp.setBackgroundColor(rsvpColor)
            itemView.rsvp.setViewVisibility(rsvp)
        }

        override fun setRsvpConflict(hasConflict: Boolean) {
            itemView.conflict_text.setViewVisibility(hasConflict)
            if (hasConflict)
                itemView.rsvp.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.red))
        }

        override fun setLiveNowVisible(liveNow: Boolean) {
            itemView.live.setViewVisibility(liveNow)
        }

        override fun setTimeGap(gap: Boolean) {
            if (!isTablet(itemView)) {
                val topPadding = if (gap) R.dimen.padding_small else R.dimen.padding_xmicro
                itemView.setPadding(itemView.paddingLeft,
                        itemView.context.resources.getDimensionPixelOffset(topPadding),
                        itemView.paddingRight,
                        itemView.paddingBottom)
            }
        }

        fun setOnClickListener(listener: () -> Unit) {
            itemView.card.setOnClickListener { listener() }
        }

        fun isTablet(itemView: View): Boolean {
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

    inner class NewRowViewHolder(itemView: View) : ScheduleCardViewHolder(itemView)

    companion object {
        private val VIEW_TYPE_EVENT = 0
        private val VIEW_TYPE_BLOCK = 1
        private val VIEW_TYPE_PAST_EVENT = 2
        private val VIEW_TYPE_NOTIFICATION = 3
        private val VIEW_TYPE_NEW_ROW = 4
        private val HEADER_ITEMS_COUNT = 1
    }
}

interface EventClickListener {
    fun onEventClick(event: Event)
}

data class UpdateAllowNotificationEvent(val allow: Boolean)

