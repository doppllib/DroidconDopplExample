package co.touchlab.droidconandroid.ios;
import com.google.j2objc.annotations.ObjectiveCName;

/**
 * Created by kgalligan on 9/5/16.
 */
@ObjectiveCName("DCIosFirebase")
public interface IosFirebase
{
    void logFirebaseNative(String s);
    void logPushFirebaseNative(String s);
    void logEvent(String name, String... params);
}
