package co.touchlab.droidconandroid.ui

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.droidconandroid.R
import co.touchlab.droidconandroid.isOdd
import co.touchlab.droidconandroid.setViewVisibility
import co.touchlab.droidconandroid.shared.data.AppPrefs
import co.touchlab.droidconandroid.shared.data.Block
import co.touchlab.droidconandroid.shared.data.Event
import co.touchlab.droidconandroid.shared.network.sessionize.SessionWithSpeakers
import co.touchlab.droidconandroid.shared.utils.EventUtils
import co.touchlab.droidconandroid.shared.viewmodel.HourBlock
import kotlinx.android.synthetic.main.item_event.view.*
import java.util.*

/**
 *
 * Created by izzyoji :) on 8/6/15.
 */

class EventAdapter(private val context: Context,
                   private val allEvents: Boolean,
                   private val eventClickListener: EventClickListener,
                   private val appPrefs: AppPrefs) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataSet: List<HourBlock> = emptyList()
    private var filteredData: ArrayList<HourBlock?> = ArrayList()

    override fun getItemCount(): Int {
        return filteredData.size
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
            VIEW_TYPE_NEW_ROW -> {
                val v = LayoutInflater.from(context).inflate(R.layout.item_new_row, parent, false)
                NewRowViewHolder(v)
            }
            else -> throw UnsupportedOperationException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ScheduleBlockViewHolder) {
            val adjustedPosition = position
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

        val adjustedPosition = position
        if (filteredData[adjustedPosition] == null) {
            return VIEW_TYPE_NEW_ROW
        }

        val item = filteredData[adjustedPosition]?.timeBlock
        when (item) {
            is SessionWithSpeakers -> return VIEW_TYPE_EVENT
            is Block -> return VIEW_TYPE_BLOCK
            else -> throw UnsupportedOperationException()
        }
    }

    private fun updateData() {
        filteredData.clear()
            for (item in dataSet) {
                val position = filteredData.size
                if (item.hourStringDisplay.isNotBlank() && position.isOdd()) {
                    // Insert an empty block to indicate a new row
                    filteredData.add(null)
                }
                else
                {
                    Log.e("EventAdapter", "What not odd?!")
                }
                filteredData.add(item)
            }

        notifyDataSetChanged()
    }

    fun updateEvents(data: List<HourBlock>) {
        dataSet = data
        updateData()
    }


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

    inner class NewRowViewHolder(itemView: View) : ScheduleCardViewHolder(itemView)

    companion object {
        private val VIEW_TYPE_EVENT = 0
        private val VIEW_TYPE_BLOCK = 1
        private val VIEW_TYPE_PAST_EVENT = 2
        private val VIEW_TYPE_NEW_ROW = 4
        private val HEADER_ITEMS_COUNT = 1
    }
}

interface EventClickListener {
    fun onEventClick(event: Event)
}

data class UpdateAllowNotificationEvent(val allow: Boolean)

