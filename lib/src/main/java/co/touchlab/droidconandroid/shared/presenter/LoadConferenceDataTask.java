package co.touchlab.droidconandroid.shared.presenter;
import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by kgalligan on 4/17/16.
 */
public class LoadConferenceDataTask extends Task
{
    public final boolean               allEvents;
    public       ConferenceDayHolder[] conferenceDayHolders;

    public LoadConferenceDataTask(boolean allEvents)
    {
        this.allEvents = allEvents;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        conferenceDayHolders = ConferenceDataHelper.listDays(context, allEvents);
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
