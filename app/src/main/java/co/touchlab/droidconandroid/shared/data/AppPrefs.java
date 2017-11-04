package co.touchlab.droidconandroid.shared.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

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
    public static final String CONVENTION_END   = "convention_end";
    public static final String REFRESH_TIME     = "refresh_time2";
    public static final String USER_UNIQUE_UUID = "USER_UNIQUE_UUID";
    public static final String DOG_CLICKED = "DOG_CLICKED";

    private SharedPreferences prefs;

    private final BehaviorSubject<Pair<String, String>> conventionDateProcessor;
    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Inject
    public AppPrefs(Application context)
    {
        prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener()
        {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
            {
                if(s.equals(CONVENTION_START) || s.equals(CONVENTION_END))
                {
                    conventionDateProcessor.onNext(new Pair<>(prefs.getString(CONVENTION_START, null),
                            prefs.getString(CONVENTION_END, null)));
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        //We're depending on seeded data to properly init the screen.
        if(prefs.getString(CONVENTION_START, null) == null)
        {
            conventionDateProcessor = BehaviorSubject.create();
        }
        else
        {
            Pair<String, String> defaultValue = new Pair<>(prefs.getString(CONVENTION_START, null),
                    prefs.getString(CONVENTION_END, null));
            conventionDateProcessor = BehaviorSubject.createDefault(defaultValue);
        }
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

    public void setConventionDates(@NonNull String startDate, @NonNull String endDate)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CONVENTION_START, startDate);
        editor.putString(CONVENTION_END, endDate);
        editor.apply();
    }

    /**
     * Observe initial and changed values (which should basically never happen)
     * @return
     */
    public Observable<Pair<String, String>> observeConventionDates()
    {
        return conventionDateProcessor.distinctUntilChanged();
    }

    public void setRefreshTime(long time)
    {
        setLong(REFRESH_TIME, time);
    }

    public long getRefreshTime()
    {
        return prefs.getLong(REFRESH_TIME, 0);
    }

    public boolean getDogClicked()
    {
        return prefs.getBoolean(DOG_CLICKED, false);
    }

    public void setDogClicked(boolean b)
    {
        prefs.edit().putBoolean(DOG_CLICKED, b).apply();
    }

    //helper methods
    private void setBoolean(String key, Boolean value)
    {
        prefs.edit().putBoolean(key, value).apply();
    }

    private void setLong(String key, Long value)
    {
        prefs.edit().putLong(key, value).apply();
    }

    public String getUserUniqueUuid()
    {
        String uuid = prefs.getString(USER_UNIQUE_UUID, null);
        if(uuid == null)
        {
            prefs.edit().putString(USER_UNIQUE_UUID, UUID.randomUUID().toString()).apply();
        }
        return prefs.getString(USER_UNIQUE_UUID, null);
    }

}
