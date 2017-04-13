package co.touchlab.droidconandroid.shared.tasks;
import android.content.Context;

import java.util.concurrent.Callable;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.squeaky.dao.Dao;

/**
 * Created by kgalligan on 4/8/16.
 */
public class RemoveRsvpTask extends Task
{
    private final long eventId;

    public RemoveRsvpTask(long eventId)
    {
        this.eventId = eventId;
    }

    @Override
    protected void run(final Context context) throws Throwable
    {
        DatabaseHelper.getInstance(context).performTransactionOrThrowRuntime(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                Dao<Event> dao = DatabaseHelper.getInstance(context).getEventDao();
                Event event = dao.queryForId(eventId);
                if(event != null)
                {
                    event.rsvpUuid = null;
                    dao.update(event);
                    TaskQueue.loadQueueDefault(context).execute(new UpdateAlertsTask());
                }

                return null;
            }
        });
    }

    @Override
    protected boolean handleError(Context context, Throwable e)
    {
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
    }
}
