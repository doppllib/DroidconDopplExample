package co.touchlab.droidconandroid.shared.presenter;
import android.content.Context;

import com.google.gson.Gson;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.dagger.AppComponent;
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

    private static Context        context;
    private static PlatformClient platformClient;
    private static AppManager     instance;
    private        AppComponent   appComponent;
    private        PlatformClient mPlatformClient;
    private        SeedInteractor seedInteractor;

    private AppManager(AppComponent appComponent, PlatformClient platformClient)
    {
        this.appComponent = appComponent;
        this.mPlatformClient = platformClient;
    }

    public static AppManager getInstance(AppComponent appComponent, PlatformClient platformClient)
    {
        if(instance == null)
        {
            instance = new AppManager(appComponent, platformClient);
        }
        return instance;
    }

    public void seed(LoadDataSeed loadDataSeed)
    {
        SeedInteractor seedInteractor = appComponent.seedInteractor();
        seedInteractor.seedDatabase(loadDataSeed);
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

    public PlatformClient getmPlatformClient()
    {
        return mPlatformClient;
    }

    public enum AppScreens
    {
        Welcome,
        Schedule
    }

    // TODO: May be able to replace this functionality by putting it in a ViewModel, injecting AppPrefs
    public static AppScreens findStartScreen()
    {
        final AppPrefs appPrefs = AppPrefs.getInstance(context);
        boolean hasSeenWelcome = appPrefs.getHasSeenWelcome();
        if(! hasSeenWelcome)
        {
            return AppScreens.Welcome;
        }
        else
        {
            return AppScreens.Schedule;
        }
    }

}
