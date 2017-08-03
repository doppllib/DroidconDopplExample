package co.touchlab.droidconandroid

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.touchlab.droidconandroid.shared.data.Event
import co.touchlab.droidconandroid.shared.data.EventInfo
import co.touchlab.droidconandroid.shared.data.Track
import co.touchlab.droidconandroid.shared.data.UserAccount
import co.touchlab.droidconandroid.shared.presenter.AppManager
import co.touchlab.droidconandroid.shared.presenter.EventDetailHost
import co.touchlab.droidconandroid.shared.presenter.EventDetailViewModel
import kotlinx.android.synthetic.main.fragment_event_detail.*
import java.util.*

/**
 * Created by kgalligan on 7/27/14.
 */

class EventDetailFragment : Fragment(), EventDetailHost {

    private val eventId: Long by lazy { findEventIdArg() }

    private val viewModel: EventDetailViewModel by lazy {
        val factory = EventDetailViewModel.Factory()
        AppManager.getInstance().appComponent.inject(factory)
        ViewModelProviders.of(this, factory)[EventDetailViewModel::class.java]
    }

    private var trackColor: Int = 0
    private var fabColorList: ColorStateList? = null

    companion object {
        @JvmField
        val EXTRA_EVENT_ID = "EXTRA_EVENT_ID"
        private val FAB_URL = "http://imgur.com/gallery/7drHiqr"
        private val TIME_FORMAT = "h:mm a"
        private val EVENT_ID = "EVENT_ID"
        private val TRACK_ID = "TRACK_ID"

        fun newInstance(id: Long, track: Int): EventDetailFragment {
            val bundle = Bundle()
            bundle.putLong(EVENT_ID, id)
            bundle.putInt(TRACK_ID, track)

            val f = EventDetailFragment()
            f.arguments = bundle
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unregister()
    }

    private fun findEventIdArg(): Long {
        var eventId = arguments?.getLong(EVENT_ID, -1)
        if (eventId == null || eventId == -1L) {
            if (activity == null) return -1L
            eventId = activity.intent.getLongExtra(EVENT_ID, -1)
        }

        if (eventId == -1L)
            throw IllegalArgumentException("Must set event id")

        return eventId
    }

    /**
     * Gets the track ID argument. This is to make sure we don't flash the incorrect colors
     * on things like the FAB and toolbar while waiting to load the event details
     */
    private fun findTrackIdArg(): String? {
        var trackId = arguments?.getString(TRACK_ID)
        if (trackId == null) {
            trackId = activity.intent.getStringExtra(TRACK_ID)
        }

        return trackId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_detail, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.register(this)
        viewModel.getDetails(eventId)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        toolbar.title = ""
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        recycler.layoutManager = LinearLayoutManager(getActivity())

        updateTrackColor(findTrackIdArg())
    }

    override fun dataRefresh(eventInfo: EventInfo) {

        val event = eventInfo.event

        updateTrackColor(event.category)
        updateFAB(event)

        updateContent(event,
                eventInfo.speakers, // make another call for speakers
                eventInfo.conflict)
    }

    override fun reportError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun updateRsvp(event: Event) {
        updateFAB(event)
    }

    /**
     * Sets up the floating action bar according to the event details. This includes setting the color
     * and adjusting the icon according to rsvp status
     */
    private fun updateFAB(event: Event) {
        //Follow Fab
        fab.backgroundTintList = fabColorList
        fab.setColorFilter(trackColor)
        fab.rippleColor = ContextCompat.getColor(context, R.color.white)

        if (event.isRsvped) {
            fab.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_check))
            fab.isActivated = true
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_plus))
            fab.isActivated = false
        }

        if (event.isNow) {
            fab.setOnLongClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(FAB_URL))
                if (intent.resolveActivity(activity.packageManager) != null)
                    activity.startActivity(intent)
                true
            }
        }

        val layoutParams = fab.layoutParams as CoordinatorLayout.LayoutParams
        if (event.isPast) {
            layoutParams.anchorId = View.NO_ID
            fab.layoutParams = layoutParams
            fab.visibility = View.GONE
        } else {
            fab.setOnClickListener {
                if (event.isRsvped) {
                    viewModel.setRsvp(false, eventId)
                } else {
                    viewModel.setRsvp(true, eventId)
                }
            }

            layoutParams.anchorId = R.id.appbar
            fab.layoutParams = layoutParams
            fab.visibility = View.VISIBLE
        }
    }

    /**
     * Adds all the content to the recyclerView
     */
    private fun updateContent(event: Event, speakers: List<UserAccount>?, conflict: Boolean) {
        val adapter = EventDetailAdapter(activity, viewModel, trackColor)

        //Construct the time and venue string and add it to the adapter
        var formattedStart = event.startDateLong!!.formatDate(TIME_FORMAT)
        val formattedEnd = event.endDateLong!!.formatDate(TIME_FORMAT)

        val startMarker = formattedStart.substring(Math.max(formattedStart.length - 3, 0))
        val endMarker = formattedEnd.substring(Math.max(formattedEnd.length - 3, 0))
        if (startMarker == endMarker) {
            formattedStart = formattedStart.substring(0, Math.max(formattedStart.length - 3, 0))
        }

        val venueFormatString = resources.getString(R.string.event_venue_time)
        adapter.addHeader(event.name, venueFormatString.format(event.venue.name, formattedStart, formattedEnd))

        if (event.isNow)
            adapter.addInfo("<i><b>" + resources.getString(R.string.event_now) + "</b></i>")
        else if (event.isPast)
            adapter.addInfo("<i><b>" + resources.getString(R.string.event_past) + "</b></i>")
        else if (conflict)
            adapter.addInfo("<i><b>" + resources.getString(R.string.event_conflict) + "</b></i>")

        //Description text
        if (!event.description.isNullOrBlank())
            adapter.addBody(event.description)

        for (item: UserAccount in speakers as ArrayList) {
            adapter.addSpeaker(item)
        }

        //TODO add feedback link
        //adapter.addFeedback("feedback link goes here")

        recycler.adapter = adapter
    }

    /**
     * Ensures that all view which are colored according to the track are updated
     */
    private fun updateTrackColor(category: String?) {
        //Default to design
        val track = if (!category.isNullOrBlank()) Track.findByServerName(category)
        else Track.findByServerName("Design")

        var drawableResourceId = R.drawable.illo_development

        when (track) {
            Track.DEVDESIGN -> {
                drawableResourceId = R.drawable.illo_designdevtalk
            }

            Track.DESIGN -> {
                drawableResourceId = R.drawable.illo_designtalk
            }

            Track.DESIGNLAB -> {
                drawableResourceId = R.drawable.illo_designlab
            }
        }

        trackColor = ContextCompat.getColor(context,
                context.resources.getIdentifier(track.textColorRes,
                        "color",
                        context.packageName))
        fabColorList = ContextCompat.getColorStateList(context,
                context.resources.getIdentifier(track.checkBoxSelectorRes,
                        "color",
                        context.packageName))

        collapsingToolbar.setContentScrimColor(trackColor)
        collapsingToolbar.setStatusBarScrimColor(trackColor)
        backdrop.setBackgroundColor(trackColor)
        backdrop.setImageResource(drawableResourceId)
    }
}