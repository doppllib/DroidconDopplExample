package co.touchlab.droidconandroid.shared.tasks;
import java.sql.SQLException;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.network.WatchVideoRequest;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import co.touchlab.droidconandroid.shared.utils.AnalyticsEvents;
import retrofit.client.Response;

/**
 * Created by kgalligan on 8/17/16.
 */
public class StartWatchVideoTask extends AbstractWatchVideoTask
{
    private final long eventId;
    public final String link;
    public final String cover;

    public StartWatchVideoTask(long eventId, String link, String cover)
    {
        this.eventId = eventId;
        this.link = link;
        this.cover = cover;
    }

    @Override
    Response callVideoUrl(WatchVideoRequest watchVideoRequest, String email, String uuid, long conventionId)
    {
//        if(email != null)
//            throw new UnsupportedOperationException("Hey, test");
        logEvent();
        return watchVideoRequest.startWatchVideo(conventionId, email, uuid);
    }

    private void logEvent()
    {
        try
        {
            Event event = DatabaseHelper.getInstance(AppManager.getContext())
                    .getEventDao()
                    .queryForId(eventId);
            AppManager.getPlatformClient().logEvent(AnalyticsEvents.START_VIDEO, "item_id", Long.toString(eventId), "item_name", event.name);
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
