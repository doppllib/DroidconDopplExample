package co.touchlab.droidconandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import co.touchlab.android.threading.eventbus.EventBusExt
import co.touchlab.android.threading.tasks.TaskQueue
import co.touchlab.droidconandroid.shared.network.SponsorsResult
import co.touchlab.droidconandroid.shared.presenter.AppManager
import co.touchlab.droidconandroid.shared.tasks.SponsorsTask
import co.touchlab.droidconandroid.shared.utils.AnalyticsEvents
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_sponsors_list.*
import java.util.*

const val SPONSOR_TYPE = "SPONSOR_TYPE"
const val SPONSOR_COUNT = 3

fun createSponsorsListFragment(type: Int): SponsorsListFragment {
    val fragment = SponsorsListFragment()
    val args = Bundle()
    args.putInt(SPONSOR_TYPE, type)
    fragment.arguments = args
    return fragment
}

class SponsorsListFragment() : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_sponsors_list, container, false)
    }

    var adapter: SponsorsAdapter? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        EventBusExt.getDefault()!!.register(this)
        val type = arguments.getInt(SPONSOR_TYPE)
        TaskQueue.loadQueueDefault(context.applicationContext).execute(SponsorsTask(type))
    }

    override fun onStop() {
        super.onStop()
        EventBusExt.getDefault()!!.unregister(this)
    }

    @Suppress("unused")
    fun onEventMainThread(task: SponsorsTask) {
        if (task.type == arguments.getInt(SPONSOR_TYPE)) {
            // Filter through and insert "filler items" .. TODO This is a hack
            var finalList: ArrayList<Any> = ArrayList()
            var totalSpanCount = 1

            if(task.response != null) {
                totalSpanCount = task.response!!.totalSpanCount
                val lastIndex = task.response!!.sponsors.lastIndex
                val spanCounts = SparseIntArray()
                for (sponsor in task.response!!.sponsors) {
                    // Add object to results
                    finalList.add(sponsor)

                    // Increment count
                    var currentCount = spanCounts.get(sponsor.spanCount, -1)
                    if (currentCount == -1) {
                        spanCounts.put(sponsor.spanCount, 1)
                        currentCount = 1
                    } else {
                        spanCounts.put(sponsor.spanCount, ++currentCount)
                    }

                    // Check if last item of group, if so, insert any spaces if needed
                    val index = task.response!!.sponsors.indexOf(sponsor)
                    if (index != lastIndex && sponsor.spanCount != task.response!!.sponsors[index + 1].spanCount) {
                        var emptySpots = (totalSpanCount - (currentCount * sponsor.spanCount) % totalSpanCount) / sponsor.spanCount
                        while (emptySpots > 0) {
                            finalList.add(Empty(sponsor.spanCount))
                            emptySpots--
                        }
                    }
                }
            }

            // Create adapter and set on recyclerView
            adapter = SponsorsAdapter()
            adapter!!.addAll(finalList)
            sponsor_list!!.adapter = adapter

            // Set Layout manager w/ a non-default span-size lookup
            var layoutManager = GridLayoutManager(activity, totalSpanCount)
            val spanSizeLookip: GridLayoutManager.SpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return adapter!!.getItemSpanSize(position)
                }
            }
            spanSizeLookip.isSpanIndexCacheEnabled = false
            layoutManager.spanSizeLookup = spanSizeLookip
            sponsor_list!!.layoutManager = layoutManager
        }
    }

    inner class Empty (val spanCount: Int)

    inner class SponsorsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val VIEW_TYPE_ITEM = 0
        private val VIEW_TYPE_EMPTY = 1

        private var dataset: ArrayList<Any> = ArrayList()

        override fun getItemViewType(position: Int): Int {
             if (dataset[position] is SponsorsResult.Sponsor) return VIEW_TYPE_ITEM else return VIEW_TYPE_EMPTY
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
            if (viewType == VIEW_TYPE_ITEM) {
                val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_sponsor, parent, false)
                return SponsorVH(view)
            } else {
                val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_empty, parent, false)
                return EmptyVH(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (getItemViewType(position) == VIEW_TYPE_ITEM) {
                val vh = holder as SponsorVH
                val data = dataset[position] as SponsorsResult.Sponsor

                // Load image and set content desc
                vh.image!!.contentDescription = data.sponsorName
                Picasso.with(context)
                        .load(data.sponsorImage)
                        .placeholder(R.drawable.placeholder_sponsor_image)
                        .into(vh.image)

                // Set view click
                vh.itemView.setOnClickListener {
                    AppManager.getPlatformClient()
                            .logEvent(AnalyticsEvents.CLICK_SPONSOR, AnalyticsEvents.PARAM_ITEM_NAME, data.sponsorLink)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.sponsorLink))
                    startActivity(intent)
                }
            }
        }

        override fun getItemCount(): Int {
            return dataset.size
        }

        inner class SponsorVH(val item: View) : RecyclerView.ViewHolder(item) {
            var image: ImageView? = null

            init {
                image = item.findViewById(R.id.item_sponsor_image) as ImageView
            }
        }

        inner class EmptyVH(val item: View) : RecyclerView.ViewHolder(item) {

        }

        fun getItemSpanSize(position: Int): Int {
            if (dataset[position] is SponsorsResult.Sponsor) {
                return (dataset[position] as SponsorsResult.Sponsor).spanCount
            } else{
                return (dataset[position] as Empty).spanCount
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
    }

}