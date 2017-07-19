package co.touchlab.droidconandroid.shared.presenter;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.util.Log;

import com.google.j2objc.annotations.AutoreleasePool;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.interactors.RefreshScheduleInteractor;
import co.touchlab.droidconandroid.shared.tasks.UpdateAlertsTask;
import co.touchlab.droidconandroid.shared.tasks.persisted.RefreshScheduleJob;
import co.touchlab.droidconandroid.shared.utils.SlackUtils;

public class ConferenceDataViewModel extends ViewModel
{
    private static final long SERVER_REFRESH_TIME = 3600000 * 6; // 6 hours

    private       RefreshScheduleInteractor interactor;
    private       boolean                   allEvents;
    private final Context                   context;
    private final AppPrefs                  appPrefs;

    private ConferenceDataViewModel(RefreshScheduleInteractor interactor, boolean allEvents, Context context)
    {
        this.interactor = interactor;
        this.allEvents = allEvents;
        this.context = context;
        this.appPrefs = AppPrefs.getInstance(context);
        refreshConferenceData();
    }

    @AutoreleasePool
    public void refreshConferenceData()
    {
        interactor.refreshFromDatabase(allEvents);
    }

    public void refreshFromServer()
    {
        //        if((System.currentTimeMillis() - appPrefs.getRefreshTime() > SERVER_REFRESH_TIME))
        //        {
        interactor.refreshFromServer();
        //        }
    }

    // FIXME: To be removed
    public String getSlackLink()
    {
        return SlackUtils.createSlackLink(null);
    }

    public String getSlackLinkHttp()
    {
        return SlackUtils.createSlackLinkHttp(null);
    }

    public boolean shouldShowSlackDialog()
    {
        return appPrefs.getShowSlackDialog();
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {
        private final RefreshScheduleInteractor interactor;
        private final boolean                   allEvents;
        private final Context                   context;

        public Factory(Context context, RefreshScheduleInteractor interactor, boolean allEvents)
        {
            this.interactor = interactor;
            this.allEvents = allEvents;
            this.context = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new ConferenceDataViewModel(interactor, allEvents, context);
        }
    }
}
