package co.touchlab.droidconandroid

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.droidconandroid.shared.data.UserAccount
import co.touchlab.droidconandroid.shared.presenter.EventDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_event_header.view.*
import kotlinx.android.synthetic.main.item_event_info.view.*
import kotlinx.android.synthetic.main.item_event_text.view.*
import kotlinx.android.synthetic.main.item_user_summary.view.*
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Created by samuelhill on 8/7/15.
 */

class EventDetailAdapter(private val context: Context,
                         private val viewModel: EventDetailViewModel,
                         private val trackColor: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //dataset
    private var data = ArrayList<Detail>()

    //=================== Public helper functions ===================
    fun addHeader(title: String, venue: String) {
        data.add(HeaderDetail(TYPE_HEADER, title, venue))
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
        data.add(SpeakerDetail(TYPE_SPEAKER, speaker.avatarImageUrl(), speaker.name, speaker.company, speaker.profile, speaker.userCode, speaker.id))
    }

    fun addFeedback(link: String) {
        data.add(TextDetail(TYPE_FEEDBACK, link, 0))
    }

    //=================== Adapter Overrides ===================
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_event_header, parent, false)
                HeaderVH(view)
            }

            TYPE_BODY -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_event_text, parent, false)
                TextVH(view)
            }
            TYPE_INFO -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_event_info, parent, false)
                InfoVH(view)
            }
            TYPE_SPEAKER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_user_summary, parent, false)
                SpeakerVH(view)
            }
            TYPE_SPACE -> {
                val view = View(context)
                parent!!.addView(view)
                object : RecyclerView.ViewHolder(view) {}
            }
            TYPE_FEEDBACK -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_event_feedback, parent, false)
                FeedbackVH(view)
            }
            else -> null
        }
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
                val view = (holder as HeaderVH).itemView
                view.title.text = (data[position] as HeaderDetail).title
                view.subtitle.text = (data[position] as HeaderDetail).subtitle
            }

            TYPE_INFO -> {
                val view = (holder as InfoVH).itemView
                view.info.text = Html.fromHtml(StringUtils.trimToEmpty((data[position] as TextDetail).text))
            }

            TYPE_BODY -> {
                val view = (holder as TextVH).itemView
                view.body.text = Html.fromHtml(StringUtils.trimToEmpty((data[position] as TextDetail).text))
            }

            TYPE_SPEAKER -> {
                val view = (holder as SpeakerVH).itemView
                val user = data[position] as SpeakerDetail

                if (!user.avatar.isNullOrBlank()) {
                    Picasso.with(context).load(user.avatar)
                            .noFade()
                            .placeholder(R.drawable.profile_placeholder)
                            .into(view.profile_image)
                }

                view.name.text = context.getString(R.string.event_speaker_name).format(user.name, user.company)
                view.name.setTextColor(trackColor)

                view.setOnClickListener { UserDetailActivity.callMe(context as Activity, user.userId) }
                view.bio.text = Html.fromHtml(StringUtils.trimToEmpty(user.bio))
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

    companion object {
        private val TYPE_HEADER: Int = 0
        private val TYPE_BODY: Int = 1
        private val TYPE_INFO: Int = 3
        private val TYPE_SPACE: Int = 4
        private val TYPE_SPEAKER: Int = 5
        private val TYPE_FEEDBACK: Int = 6
    }

    //=================== Adapter type models ===================
    open inner class Detail(val type: Int) {
        fun getItemType(): Int {
            return type
        }
    }

    inner class HeaderDetail(type: Int, val title: String, val subtitle: String) : Detail(type)

    inner class TextDetail(type: Int, val text: String, val icon: Int) : Detail(type)

    inner class SpeakerDetail(type: Int, val avatar: String?, val name: String, val company: String, val bio: String?, val userCode: String, val userId: Long) : Detail(type)

    inner class SpaceDetail(type: Int, val size: Int) : Detail(type)

    //=================== Type ViewHolders ===================

    inner class HeaderVH(val item: View) : RecyclerView.ViewHolder(item) {}

    inner class InfoVH(val item: View) : RecyclerView.ViewHolder(item) {}

    inner class TextVH(val item: View) : RecyclerView.ViewHolder(item) {}

    inner class SpeakerVH(val item: View) : RecyclerView.ViewHolder(item) {}

    inner class FeedbackVH(val item: View) : RecyclerView.ViewHolder(item) {}
}