package co.touchlab.droidconandroid.shared.viewmodel;

/**
 * Created by kgalligan on 4/6/16.
 */
public interface PlatformClient
{
    /**
     * Url for droidcon server
     * @return
     */
    String baseUrl();

    /**
     * ID of convention for app. Different by flavor.
     * @return
     */
    Integer getConventionId();

    /**
     * Crash logging. Writes but doesn't push remotely.
     * @param s
     */
    void log(String s);

    /**
     * Crash logging. Should push remotely for caught exceptions.
     * @param t
     */
    void logException(Throwable t);

    /**
     * Analytics event.
     * @param name
     * @param params
     */
    void logEvent(String name, String... params);

    /**
     * Get a string value for a given id. iOS and Android have slightly different methods of storing
     * string resources, but implement the same concept.
     *
     * @param id
     * @return
     */
    String getString(String id);
}
