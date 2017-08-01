package co.touchlab.droidconandroid.shared.presenter;
import co.touchlab.droidconandroid.shared.dagger.AppComponent;

/**
 * Created by kgalligan on 4/19/16.
 */
public class AppManager
{
    public static final String FIRST_SEED = "FIRST_SEED";

    private static AppManager     instance;
    private        AppComponent   appComponent;
    private        PlatformClient platformClient;

    private AppManager(AppComponent appComponent, PlatformClient platformClient)
    {
        this.appComponent = appComponent;
        this.platformClient = platformClient;
    }

    public static AppManager getInstance()
    {
        return instance;
    }

    public static void create(AppComponent appComponent, PlatformClient platformClient)
    {
        if(instance == null)
        {
            instance = new AppManager(appComponent, platformClient);
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

    public enum AppScreens
    {
        Welcome,
        Schedule
    }

}
