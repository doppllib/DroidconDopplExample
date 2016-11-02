package co.touchlab.droidconandroid;
import co.touchlab.droidconandroid.presenter.AppManager;

/**
 * Created by kgalligan on 4/6/16.
 */
public class CrashReport
{
    public static void logException(Throwable t)
    {
        AppManager.getPlatformClient().logException(t);
    }
}
