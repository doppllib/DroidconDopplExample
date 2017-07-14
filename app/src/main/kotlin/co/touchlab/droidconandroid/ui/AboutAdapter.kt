package co.touchlab.droidconandroid.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.droidconandroid.R
import kotlinx.android.synthetic.main.item_about.view.*
import java.util.ArrayList

/**
 * Created by sufeizhao on 7/11/17.
 */
class AboutAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataset = ArrayList<AboutItem>()

    fun add(headerRes: Int, logoRes: Int, bodyRes: Int) {
        dataset.add(AboutItem(headerRes, logoRes, bodyRes))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val view = LayoutInflater.from(context).inflate(R.layout.item_about, parent, false)
        return AboutVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val view = (holder as AboutVH).itemView
        val data = dataset[position]
        view.body.setText(data.bodyRes)
        view.logo.setImageResource(data.logoRes)
        view.header.setText(data.headerRes)
        view.body.maxLines = Int.MAX_VALUE
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class AboutVH(val item: View) : RecyclerView.ViewHolder(item)

    inner class AboutItem(val headerRes: Int, val logoRes: Int, val bodyRes: Int)
}