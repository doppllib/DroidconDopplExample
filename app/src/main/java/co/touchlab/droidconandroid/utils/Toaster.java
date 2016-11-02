package co.touchlab.droidconandroid.utils;

import android.content.Context;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

/**
 * Created by kgalligan on 7/12/14.
 */
public class Toaster
{
    public static void showMessage(Context context, String message)
    {
        SuperToast.create(context, message, SuperToast.Duration.LONG,
            Style.getStyle(Style.GREEN, SuperToast.Animations.FLYIN)).show();
    }

    public static void showMessage(Context context, int message)
    {
        showMessage(context, context.getString(message));
    }

}
