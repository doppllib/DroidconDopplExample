package co.touchlab.droidconandroid.shared.tasks;
import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.network.DataHelper;
import co.touchlab.droidconandroid.shared.network.NetworkErrorHandler;
import co.touchlab.droidconandroid.shared.network.SponsorsRequest;
import co.touchlab.droidconandroid.shared.network.SponsorsResult;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import retrofit.RestAdapter;

public class SponsorsTask extends Task
{
    public static final int SPONSOR_GENERAL   = 0;
    public static final int SPONSOR_STREAMING = 1;
    public static final int SPONSOR_PARTY     = 2;

    //TODO move to build config?
    public static final String URL_AMAZON_S3 = "https://s3.amazonaws.com/";

    public int            type;
    public SponsorsResult response;

    public SponsorsTask(int type)
    {
        this.type = type;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        RestAdapter restAdapter = DataHelper.makeRequestAdapterBuilder(context,
                AppManager.getPlatformClient(), URL_AMAZON_S3,
                new NetworkErrorHandler()).build();
        String fileName = getFileName(type);
        response = restAdapter.create(SponsorsRequest.class).getSponsors(fileName);
    }

    private String getFileName(int type)
    {
        switch(type)
        {
            case SPONSOR_STREAMING:
                return "sponsors_stream.json";
            case SPONSOR_PARTY:
                return "sponsors_party.json";
            default:
                return "sponsors_general.json";
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        CrashReport.logException(throwable);
        return true;
    }

    @Override
    protected void onComplete(Context context)
    {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);
    }
}
