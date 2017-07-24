package co.touchlab.droidconandroid

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.touchlab.android.threading.tasks.TaskQueue
import co.touchlab.droidconandroid.shared.data.Event
import co.touchlab.droidconandroid.shared.data.Track
import co.touchlab.droidconandroid.shared.data.UserAccount
import co.touchlab.droidconandroid.shared.presenter.EventDetailHost
import co.touchlab.droidconandroid.shared.presenter.EventDetailPresenter
import co.touchlab.droidconandroid.shared.tasks.AddRsvpTask
import co.touchlab.droidconandroid.shared.tasks.EventVideoDetailsTask
import co.touchlab.droidconandroid.shared.tasks.RemoveRsvpTask
import co.touchlab.droidconandroid.shared.tasks.StartWatchVideoTask
import co.touchlab.droidconandroid.shared.utils.TimeUtils
import com.wnafee.vector.compat.ResourcesCompat
import kotlinx.android.synthetic.main.fragment_event_detail.*
import kotlinx.android.synthetic.main.view_streaming_email_dialog.view.*
import java.util.*

/**
 * Created by kgalligan on 7/27/14.
 */
var businessDrawable: Drawable? = null
var designDrawable: Drawable? = null
var devDrawable: Drawable? = null

class EventDetailFragment() : Fragment(), EventDetailHost
{
    private var trackColor: Int = 0
    private var fabColorList: ColorStateList? = null
    private var presenter: EventDetailPresenter? = null

