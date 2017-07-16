package co.touchlab.droidconandroid

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import co.touchlab.droidconandroid.EventDetailFragment.Companion.EXTRA_EVENT_ID
import co.touchlab.droidconandroid.EventDetailFragment.Companion.EXTRA_STREAM_COVER
import co.touchlab.droidconandroid.EventDetailFragment.Companion.EXTRA_STREAM_LINK
import co.touchlab.droidconandroid.shared.presenter.VideoPlayerHost
import co.touchlab.droidconandroid.shared.presenter.VideoPlayerPresenter

class VideoActivity : AppCompatActivity(), VideoPlayerHost {

    override fun shutDownForce(s: String?) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
        finish()
    }

    private var presenter: VideoPlayerPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        val link = intent.getStringExtra(EXTRA_STREAM_LINK)
        val cover = intent.getStringExtra(EXTRA_STREAM_COVER)
        val eventId = intent.getLongExtra(EXTRA_EVENT_ID, -1)
        if (!link.isNullOrBlank()) {
//            val builder = PlaylistItem.Builder().file(link)
//
//            if(!TextUtils.isEmpty(cover))
//                builder.image(cover)

//            jwplayer.load(builder.build())
        } else {
            finish()
        }

        presenter = VideoPlayerPresenter(this, this, eventId)
    }

    override fun onResume() {
        // Let JW Player know that the app has returned from the background
        super.onResume()
//        jwplayer.onResume()

//        presenter!!.startChecking()
    }

    override fun onPause() {
//        presenter!!.stopChecking()

        // Let JW Player know that the app is going to the background
//        jwplayer.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        presenter!!.unregister()
        // Let JW Player know that the app is being destroyed
//        jwplayer.onDestroy()
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // Set fullscreen when the device is rotated to landscape
//        jwplayer.setFullscreen(newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE, true)
        super.onConfigurationChanged(newConfig)
    }
}
