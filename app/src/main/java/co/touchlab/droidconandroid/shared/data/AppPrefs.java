package co.touchlab.droidconandroid.shared.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Created by kgalligan on 6/28/14.
 */
@Singleton
public class AppPrefs
{
    public static final String SEEN_WELCOME     = "seen_welcome";
    public static final String CONVENTION_START = "convention_start";
    public static final String CONVENTION_END  = "convention_end";
    public static final String REFRESH_TIME    = "refresh_time";
    public static final String ALLOW_NOTIFS    = "allow_notifs";
    public static final String SHOW_NOTIF_CARD = "show_notif_card";

    private SharedPreferences prefs;

    @Inject
    public AppPrefs(Context context)
    {
        prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
    }

    public boolean once(String key)
    {
        Boolean shouldOnce = prefs.getBoolean(key, true);
        setBoolean(key, false);
        return shouldOnce;
    }

    public boolean getHasSeenWelcome()
    {
        return prefs.getBoolean(SEEN_WELCOME, false);
    }

    public void setHasSeenWelcome()
    {
        setBoolean(SEEN_WELCOME, true);
    }

    public void setConventionStartDate(@NotNull String startDate)
    {
        setString(CONVENTION_START, startDate);
    }

    public String getConventionStartDate()
    {
        return prefs.getString(CONVENTION_START, null);
    }

    public void setConventionEndDate(@NotNull String endDate)
    {
        setString(CONVENTION_END, endDate);
    }

    public String getConventionEndDate()
    {
        return prefs.getString(CONVENTION_END, null);
    }

    public void setRefreshTime(long time)
    {
        setLong(REFRESH_TIME, time);
    }

    public long getRefreshTime()
    {
        return prefs.getLong(REFRESH_TIME, 0);
    }

    public boolean getAllowNotifications(){return prefs.getBoolean(ALLOW_NOTIFS, false);}
    public void setAllowNotifications(boolean allow){prefs.edit().putBoolean(ALLOW_NOTIFS, allow).apply();}

    public boolean getShowNotifCard(){return prefs.getBoolean(SHOW_NOTIF_CARD, true);}
    public void setShowNotifCard(boolean show){prefs.edit().putBoolean(SHOW_NOTIF_CARD, show).apply();}

    //helper methods
    private void setBoolean(String key, Boolean value)
    {
        prefs.edit().putBoolean(key, value).apply();
    }

    private void setString(String key, String value)
    {
        prefs.edit().putString(key, value).apply();
    }

    private void setLong(String key, Long value)
    {
        prefs.edit().putLong(key, value).apply();
    }
}
