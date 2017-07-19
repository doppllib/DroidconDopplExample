package co.touchlab.droidconandroid.shared.tasks.persisted;
import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;

import co.touchlab.droidconandroid.DroidconApplication;

public class JobQueueService extends FrameworkJobSchedulerService
{
    @NonNull
    @Override
    protected JobManager getJobManager()
    {
        return DroidconApplication.getInstance().getJobManager();
    }
}
