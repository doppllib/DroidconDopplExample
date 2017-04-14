package co.touchlab.droidconandroid.shared.tasks;
import co.touchlab.droidconandroid.shared.network.WatchVideoRequest;
import retrofit.client.Response;

/**
 * Created by kgalligan on 8/17/16.
 */
public class CheckWatchVideoTask extends AbstractWatchVideoTask
{
    private final long eventId;

    public CheckWatchVideoTask(long eventId)
    {
        this.eventId = eventId;
    }

    @Override
    Response callVideoUrl(WatchVideoRequest watchVideoRequest, String email, String uuid, long conventionId)
    {
        return watchVideoRequest.checkWatchVideo(conventionId, email, uuid, Long.toString(eventId));
    }
}
