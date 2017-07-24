package co.touchlab.droidconandroid.shared.tasks.persisted;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data2.Data2Helper;
import co.touchlab.droidconandroid.shared.network.DataHelper;
import co.touchlab.droidconandroid.shared.network.RefreshScheduleDataRequest;
import co.touchlab.droidconandroid.shared.network.dao.Convention;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper;
import co.touchlab.droidconandroid.shared.presenter.PlatformClient;
import co.touchlab.droidconandroid.shared.utils.EventBusExt;
import retrofit2.Retrofit;

public class RefreshScheduleJob extends Job
{
    private static final String GROUP_TAG   = "refresh-schedule";
    private static final int    DELAY_IN_MS = 60000; // 1 min
    private static final int    MAX_RETRY   = 7;
    private              int    retryCount  = 0;

    public RefreshScheduleJob()
    {
        super(new Params(Priority.LOW).requireNetwork()
                .groupBy(GROUP_TAG)
                .singleInstanceBy(GROUP_TAG)
                .persist());
    }

    @Override
    public void onAdded()
    {
    }

    @Override
    public void onRun() throws Throwable
    {
        Retrofit retrofit = DataHelper.makeRetrofit2Client(AppManager.getPlatformClient()
                .baseUrl());
        RefreshScheduleDataRequest request = retrofit.create(RefreshScheduleDataRequest.class);
        final PlatformClient platformClient = AppManager.getPlatformClient();
        Convention convention = request.getScheduleData(platformClient.getConventionId())
                .execute()
                .body();
        Data2Helper helper = Data2Helper.getInstance(getApplicationContext());
        AppPrefs appPrefs = AppPrefs.getInstance(getApplicationContext());
        ConferenceDataHelper.saveConventionData(helper, appPrefs, convention);
        appPrefs.setRefreshTime(System.currentTimeMillis());
        EventBusExt.getDefault().post(this);
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable)
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
