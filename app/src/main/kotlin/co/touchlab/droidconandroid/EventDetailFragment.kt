package co.touchlab.droidconandroid

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.touchlab.android.threading.tasks.TaskQueue
import co.touchlab.droidconandroid.shared.data.*
import co.touchlab.droidconandroid.shared.interactors.EventDetailInteractor
import co.touchlab.droidconandroid.shared.interactors.EventVideoDetailsInteractor
import co.touchlab.droidconandroid.shared.network.DataHelper
import co.touchlab.droidconandroid.shared.network.VideoDetailsRequest
import co.touchlab.droidconandroid.shared.network.dao.EventVideoDetails
import co.touchlab.droidconandroid.shared.presenter.AppManager
import co.touchlab.droidconandroid.shared.presenter.EventDetailHost
import co.touchlab.droidconandroid.shared.presenter.EventDetailPresenter
import co.touchlab.droidconandroid.shared.tasks.AddRsvpTask
import co.touchlab.droidconandroid.shared.tasks.RemoveRsvpTask
import kotlinx.android.synthetic.main.fragment_event_detail.*
import kotlinx.android.synthetic.main.view_streaming_email_dialog.view.*
import java.util.*

/**
 * Created by kgalligan on 7/27/14.
 */

class EventDetailFragment : Fragment(), EventDetailHost {

    private val eventId: Long by lazy { findEventIdArg() }

    private val presenter: EventDetailPresenter by lazy {
        val retrofit = DataHelper.makeRetrofit2Client(AppManager.getPlatformClient().baseUrl())
        val videoDetailsRequest = retrofit.create(VideoDetailsRequest::class.java)
        val videoDetailsInteractor = EventVideoDetailsInteractor(videoDetailsRequest, eventId)

        val helper = DatabaseHelper.getInstance(activity)
        val eventDetailsInteractor = EventDetailInteractor(helper, eventId)
        EventDetailPresenter(eventDetailsInteractor, videoDetailsInteractor)
    }

    private var trackColor: Int = 0
    private var fabColorList: ColorStateList? = null

    companion object {
        @JvmField
        val EXTRA_STREAM_LINK = "EXTRA_STREAM_LINK"
        @JvmField
        val EXTRA_STREAM_COVER = "EXTRA_STREAM_COVER"
        @JvmField
        val EXTRA_EVENT_ID = "EXTRA_EVENT_ID"
        private val TICKET_URL = "https://www.eventbrite.com/e/droidcon-nyc-2016-tickets-25645809306"
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
        presenter.unregister()
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
        presenter.register(this)
        presenter.getDetails()
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
                eventInfo.videoDetails,
                eventInfo.speakers,
                eventInfo.conflict)
    }

    override fun reportError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun showTicketOptions(email: String?, link: String, cover: String) {
        val bodyView = LayoutInflater.from(activity).inflate(R.layout.view_streaming_email_dialog, null)

        val buyClickListener = DialogInterface.OnClickListener { _, _ ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(TICKET_URL)
            startActivity(intent)
        }

        val builder = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.whoops))
                .setView(bodyView)
                .setPositiveButton(getString(R.string.buy_ticket), buyClickListener)
                .setNegativeButton(getString(R.string.cancel), null)
        val dialog = builder.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(activity, R.color.text_gray))

        bodyView.streaming_email_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(email: CharSequence?, start: Int, before: Int, count: Int) {
                val buttonText: String
                if (email!!.isNotEmpty()) {
                    buttonText = getString(R.string.proceed)
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText, { _, _ ->
                        presenter.setEventbriteEmail(email.toString(), link, cover)
                    })
                } else {
                    buttonText = getString(R.string.buy_ticket)
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText, buyClickListener)
                }
                // setButton doesn't update the title after dialog is shown, use getButton
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).text = buttonText
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun resetStreamProgress() { recycler.adapter?.notifyDataSetChanged() }

    override fun openSlack(slackLink: String, slackLinkHttp: String, showSlackDialog: Boolean) {
        SlackHelper.openSlack(activity, slackLink, slackLinkHttp, showSlackDialog)
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
                    TaskQueue.loadQueueDefault(activity).execute(RemoveRsvpTask(event.id))
                } else {
                    TaskQueue.loadQueueDefault(activity).execute(AddRsvpTask(event.id))
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
    private fun updateContent(event: Event, videoDetails: EventVideoDetails, speakers: List<UserAccount>?, conflict: Boolean) {
        val adapter = EventDetailAdapter(activity, presenter, trackColor)

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

        if (videoDetails.hasStream())
            adapter.addStream(videoDetails.mergedStreamLink, "", videoDetails.isNow)

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