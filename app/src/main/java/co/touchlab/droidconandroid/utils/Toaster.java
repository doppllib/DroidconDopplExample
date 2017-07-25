package co.touchlab.droidconandroid.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kgalligan on 7/12/14.
 */
public class Toaster
{
    public static void showMessage(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showMessage(Context context, int message)
    {
        showMessage(context, context.getString(message));
    }

}
