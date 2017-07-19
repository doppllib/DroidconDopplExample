package co.touchlab.droidconandroid;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;

import co.touchlab.droidconandroid.shared.tasks.persisted.JobQueueService;
import co.touchlab.droidconandroid.shared.utils.IOUtils;

import java.io.IOException;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.droidconandroid.alerts.AlertManagerKt;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import co.touchlab.droidconandroid.shared.presenter.PlatformClient;
import co.touchlab.droidconandroid.shared.tasks.UpdateAlertsTask;
import retrofit.client.Client;

/**
 * Created by kgalligan on 6/28/14.
 */
public class DroidconApplication extends Application
{
    private static DroidconApplication instance;
    private        JobManager          jobManager;

    public DroidconApplication()
    {
        instance = this;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        EventBusExt.getDefault().register(this);
        getJobManager();

        String currentProcessName = getCurrentProcessName(this);
        Log.i(DroidconApplication.class.getSimpleName(), "currentProcessName: "+ currentProcessName );
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

            AppManager.initContext(this, platformClient, new AppManager.LoadDataSeed()
            {
                @Override
                public String dataSeed()
                {
                    try
                    {
                        return IOUtils.toString(getAssets().open("dataseed.json"));
                    }
                    catch(IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });

        }
    }

    public static String getCurrentProcessName(Context context)
    {
        // Log.d(TAG, "getCurrentProcessName");
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses())
        {
            // Log.d(TAG, processInfo.processName);
            if(processInfo.pid == pid)
            {
                return processInfo.processName;
            }
        }
        return "";
    }

    private void configureJobManager()
    {
        Configuration.Builder builder = new Configuration.Builder(this).minConsumerCount(1)
                .maxConsumerCount(3)
                .loadFactor(5)
                .consumerKeepAlive(120) // 2 minute
                .customLogger(new CustomLogger()
                {
                    private static final String TAG = "JobManager";

                    @Override
                    public boolean isDebugEnabled()
                    {
                        return BuildConfig.DEBUG;
                    }

                    @Override
                    public void d(String text, Object... args)
                    {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args)
                    {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args)
                    {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args)
                    {

                    }
                });

        builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(this,
                JobQueueService.class), true);
        jobManager = new JobManager(builder.build());
    }

    public synchronized JobManager getJobManager()
    {
        if(jobManager == null)
        {
            configureJobManager();
        }
        return jobManager;
    }

    public static DroidconApplication getInstance()
    {
        return instance;
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(UpdateAlertsTask task)
    {
        AlertManagerKt.scheduleAlert(this, task.nextEvent);
    }
}
