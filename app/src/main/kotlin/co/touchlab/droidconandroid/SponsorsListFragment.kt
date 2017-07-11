package co.touchlab.droidconandroid

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.android.threading.eventbus.EventBusExt
import co.touchlab.android.threading.tasks.TaskQueue
import co.touchlab.droidconandroid.shared.tasks.SponsorsTask
import co.touchlab.droidconandroid.ui.SponsorsAdapter
import kotlinx.android.synthetic.main.fragment_sponsors_list.*
import java.util.*

class SponsorsListFragment : Fragment() {
    var adapter: SponsorsAdapter? = null

    companion object {
        private val SPONSOR_TYPE = "SPONSOR_TYPE"

        fun newInstance(type: Int): SponsorsListFragment {
            val fragment = SponsorsListFragment()
            val args = Bundle()
            args.putInt(SPONSOR_TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_sponsors_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        EventBusExt.getDefault().register(this)
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

            task.response?.let { sponsorResult ->
                totalSpanCount = sponsorResult.totalSpanCount
                val lastIndex = sponsorResult.sponsors.lastIndex
                val spanCounts = SparseIntArray()
                for (sponsor in sponsorResult.sponsors) {
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
                    val index = sponsorResult.sponsors.indexOf(sponsor)
                    if (index != lastIndex && sponsor.spanCount != sponsorResult.sponsors[index + 1].spanCount) {
                        var emptySpots = (totalSpanCount - (currentCount * sponsor.spanCount) % totalSpanCount) / sponsor.spanCount
                        while (emptySpots > 0) {
                            finalList.add(Empty(sponsor.spanCount))
                            emptySpots--
                        }
                    }
                }
            }

            // Create adapter and set on recyclerView
            adapter = SponsorsAdapter(activity)
            adapter!!.addAll(finalList)
            sponsor_list.adapter = adapter

            // Set Layout manager w/ a non-default span-size lookup
            val layoutManager = GridLayoutManager(activity, totalSpanCount)
            val spanSizeLookip: GridLayoutManager.SpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return adapter!!.getItemSpanSize(position)
                }
            }
            spanSizeLookip.isSpanIndexCacheEnabled = false
            layoutManager.spanSizeLookup = spanSizeLookip
            sponsor_list.layoutManager = layoutManager
        }
    }

    inner class Empty(val spanCount: Int)
}