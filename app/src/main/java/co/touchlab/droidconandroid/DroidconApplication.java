package co.touchlab.droidconandroid;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.droidconandroid.alerts.AlertManagerKt;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import co.touchlab.droidconandroid.shared.presenter.PlatformClient;
import co.touchlab.droidconandroid.shared.tasks.UpdateAlertsInteractor;
import co.touchlab.droidconandroid.shared.utils.IOUtils;
import io.reactivex.Observable;
import retrofit.client.Client;

/**
 * Created by kgalligan on 6/28/14.
 */
public class DroidconApplication extends Application
{

    private Observable<Event> nextEvent;

    public static String getCurrentProcessName(Context context) {
        // Log.d(TAG, "getCurrentProcessName");
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses())
        {
            // Log.d(TAG, processInfo.processName);
            if (processInfo.pid == pid)
                return processInfo.processName;
        }
        return "";
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        EventBusExt.getDefault().register(this);

        String currentProcessName = getCurrentProcessName(this);
        Log.i(DroidconApplication.class.getSimpleName(), "currentProcessName: "+ currentProcessName );

        DatabaseHelper helper = DatabaseHelper.getInstance(this);
        AppPrefs appPrefs = AppPrefs.getInstance(this);

        UpdateAlertsInteractor alertsInteractor = new UpdateAlertsInteractor(helper, appPrefs);

        if(!currentProcessName.contains("background_crash"))
        {
            PlatformClient platformClient = new co.touchlab.droidconandroid.shared.presenter.PlatformClient()
            {
                @Override
                public Client makeClient()
                {
                    return null;
                }

                @Override
                public String baseUrl()
                {
                    return BuildConfig.BASE_URL;
                }

                @Override
                public Integer getConventionId()
                {
                    return Integer.parseInt(DroidconApplication.this.getString(R.string.convention_id));
                }

                @Override
                public void log(String s)
                {
                    Log.i(DroidconApplication.class.getSimpleName(), s);
                }

                @Override
                public void logException(Throwable t)
                {
                    Log.e(DroidconApplication.class.getSimpleName(), "", t);
                }

                @Override
                public void logEvent(String name, String... params)
                {

                }

                @Override
                public String getString(String id)
                {
                    return DroidconApplication.this.getString(
                            getResources().getIdentifier(id, "string", getPackageName()));
                }
            };

            AppManager.initContext(this, platformClient, () -> {
                try
                {
                    return IOUtils.toString(getAssets().open("dataseed.json"));
                }
                catch(IOException e)
                {
                    throw new RuntimeException(e);
                }
            });

        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(UpdateAlertsInteractor task)
    {
        AlertManagerKt.scheduleAlert(this, task.nextEvent);
    }
}
