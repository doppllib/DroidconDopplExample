package co.touchlab.droidconandroid.utils;
import android.text.TextUtils;

/**
 * Created by kgalligan on 10/29/16.
 */

public class StringUtils
{
    public static boolean isEmpty(String s)
    {
        return TextUtils.isEmpty(s);
    }

    public static boolean isNotEmpty(String s)
    {
        return !TextUtils.isEmpty(s);
    }

    public static String trimToEmpty(String s)
    {
        if(s == null)
            return "";
        else
            return s.trim();
    }
}
