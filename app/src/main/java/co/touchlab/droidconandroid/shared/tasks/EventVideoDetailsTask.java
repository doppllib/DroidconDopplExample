package co.touchlab.droidconandroid.shared.tasks;

import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.network.DataHelper;
import co.touchlab.droidconandroid.shared.network.RefreshScheduleDataRequest;
import co.touchlab.droidconandroid.shared.network.dao.EventVideoDetails;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import co.touchlab.droidconandroid.shared.utils.StringUtils;
import retrofit2.Retrofit;

/**
 * Created by kgalligan on 9/14/16.
 */
public class EventVideoDetailsTask extends Task
{
    private final long eventId;
    private EventVideoDetails eventVideoDetails;

    public EventVideoDetailsTask(long eventId)
    {
        this.eventId = eventId;
    }

    public long getEventId()
    {
        return eventId;
    }

    public EventVideoDetails getEventVideoDetails()
    {
        return eventVideoDetails;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        Retrofit restAdapter = DataHelper.makeRetrofit2Client(AppManager.getPlatformClient().baseUrl());
        RefreshScheduleDataRequest refreshScheduleDataRequest = restAdapter.create(
                RefreshScheduleDataRequest.class);
        eventVideoDetails = refreshScheduleDataRequest.getEventVideoDetails(eventId).execute().body();
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        CrashReport.logException(throwable);
        return true;
    }

    public boolean hasStream()
    {
        return eventVideoDetails != null && (StringUtils.isNotEmpty(eventVideoDetails.getStreamLink()) || StringUtils.isNotEmpty(eventVideoDetails.getStreamArchiveLink()));
    }

    public boolean isNow()
    {
        return eventVideoDetails != null && StringUtils.isNotEmpty(eventVideoDetails.getStreamLink());
    }

    public String getMergedStreamLink()
    {
        if(eventVideoDetails == null)
            return null;

        return StringUtils.isNotEmpty(eventVideoDetails.getStreamLink()) ? eventVideoDetails.getStreamLink() : eventVideoDetails.getStreamArchiveLink();
    }
}
