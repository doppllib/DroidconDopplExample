package co.touchlab.droidconandroid.shared.presenter;
import android.content.Context;
import android.os.Handler;

import com.google.j2objc.annotations.Weak;

/**
 * Created by kgalligan on 9/16/16.
 */
public class VideoPlayerPresenter
{
    @Weak
    final VideoPlayerHost host;
    private final Handler handler;
    private final long eventId;
    int checkCount;

    public VideoPlayerPresenter(Context context, VideoPlayerHost host, long eventId)
    {
        this.host = host;
        this.eventId = eventId;
        handler = new Handler();
    }

    public void unregister() {

    }
}
