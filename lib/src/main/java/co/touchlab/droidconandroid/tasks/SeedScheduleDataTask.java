package co.touchlab.droidconandroid.tasks;
import android.content.Context;

import com.google.gson.Gson;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.network.dao.Convention;
import co.touchlab.droidconandroid.tasks.persisted.RefreshScheduleData;

/**
 * Created by kgalligan on 4/7/16.
 */
public class SeedScheduleDataTask extends Task
{
    private final String dataseed;

    public SeedScheduleDataTask(String dataseed)
    {
        this.dataseed = dataseed;
    }

    @Override
    protected boolean handleError(Context context, Throwable e)
    {
        CrashReport.logException(e);
        return false;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        RefreshScheduleData.saveConventionData(context,
                                               new Gson().fromJson(dataseed, Convention.class));
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
    }
}
