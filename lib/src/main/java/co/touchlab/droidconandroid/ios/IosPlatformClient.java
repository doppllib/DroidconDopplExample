package co.touchlab.droidconandroid.ios;
import com.google.j2objc.annotations.ObjectiveCName;

import java.io.PrintWriter;
import java.io.StringWriter;

import co.touchlab.droidconandroid.presenter.PlatformClient;
import retrofit.client.Client;

/**
 * Created by kgalligan on 4/10/16.
 */
@ObjectiveCName("DCIosPlatformClient")
public class IosPlatformClient implements PlatformClient
{
    private final IosFirebase iosFirebase;

    public IosPlatformClient(IosFirebase iosFirebase)
    {
        this.iosFirebase = iosFirebase;
    }

    @Override
    public Client makeClient()
    {
        return null;
    }

    @Override
    public String baseUrl()
    {
        return "https://droidcon-server.herokuapp.com/";
    }

    @Override
    public Integer getConventionId()
    {
        return 61100;
    }

    @Override
    public void log(String s)
    {
        iosFirebase.logFirebaseNative(s);
    }

//    private native void logFirebaseNative(String s)/*-[
//    FIRCrashLog(s);
//    ]-*/;

    @Override
    public void logException(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.close();
        iosFirebase.logPushFirebaseNative(sw.toString());
    }

    @Override
    public void logEvent(String name, String... params)
    {
        iosFirebase.logEvent(name, params);
    }

    //    private native void logPushFirebaseNative(String s)/*-[
//    FIRCrashLog(s);
//    assert(NO);
//    ]-*/;

    @Override
    public native String getString(String id)/*-[
    return [[NSBundle mainBundle] objectForInfoDictionaryKey:id_];
    ]-*/;
    /*{
        return null;
    }*/
}
