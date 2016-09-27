package co.touchlab.droidconandroid.tasks.persisted;
import android.os.Handler;
import android.os.Looper;

import co.touchlab.android.threading.tasks.BaseTaskQueue;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;

/**
 * Created by kgalligan on 8/23/15.
 */
public class BackoffRetryListener implements BaseTaskQueue.QueueListener
{
    private final Handler handler;
    int retryCount = 0;

    public BackoffRetryListener()
    {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void queueStarted(BaseTaskQueue queue)
    {

    }

    @Override
    public void queueFinished(final BaseTaskQueue queue)
    {
        if(retryCount < 8 && queue.countTasks() > 0)
        {
            retryCount++;
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    ((PersistedTaskQueue)queue).restartQueue();
                }
            }, 5000 * (2 ^ retryCount));
        }
        else
        {
            retryCount = 0;
        }
    }

    @Override
    public void taskStarted(BaseTaskQueue queue, Task task)
    {

    }

    @Override
    public void taskFinished(BaseTaskQueue queue, Task task)
    {

    }
}
