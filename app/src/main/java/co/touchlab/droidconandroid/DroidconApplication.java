package co.touchlab.droidconandroid;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import co.touchlab.droidconandroid.alerts.AlertManagerKt;
import co.touchlab.droidconandroid.shared.dagger.AppModule;
import co.touchlab.droidconandroid.shared.dagger.DaggerAppComponent;
import co.touchlab.droidconandroid.shared.dagger.DatabaseModule;
import co.touchlab.droidconandroid.shared.dagger.NetworkModule;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.interactors.UpdateAlertsInteractor;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import co.touchlab.droidconandroid.shared.presenter.LoadDataSeed;
import co.touchlab.droidconandroid.shared.presenter.PlatformClient;
import co.touchlab.droidconandroid.shared.utils.EventBusExt;
import co.touchlab.droidconandroid.shared.utils.IOUtils;

/**
 * Created by kgalligan on 6/28/14.
 */
public class DroidconApplication extends Application
{
    private static DroidconApplication instance;

    public DroidconApplication()
    {
        instance = this;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        String currentProcessName = getCurrentProcessName(this);
        Log.i(DroidconApplication.class.getSimpleName(),
                "currentProcessName: " + currentProcessName);

        EventBusExt.getDefault().register(this);

        if(! currentProcessName.contains("background_crash"))
        {
            PlatformClient platformClient = new PlatformClient()
            {
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
                    StringBuilder sb = new StringBuilder();
                    for(String param : params)
                    {
                        sb.append(param);
                        sb.append("; ");
                    }
                    Log.i(name, sb.toString());
                }

                @Override
                public String getString(String id)
                {
                    return DroidconApplication.this.getString(getResources().getIdentifier(id,
                            "string",
                            getPackageName()));
                }
            };

            LoadDataSeed loadDataSeed = () ->
            {
                try
                {
                    return IOUtils.toString(getAssets().open("dataseed.json"));
                }
                catch(IOException e)
                {
                    throw new RuntimeException(e);
                }
            };

            AppManager.create(this,
                    platformClient,
                    DaggerAppComponent.builder()
                            .appModule(new AppModule(this))
                            .databaseModule(new DatabaseModule())
                            .networkModule(new NetworkModule())
                            .build());

            AppManager.getInstance().seed(loadDataSeed);

        }
    }

    public static String getCurrentProcessName(Context context)
    {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses())
        {
            if(processInfo.pid == pid)
            {
                return processInfo.processName;
            }
        }
        return "";
    }

    public static DroidconApplication getInstance()
    {
        return instance;
    }

    public void onEventMainThread(UpdateAlertsInteractor interactor)
    {
        update(interactor.event);
    }

    public void update(Event nextEvent)
    {
        AlertManagerKt.scheduleAlert(this, nextEvent);
    }
}
