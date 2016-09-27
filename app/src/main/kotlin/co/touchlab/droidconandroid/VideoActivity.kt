package co.touchlab.droidconandroid

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import co.touchlab.droidconandroid.presenter.VideoPlayerHost
import co.touchlab.droidconandroid.presenter.VideoPlayerPresenter
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity(), VideoPlayerEvents.OnFullscreenListener, VideoPlayerHost {

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
        if (!TextUtils.isEmpty(link)) {
            val builder = PlaylistItem.Builder().file(link)

            if(!TextUtils.isEmpty(cover))
                builder.image(cover)

            jwplayer.load(builder.build())
        } else {
            finish()
        }

        presenter = VideoPlayerPresenter(this, this, eventId)
    }

    override fun onResume() {
        // Let JW Player know that the app has returned from the background
        super.onResume()
        jwplayer.onResume()

        presenter!!.startChecking()
    }

    override fun onPause() {
        presenter!!.stopChecking()

        // Let JW Player know that the app is going to the background
        jwplayer.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        presenter!!.unregister()
        // Let JW Player know that the app is being destroyed
        jwplayer.onDestroy()
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // Set fullscreen when the device is rotated to landscape
        jwplayer.setFullscreen(newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE, true)
        super.onConfigurationChanged(newConfig)
    }

    override fun onFullscreen(state: Boolean) {
        if (state) {
            actionBar.hide()
        } else {
            actionBar.show()
        }
    }
}
