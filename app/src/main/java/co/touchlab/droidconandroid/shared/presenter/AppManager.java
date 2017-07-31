package co.touchlab.droidconandroid.shared.presenter;
import android.content.Context;

import com.google.gson.Gson;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.network.dao.Convention;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        AppPrefs appPrefs = AppPrefs.getInstance(context);
        if(appPrefs.once(FIRST_SEED))
        {
            try
            {
                final String seed = loadDataSeed.dataSeed();
                ConferenceDataHelper.saveConvention(helper,
                        appPrefs,
                        new Gson().fromJson(seed, Convention.class))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() ->
                        {
                        }, CrashReport:: logException);
            }
            catch(RuntimeException e)
            {
                CrashReport.logException(e);
            }
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
