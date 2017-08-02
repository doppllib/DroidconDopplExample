package co.touchlab.droidconandroid.shared.presenter;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.google.j2objc.annotations.AutoreleasePool;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.interactors.RefreshScheduleInteractor;

public class ConferenceDataViewModel extends ViewModel
{
    private static final long SERVER_REFRESH_TIME = 3600000 * 6; // 6 hours

    private RefreshScheduleInteractor interactor;
    private AppPrefs                  appPrefs;

    private ConferenceDataViewModel(RefreshScheduleInteractor interactor, AppPrefs appPrefs)
    {
        this.interactor = interactor;
        this.appPrefs = appPrefs;
        refreshConferenceData();
    }

    @AutoreleasePool
    public void refreshConferenceData()
    {
        interactor.refreshFromDatabase();
    }

    public void refreshFromServer()
    {
        if((System.currentTimeMillis() - appPrefs.getRefreshTime() > SERVER_REFRESH_TIME))
        {
            interactor.refreshFromServer();
        }
        else
        {
            refreshConferenceData();
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {
        @Inject
        RefreshScheduleInteractor interactor;
        @Inject
        AppPrefs                  appPrefs;
        private final boolean allEvents;

        public Factory(boolean allEvents)
        {

            this.allEvents = allEvents;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new ConferenceDataViewModel(interactor, appPrefs);
        }
    }
}
