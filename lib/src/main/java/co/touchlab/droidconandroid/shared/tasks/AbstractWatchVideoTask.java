package co.touchlab.droidconandroid.shared.tasks;
import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.network.DataHelper;
import co.touchlab.droidconandroid.shared.network.WatchVideoRequest;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kgalligan on 8/17/16.
 */
public abstract class AbstractWatchVideoTask extends Task
{
    public boolean videoOk;
    public boolean unauthorized;

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        RestAdapter restAdapter = DataHelper.makeRequestAdapter(context,
                AppManager.getPlatformClient());
        WatchVideoRequest watchVideoRequest = restAdapter.create(WatchVideoRequest.class);
        AppPrefs appPrefs = AppPrefs.getInstance(context);
        String email = appPrefs.getEventbriteEmail();
        String uuid = appPrefs.getVideoDeviceId();
        boolean checkVideo = this instanceof CheckWatchVideoTask;
        if(checkVideo)
        {
            //If we're just checking, always be OK unless its 401
            //If starting, only OK if explicitly 200
            videoOk = true;
        }

        long conventionId = AppManager.getPlatformClient().getConventionId().longValue();
        try
        {
            Response response = callVideoUrl(watchVideoRequest, email, uuid, conventionId);
            videoOk = response.getStatus() == 200;
        }
        catch(Exception e)
        {
            CrashReport.logException(e);

            if(e instanceof RetrofitError)
            {
                RetrofitError retrofitError = (RetrofitError) e;
                int errorStatus = retrofitError.getResponse().getStatus();
                unauthorized = errorStatus == 401;

                if(checkVideo && errorStatus == 401)
                    videoOk = false;
            }
        }
    }

    abstract Response callVideoUrl(WatchVideoRequest watchVideoRequest, String email, String uuid, long conventionId);

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        CrashReport.logException(throwable);
        return true;
    }
}
