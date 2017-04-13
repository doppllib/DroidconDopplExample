package co.touchlab.droidconandroid.shared.presenter;
import android.content.Context;
import android.os.Handler;

import com.google.j2objc.annotations.Weak;

import java.util.Random;

import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.droidconandroid.shared.tasks.CheckWatchVideoTask;

/**
 * Created by kgalligan on 9/16/16.
 */
public class VideoPlayerPresenter extends AbstractEventBusPresenter
{
    @Weak
    final VideoPlayerHost host;
    private final Handler handler;
    private final long eventId;
    int checkCount;
    private final CheckVideoRunnable checkVideoRunnable = new CheckVideoRunnable();

    public VideoPlayerPresenter(Context context, VideoPlayerHost host, long eventId)
    {
        super(context);
        this.host = host;
        this.eventId = eventId;
        handler = new Handler();
    }

    class CheckVideoRunnable implements Runnable
    {
        @Override
        public void run()
        {
            TaskQueue.loadQueueNetwork(getContext()).execute(new CheckWatchVideoTask(eventId));
            checkCount++;
        }
    }

    @Override
    public void unregister()
    {
        stopChecking();
        super.unregister();
    }

    public void onEventMainThread(CheckWatchVideoTask task)
    {
        if(task.videoOk)
        {
            checkWatchVideoDelayed();
        }
        else
        {
            host.shutDownForce("Another device watching video");
        }
    }

    private void checkWatchVideoDelayed() {
        long waitLength = 5 * 60 * 1000;

        if(checkCount == 0)
        {
            waitLength = (new Random().nextInt(30) + 20) * 1000;
        }
        handler.postDelayed(checkVideoRunnable, waitLength);
    }

    public void stopChecking()
    {
        handler.removeCallbacks(checkVideoRunnable);
    }

    public void startChecking()
    {
        stopChecking();
        checkWatchVideoDelayed();
    }
}
