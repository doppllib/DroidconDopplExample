package co.touchlab.droidconandroid.shared.utils;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.presenter.AppManager;

/**
 * Created by sufeizhao on 7/19/17.
 */

public class AnalyticsHelper
{
    public static void recordAnalytics(String analyticsKey)
    {
        // Do nothing for now
    }

    public static void recordAnalytics(String analyticsKey, long eventId)
    {
        AppManager.getInstance().getPlatformClient()
                .logEvent(analyticsKey, AnalyticsEvents.PARAM_ITEM_ID, Long.toString(eventId));
    }

    public static void recordAnalytics(String analyticsKey, Event event)
    {
        String eventName = event != null ? StringUtils.trimToEmpty(event.getName()) : "";
        AppManager.getInstance().getPlatformClient()
                .logEvent(analyticsKey,
                        AnalyticsEvents.PARAM_ITEM_ID,
                        Long.toString(event.getId()),
                        AnalyticsEvents.PARAM_ITEM_NAME,
                        eventName);
    }
}
