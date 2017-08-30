package co.touchlab.droidconandroid.shared.utils;

/**
 * Created by kgalligan on 10/29/16.
 */

public class StringUtils
{
    public static String trimToEmpty(String s)
    {
        if(s == null)
            return "";
        else
            return s.trim();
    }
}
