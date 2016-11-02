package co.touchlab.droidconandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_about.*
import java.util.*

class AboutActivity : AppCompatActivity()
{
    companion object
    {
        fun callMe(a: Activity)
        {
            val i = Intent(a, AboutActivity::class.java)
            a.startActivity(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        about_backdrop.setColorFilter(ContextCompat.getColor(this, R.color.glyph_foreground_dark))

        about_list !!.layoutManager = LinearLayoutManager(this)

        var adapter = AboutAdapter()

        adapter.add(R.string.about_app_header, 0, R.string.about_app)
        adapter.add(R.string.about_con_header, 0, R.string.about_con)
        adapter.add(R.string.about_touch_header, 0, R.string.about_touch)

        about_list !!.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    inner class AboutAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {
        private var dataset = ArrayList<AboutItem>()

        fun add(headerRes: Int, logoRes: Int, bodyRes: Int)
        {
            dataset.add(AboutItem(headerRes, logoRes, bodyRes))
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
            var view = LayoutInflater.from(this@AboutActivity).inflate(R.layout.item_about, parent, false)
            return AboutVH(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            var vh = holder as AboutVH
            var data = dataset.get(position)
            vh.body!!.setText(data.bodyRes)
            vh.logo!!.setImageResource(data.logoRes)
            vh.header!!.setText(data.headerRes)
            vh.body!!.setMaxLines(Int.MAX_VALUE)
        }

        override fun getItemCount(): Int {
            return dataset.size
        }

        inner class AboutVH(val item: View): RecyclerView.ViewHolder(item)
        {
            var header: TextView? = null
            var logo: ImageView? = null
            var body: TextView? = null
            init
            {
                header = item.findViewById(R.id.header) as TextView
                logo = item.findViewById(R.id.logo) as ImageView
                body = item.findViewById(R.id.body) as TextView
            }
        }

        inner class AboutItem(val headerRes: Int, val logoRes: Int, val bodyRes: Int)
    }
}