package co.touchlab.droidconandroid.shared.tasks.persisted;


import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import android.support.annotation.NonNull;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.utils.AnalyticsEvents;
import co.touchlab.droidconandroid.shared.utils.AnalyticsHelper;

public class RsvpJob extends Job
{
    private static final String GROUP_TAG   = "rsvp";
    private static final int    DELAY_IN_MS = 60000; // 1 min
    private static final int    MAX_RETRY   = 7;
    private              int    retryCount  = 0;
    private long   eventId;
    private String uuid;

    public RsvpJob(Long eventId, String uuid)
    {
        super(new Params(Priority.LOW).requireNetwork().groupBy(GROUP_TAG).persist());
        this.eventId = eventId;
        this.uuid = uuid;
    }

    @Override
    public void onAdded()
    {
    }

    @Override
    public void onRun() throws Throwable
    {
        // TODO post to backend
        if(uuid == null)
        {
            AnalyticsHelper.recordAnalytics(AnalyticsEvents.UNRSVP_EVENT, eventId);
        }
        else
        {
            AnalyticsHelper.recordAnalytics(AnalyticsEvents.RSVP_EVENT, eventId);
        }
    }

    @Override
    protected void onCancel(int cancelReason, @NonNull Throwable throwable)
    {
        CrashReport.logException(throwable);
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount)
    {
        if(retryCount < MAX_RETRY)
        {
            retryCount++;
            return RetryConstraint.createExponentialBackoff(runCount, DELAY_IN_MS);
        }
        else
        {
            retryCount = 0;
            return RetryConstraint.CANCEL;
        }
    }
}