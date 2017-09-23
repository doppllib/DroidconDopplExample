package co.touchlab.droidconandroid;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;

import co.touchlab.droidconandroid.alerts.EventNotificationsManager;
import io.fabric.sdk.android.Fabric;
import java.io.IOException;

import co.touchlab.droidconandroid.shared.dagger.AppModule;
import co.touchlab.droidconandroid.shared.dagger.DaggerAppComponent;
import co.touchlab.droidconandroid.shared.dagger.NetworkModule;
import co.touchlab.droidconandroid.shared.viewmodel.AppManager;
import co.touchlab.droidconandroid.shared.viewmodel.LoadDataSeed;
import co.touchlab.droidconandroid.shared.viewmodel.PlatformClient;
import co.touchlab.droidconandroid.shared.utils.IOUtils;

/**
 * Created by kgalligan on 6/28/14.
 */
public class DroidconApplication extends Application
{
    private static DroidconApplication instance;
    private EventNotificationsManager eventNotificationsManager;

    public DroidconApplication()
    {
        instance = this;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        String currentProcessName = getCurrentProcessName(this);
        Log.i(DroidconApplication.class.getSimpleName(),
                "currentProcessName: " + currentProcessName);

        /*if (DEVELOPER_MODE) */{
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyFlashScreen()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        Log.w("regtoken", "reg: "+ FirebaseInstanceId.getInstance().getToken());

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
                    Crashlytics.logException(t);
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

            AppModule appModule = new AppModule(this);
            NetworkModule networkModule = new NetworkModule();
            AppManager.create(this,
                    platformClient,
                    DaggerAppComponent.builder()
                            .appModule(appModule)
                            .networkModule(networkModule)
                            .build());

            AppManager instance = AppManager.getInstance();
            instance.seed(loadDataSeed);

            eventNotificationsManager = new EventNotificationsManager(instance.getAppComponent().getPrefs(),
                    this,
                    instance.getAppComponent().refreshScheduleInteractor());
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

    public void updateEventNotifications()
    {
        eventNotificationsManager.resetObserver();
    }
}
