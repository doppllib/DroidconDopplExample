package co.touchlab.droidconandroid.shared.tasks.persisted;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;

import org.jetbrains.annotations.NotNull;

import co.touchlab.droidconandroid.DroidconApplication;

public class JobQueueService extends FrameworkJobSchedulerService
{
    @NotNull
    @Override
    protected JobManager getJobManager()
    {
        return DroidconApplication.getInstance().getJobManager();
    }
}
