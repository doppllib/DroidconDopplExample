package co.touchlab.droidconandroid.shared.presenter;

import co.touchlab.droidconandroid.shared.data2.Event;
import co.touchlab.droidconandroid.shared.data2.EventInfo;

/**
 * Created by kgalligan on 4/25/16.
 */
public interface EventDetailHost
{
    void dataRefresh(EventInfo eventInfo);
    void reportError(String error);
    void updateRsvp(Event event);
}
