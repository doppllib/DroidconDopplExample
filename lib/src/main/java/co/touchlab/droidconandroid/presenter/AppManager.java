package co.touchlab.droidconandroid.presenter;
import android.content.Context;

import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.droidconandroid.data.AppPrefs;
import co.touchlab.droidconandroid.tasks.SeedScheduleDataTask;

/**
 * Created by kgalligan on 4/19/16.
 */
public class AppManager
{
    public static final String FIRST_SEED = "FIRST_SEED";

    private static Context context;
    private static PlatformClient platformClient;

    public interface LoadDataSeed
    {
        String dataSeed();
    }

    public static void initContext(Context context, PlatformClient platformClient, LoadDataSeed loadDataSeed)
    {
        AppManager.context = context;
        AppManager.platformClient = platformClient;

        if(AppPrefs.getInstance(context).once(FIRST_SEED))
        {
            final String seed = loadDataSeed.dataSeed();
            TaskQueue.loadQueueDefault(context).execute(new SeedScheduleDataTask(seed));
        }
    }

    public static Context getContext()
    {
        return context;
    }

    public static PlatformClient getPlatformClient()
    {
        return platformClient;
    }

    public enum AppScreens
    {
        Welcome, Schedule
    }

    public static AppScreens findStartScreen()
    {
        final AppPrefs appPrefs = AppPrefs.getInstance(context);
        boolean hasSeenWelcome = appPrefs.getHasSeenWelcome();
        if (! hasSeenWelcome)
        {
            return AppScreens.Welcome;
        }
        else
        {
            return AppScreens.Schedule;
        }
    }

    public static AppPrefs getAppPrefs()
    {
        return AppPrefs.getInstance(context);
    }
}
