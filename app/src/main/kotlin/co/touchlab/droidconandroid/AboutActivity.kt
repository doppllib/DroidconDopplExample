package co.touchlab.droidconandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import co.touchlab.droidconandroid.shared.viewmodel.AppManager
import co.touchlab.droidconandroid.ui.AboutAdapter
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    companion object {
        fun callMe(activity: Activity) {
            val intent = Intent(activity, AboutActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        about_backdrop.setColorFilter(ContextCompat.getColor(this, R.color.glyph_foreground_dark))
        about_list_rv.layoutManager = LinearLayoutManager(this)

        val adapter = AboutAdapter(this, AppManager.getInstance().appComponent.prefs)
        adapter.add(R.string.about_app_header, 0, R.string.about_app)
        adapter.add(R.string.about_con_header, 0, R.string.about_con)
        adapter.add(R.string.about_touch_header, 0, R.string.about_touch)
        //Binky's row
        adapter.add(null, null, null)
        about_list_rv.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }
}