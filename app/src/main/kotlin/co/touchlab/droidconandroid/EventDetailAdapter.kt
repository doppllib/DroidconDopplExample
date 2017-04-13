package co.touchlab.droidconandroid

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.droidconandroid.shared.data.UserAccount
import co.touchlab.droidconandroid.shared.presenter.EventDetailPresenter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_event_header.view.*
import kotlinx.android.synthetic.main.item_event_info.view.*
import kotlinx.android.synthetic.main.item_event_stream.view.*
import kotlinx.android.synthetic.main.item_event_text.view.*
import kotlinx.android.synthetic.main.item_user_summary.view.*
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Created by samuelhill on 8/7/15.
 */

private const val TYPE_HEADER: Int = 0
private const val TYPE_BODY: Int = 1
private const val TYPE_INFO: Int = 3
private const val TYPE_SPACE: Int = 4
private const val TYPE_SPEAKER: Int = 5
private const val TYPE_STREAM: Int = 6
private const val TYPE_FEEDBACK: Int = 7
const val EXTRA_STREAM_LINK = "EXTRA_STREAM_LINK"
const val EXTRA_STREAM_COVER = "EXTRA_STREAM_COVER"
const val EXTRA_EVENT_ID = "EXTRA_EVENT_ID"

class EventDetailAdapter(val context: Context, val frag:EventDetailFragment, val presenter:EventDetailPresenter, val trackColor: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //dataset
    private var data = ArrayList<Detail>()

    //=================== Public helper functions ===================
    fun addHeader(title: String, venue: String) {
        data.add(HeaderDetail(TYPE_HEADER, title, venue))
    }

    fun addStream(link: String, cover: String, liveNow: Boolean) {
        data.add(StreamDetail(TYPE_STREAM, link, cover, liveNow))
    }

    fun addBody(description: String) {
        data.add(TextDetail(TYPE_BODY, description, 0))
    }

    fun addInfo(description: String) {
        data.add(TextDetail(TYPE_INFO, description, 0))
    }

    fun addSpace(size: Int) {
        data.add(SpaceDetail(TYPE_SPACE, size))
    }

    fun addSpeaker(speaker: UserAccount) {
        data.add(SpeakerDetail(TYPE_SPEAKER, speaker.avatarImageUrl(), speaker.name, speaker.company, speaker.profile, speaker.userCode))
    }

    fun addFeedback(link: String) {
        data.add(TextDetail(TYPE_FEEDBACK, link, 0))
    }

    //=================== Adapter Overrides ===================
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var holder: RecyclerView.ViewHolder? = null
        when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_event_header, parent, false)
                holder = HeaderVH(view)
            }
            TYPE_STREAM -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_event_stream, parent, false)
                holder = StreamVH(view)
            }
            TYPE_BODY -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_event_text, parent, false)
                holder = TextVH(view)
            }
            TYPE_INFO -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_event_info, parent, false)
                holder = InfoVH(view)
            }
            TYPE_SPEAKER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_user_summary, parent, false)
                holder = SpeakerVH(view)
            }
            TYPE_SPACE -> {
                val view = View(context)
                parent!!.addView(view)
                holder = object : RecyclerView.ViewHolder(view) {}
            }
            TYPE_FEEDBACK -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_event_feedback, parent, false)
                holder = FeedbackVH(view)
            }
        }
        return holder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].getItemType()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder!!.itemViewType) {
            TYPE_HEADER -> {
                val headerVH = holder as HeaderVH
                headerVH.itemView.title.text = (data[position] as HeaderDetail).title
                headerVH.itemView.subtitle.text = (data[position] as HeaderDetail).subtitle
            }

            TYPE_STREAM -> {
                val streamVH = holder as StreamVH
                val detail = data[position] as StreamDetail
                streamVH.itemView.streamNow.setOnClickListener {
                    presenter.callStartVideo(detail.link, detail.cover)
                    notifyDataSetChanged()
                }
                streamVH.itemView.streamNotNow.setOnClickListener {
                    presenter.callStartVideo(detail.link, detail.cover)
                    notifyDataSetChanged()
                }
                streamVH.itemView.slackButton.setOnClickListener {
                    presenter.openSlack()
                }

                if(presenter.isStreamStarting) {
                    streamVH.itemView.streamLoading.visibility = View.VISIBLE
                    streamVH.itemView.streamNow.visibility = View.GONE
                    streamVH.itemView.streamNotNow.visibility = View.GONE
                }
                else {
                    streamVH.itemView.streamLoading.visibility = View.GONE
                    if(detail.liveNow) {
                        streamVH.itemView.streamNow.visibility = View.VISIBLE
                        streamVH.itemView.streamNotNow.visibility = View.GONE
                        streamVH.itemView.imageLive.visibility = View.VISIBLE
                        streamVH.itemView.imageNotLive.visibility = View.GONE
                        streamVH.itemView.slackButton.visibility = View.VISIBLE
                    }
                    else{
                        streamVH.itemView.streamNow.visibility = View.GONE
                        streamVH.itemView.streamNotNow.visibility = View.VISIBLE
                        streamVH.itemView.imageLive.visibility = View.GONE
                        streamVH.itemView.imageNotLive.visibility = View.VISIBLE
                        streamVH.itemView.slackButton.visibility = View.GONE
                    }

                }
            }

            TYPE_INFO -> {
                val infoVH = holder as InfoVH

                val descriptionSpanned = Html.fromHtml(StringUtils.trimToEmpty((data[position] as TextDetail).text)!!)
                infoVH.itemView.info.text = descriptionSpanned
            }

            TYPE_BODY -> {
                val bodyVH = holder as TextVH

                val descriptionSpanned = Html.fromHtml(StringUtils.trimToEmpty((data[position] as TextDetail).text)!!)
                bodyVH.itemView.body.text = descriptionSpanned
            }

            TYPE_SPEAKER -> {
                val speakerVH = holder as SpeakerVH
                val avatarView = speakerVH.itemView.profile_image
                val nameView = speakerVH.itemView.name
                val user = data[position] as SpeakerDetail

                if (!TextUtils.isEmpty(user.avatar)) {
                    Picasso.with(context).load(user.avatar)
                            .noFade()
                            .placeholder(R.drawable.profile_placeholder)
                            .into(avatarView)
                }

                val formatString = context.resources.getString(R.string.event_speaker_name)
                nameView!!.text = formatString.format(user.name, user.company)
                nameView.setTextColor(trackColor)

                speakerVH.itemView.setOnClickListener({
                    UserDetailActivity.callMe(context as Activity, user.userCode)
                })

                val bioSpanned = Html.fromHtml(StringUtils.trimToEmpty(user.bio)!!)
                speakerVH.itemView.bio.text = bioSpanned
            }

            TYPE_SPACE -> {
                val p = holder.itemView.layoutParams
                p.height = (data[position] as SpaceDetail).size
                holder.itemView.layoutParams = p
            }

            TYPE_FEEDBACK -> {
                val feedbackVH = holder as FeedbackVH
                // TODO add link to event feedback
            }
        }
    }

    //=================== Adapter type models ===================
    open inner class Detail(val type: Int) {
        fun getItemType(): Int {
            return type
        }
    }

    inner class HeaderDetail(type: Int, val title: String, val subtitle: String) : Detail(type)

    inner class TextDetail(type: Int, val text: String, val icon: Int) : Detail(type)

    inner class StreamDetail(type: Int, val link: String, val cover: String, val liveNow: Boolean) : Detail(type)

    inner class SpeakerDetail(type: Int, val avatar: String?, val name: String, val company: String, val bio: String?, val userCode: String) : Detail(type)

    inner class SpaceDetail(type: Int, val size: Int) : Detail(type)

    //=================== Type ViewHolders ===================

    inner class HeaderVH(val item: View) : RecyclerView.ViewHolder(item) {}

    inner class StreamVH(val item: View) : RecyclerView.ViewHolder(item) {}

    inner class InfoVH(val item: View) : RecyclerView.ViewHolder(item) {}

    inner class TextVH(val item: View) : RecyclerView.ViewHolder(item) {}

    inner class SpeakerVH(val item: View) : RecyclerView.ViewHolder(item) {}

    inner class FeedbackVH(val item: View) : RecyclerView.ViewHolder(item) {}
}