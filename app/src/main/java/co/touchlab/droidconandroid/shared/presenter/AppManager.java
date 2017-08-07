package co.touchlab.droidconandroid.shared.presenter;
import android.content.Context;

import co.touchlab.droidconandroid.shared.dagger.AppComponent;
import co.touchlab.droidconandroid.shared.interactors.SeedInteractor;

/**
 * Created by kgalligan on 4/19/16.
 */
public class AppManager
{
    public static final String FIRST_SEED = "FIRST_SEED";

    private static AppManager     instance;
    private        AppComponent   appComponent;
    private        PlatformClient platformClient;

    private AppManager(Context context, PlatformClient platformClient, AppComponent appComponent)
    {
        this.appComponent = appComponent;
        this.platformClient = platformClient;
    }

    public static AppManager getInstance()
    {
        return instance;
    }

    public static void create(Context context, PlatformClient platformClient, AppComponent appComponent)
    {
        if(instance == null)
        {
            instance = new AppManager(context, platformClient, appComponent);
        }
    }

    public void seed(LoadDataSeed loadDataSeed)
    {
        SeedInteractor seedInteractor = appComponent.seedInteractor();
        seedInteractor.seedDatabase(loadDataSeed);
    }


    public AppComponent getAppComponent()
    {
        return appComponent;
    }

    public PlatformClient getPlatformClient()
    {
        return platformClient;
    }



}
