package co.touchlab.droidconandroid.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.droidconandroid.R
import co.touchlab.droidconandroid.SponsorsListFragment
import co.touchlab.droidconandroid.shared.network.SponsorsResult
import co.touchlab.droidconandroid.shared.presenter.AppManager
import co.touchlab.droidconandroid.shared.utils.AnalyticsEvents
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_sponsor.view.*
import java.util.ArrayList

/**
 * Created by sufeizhao on 7/11/17.
 */
class SponsorsAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataset: ArrayList<Any> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        if (dataset[position] is SponsorsResult.Sponsor)
            return VIEW_TYPE_ITEM
        else
            return VIEW_TYPE_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_sponsor, parent, false)
                return SponsorVH(view)
            }
            else -> {
                val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_empty, parent, false)
                return EmptyVH(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            val view = (holder as SponsorVH).itemView
            val data = dataset[position] as SponsorsResult.Sponsor

            // Load image and set content desc
            view.item_sponsor_image.contentDescription = data.sponsorName
            Picasso.with(context)
                    .load(data.sponsorImage)
                    .placeholder(R.drawable.placeholder_sponsor_image)
                    .into(view.item_sponsor_image)

            // Set view click
            view.setOnClickListener {
                AppManager.getPlatformClient()
                        .logEvent(AnalyticsEvents.CLICK_SPONSOR, AnalyticsEvents.PARAM_ITEM_NAME, data.sponsorLink)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.sponsorLink))
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class SponsorVH(val item: View) : RecyclerView.ViewHolder(item)

    inner class EmptyVH(val item: View) : RecyclerView.ViewHolder(item)

    fun getItemSpanSize(position: Int): Int {
        if (dataset[position] is SponsorsResult.Sponsor) {
            return (dataset[position] as SponsorsResult.Sponsor).spanCount
        } else {
            return (dataset[position] as SponsorsListFragment.Empty).spanCount
        }
    }

    fun addAll(sponsors: List<Any>) {
        dataset.addAll(sponsors)
        notifyDataSetChanged()
    }

    fun add(spanCount: Int, name: String, image: String, link: String) {
        dataset.add(SponsorsResult().Sponsor(spanCount, name, image, link))
        notifyDataSetChanged()
    }

    companion object {
        private val VIEW_TYPE_ITEM = 0
        private val VIEW_TYPE_EMPTY = 1
    }
}