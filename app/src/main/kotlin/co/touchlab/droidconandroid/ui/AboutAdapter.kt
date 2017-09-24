package co.touchlab.droidconandroid.ui

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.droidconandroid.R
import kotlinx.android.synthetic.main.item_about.view.*
import java.util.ArrayList
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import co.touchlab.droidconandroid.shared.data.AppPrefs


/**
 * Created by sufeizhao on 7/11/17.
 */
class AboutAdapter(private val context: Context, private val appPrefs: AppPrefs) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataset = ArrayList<AboutItem>()

    fun add(headerRes: Int?, logoRes: Int?, bodyRes: Int?) {
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
        if(data.bodyRes == null) {

            view.aboutBinkyBody.visibility = View.VISIBLE
            view.aboutTextBody.visibility = View.GONE
            if (appPrefs.dogClicked) {
                clearDogClick(view)
            } else {
                view.aboutBinkyBody.setOnClickListener {
                    clearDogClick(view)
                    appPrefs.dogClicked = true
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://medium.com/@kpgalligan/whos-binky-aa27f0e400a1"))
                    if (context is Activity)
                        (context as Activity).startActivity(browserIntent)
                }
            }
        }
        else
        {
            view.aboutBinkyBody.visibility = View.GONE
            view.aboutTextBody.visibility = View.VISIBLE
            view.body.setText(data.bodyRes)
            if(data.logoRes == null)
            {
                view.logo.setImageDrawable(null)
            }
            else {
                view.logo.setImageResource(data.logoRes)
            }
            if(data.headerRes == null)
            {
                view.header.text = ""
            }
            else {
                view.header.setText(data.headerRes)
            }
            view.body.maxLines = Int.MAX_VALUE
        }

    }

    private fun clearDogClick(view: View) {
        view.aboutBinkyBody.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class AboutVH(val item: View) : RecyclerView.ViewHolder(item)

    inner class AboutItem(val headerRes: Int?, val logoRes: Int?, val bodyRes: Int?)
}