    companion object
    {
        val EVENT_ID = "EVENT_ID"
        val TRACK_ID = "TRACK_ID"

        fun createFragment(id: Long, track: Int): EventDetailFragment
        {
            val bundle = Bundle()
            bundle.putLong(EVENT_ID, id)
            bundle.putInt(TRACK_ID, track)

            val f = EventDetailFragment()
            f.arguments = bundle

            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
//        EventBusExt.getDefault() !!.register(this)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        presenter!!.unregister()
//        EventBusExt.getDefault() !!.unregister(this)
    }

    private fun findEventIdArg(): Long
    {
        var eventId = arguments?.getLong(EVENT_ID, - 1)
        if (eventId == null || eventId == - 1L)
        {
            if (activity == null)
                return - 1L

            eventId = activity !!.intent !!.getLongExtra(EVENT_ID, - 1)
        }

        if (eventId == - 1L)
            throw IllegalArgumentException("Must set event id")

        return eventId
    }

    /**
     * Gets the track ID argument. This is to make sure we don't flash the incorrect colors
     * on things like the FAB and toolbar while waiting to load the event details
     */
    private fun findTrackIdArg(): String?
    {
        var trackId = arguments?.getString(TRACK_ID)
        if (trackId == null)
        {
            trackId = activity.intent.getStringExtra(TRACK_ID)
        }

        return trackId
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        presenter = EventDetailPresenter(context, findEventIdArg(), this)

        return inflater !!.inflate(R.layout.fragment_event_detail, null)
    }

    override fun onResume() {
        super.onResume()
        presenter!!.refreshData()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        toolbar.title = ""
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        recycler.layoutManager = LinearLayoutManager(getActivity())

        updateTrackColor(findTrackIdArg())
    }

    override fun videoDataRefresh()
    {
        dataRefresh()
    }

    override fun dataRefresh()
    {
        if (! presenter !!.eventDetailLoadTask.eventId.equals(findEventIdArg()))
            return

        val event = presenter !!.eventDetailLoadTask.event !!

        updateTrackColor(event.category)
        updateFAB(event)

        updateContent(event,
                presenter !!.eventVideoDetailsTask,
                presenter !!.eventDetailLoadTask.speakers,
                presenter !!.eventDetailLoadTask.conflict)
    }

    override fun reportError(error: String){
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun showTicketOptions(email: String?, link: String, cover: String)
    {
        val bodyView = LayoutInflater.from(activity).inflate(R.layout.view_streaming_email_dialog, null)

        val buyClickListener = DialogInterface.OnClickListener { dialogInterface, i ->
            val url = "https://www.eventbrite.com/e/droidcon-nyc-2016-tickets-25645809306"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        val builder = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
        builder.setTitle("Whoops!")
        builder.setView(bodyView)
        builder.setPositiveButton("Buy a ticket", buyClickListener)
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(activity, R.color.text_gray))

        bodyView.streaming_email_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(email: CharSequence?, start: Int, before: Int, count: Int) {
                if(email!!.isNotEmpty())
                {
                    val buttonText = "Continue"
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText, { dialogInterface, i ->
                        presenter!!.setEventbriteEmail(email!!.toString(), link, cover)
                    })
                    // setButton doesn't update the title after dialog is shown, use getButton
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).text = buttonText
                }
                else
                {
                    val buttonText = "Buy a ticket"
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText, buyClickListener)
                    // setButton doesn't update the title after dialog is shown, use getButton
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).text = buttonText
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun resetStreamProgress(){
        if(recycler.adapter != null)
            recycler.adapter.notifyDataSetChanged()
    }

    /**
     * Sets up the floating action bar according to the event details. This includes setting the color
     * and adjusting the icon according to rsvp status
     */
    private fun updateFAB(event: Event)
    {
        //Follow Fab
        fab.backgroundTintList = fabColorList
        fab.setColorFilter(trackColor)
        fab.setRippleColor(ContextCompat.getColor(context, R.color.white))

        if (event.isRsvped)
        {
            fab.setImageDrawable(ResourcesCompat.getDrawable(activity, R.drawable.ic_check))
            fab.isActivated = true
        }
        else
        {
            fab.setImageDrawable(ResourcesCompat.getDrawable(activity, R.drawable.ic_plus))
            fab.isActivated = false
        }

        if (! event.isPast)
        {
            fab.setOnClickListener { v ->
                if (event.isRsvped)
                {
                    TaskQueue.loadQueueDefault(activity).execute(RemoveRsvpTask(event.id))
                }
                else
                {
                    TaskQueue.loadQueueDefault(activity).execute(AddRsvpTask(event.id))
                }
            }
        }

        if (event.isNow)
        {
            fab.setOnLongClickListener { v ->
                val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://imgur.com/gallery/7drHiqr"))
                if (intent.resolveActivity(activity.packageManager) != null)
                {
                    activity.startActivity(intent)
                }
                true
            }
        }

        val p = fab.layoutParams as CoordinatorLayout.LayoutParams
        if (event.isPast)
        {
            p.anchorId = View.NO_ID
            fab.layoutParams = p
            fab.visibility = View.GONE
        }
        else
        {
            p.anchorId = R.id.appbar
            fab.layoutParams = p
            fab.visibility = View.VISIBLE
        }
    }

    /**
     * Adds all the content to the recyclerView
     */
    private fun updateContent(event: Event, videoDetailsTask: EventVideoDetailsTask?, speakers: List<UserAccount>?, conflict: Boolean)
    {
        val adapter = EventDetailAdapter(activity, this, presenter!!, trackColor)

        //Construct the time and venue string and add it to the adapter
        val startDateVal = Date(event.startDateLong !!)
        val endDateVal = Date(event.endDateLong !!)
        val timeFormat = TimeUtils.makeDateFormat("h:mm a")
        val venueFormatString = resources.getString(R.string.event_venue_time)

        var formattedStart = timeFormat.format(startDateVal)
        val formattedEnd = timeFormat.format(endDateVal)

        val startMarker = formattedStart.substring(Math.max(formattedStart.length - 3, 0))
        val endMarker = formattedEnd.substring(Math.max(formattedEnd.length - 3, 0))

        if (TextUtils.equals(startMarker, endMarker))
        {
            formattedStart = formattedStart.substring(0, Math.max(formattedStart.length - 3, 0))
        }

        adapter.addHeader(event.name, venueFormatString.format(event.venue.name, formattedStart, formattedEnd))

        if(videoDetailsTask != null && videoDetailsTask.hasStream())
            adapter.addStream(videoDetailsTask.mergedStreamLink, "", videoDetailsTask.isNow)

        if (event.isNow)
            adapter.addInfo("<i><b>" + resources.getString(R.string.event_now) + "</b></i>")
        else if (event.isPast)
            adapter.addInfo("<i><b>" + resources.getString(R.string.event_past) + "</b></i>")
        else if (conflict)
            adapter.addInfo("<i><b>" + resources.getString(R.string.event_conflict) + "</b></i>")

        //Description text
        if (! TextUtils.isEmpty(event.description))
            adapter.addBody(event.description)

        for (item: UserAccount in speakers as ArrayList)
        {
            adapter.addSpeaker(item)
        }

        //TODO add feedback link
        //adapter.addFeedback("feedback link goes here")

        recycler.adapter = adapter
    }

    /**
     * Ensures that all view which are colored according to the track are updated
     */
    private fun updateTrackColor(category: String?)
    {
        //Default to design
        val track = if (! TextUtils.isEmpty(category)) Track.findByServerName(category)
        else Track.findByServerName("Design")

        var drawableResourceId = R.drawable.illo_development

        when (track)
        {
            Track.DEVDESIGN ->
            {
                drawableResourceId = R.drawable.illo_designdevtalk
            }

            Track.DESIGN ->
            {
                drawableResourceId = R.drawable.illo_designtalk
            }

            Track.DESIGNLAB ->
            {
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