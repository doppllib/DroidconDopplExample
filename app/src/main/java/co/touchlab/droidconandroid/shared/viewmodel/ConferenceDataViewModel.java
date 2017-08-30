package co.touchlab.droidconandroid.shared.viewmodel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.utils.TimeUtils;
import io.reactivex.disposables.CompositeDisposable;

public class ConferenceDataViewModel extends ViewModel
{
    public static final long DAY_IN_MILLIS = 86400000L;
    private AppPrefs                  appPrefs;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ConferenceDataViewModel(AppPrefs appPrefs)
    {
        this.appPrefs = appPrefs;
    }
    public void register(Host host)
    {
        compositeDisposable.add(appPrefs.observeConventionDates().subscribe(datePair ->
        {
            Long start = TimeUtils.sanitize(TimeUtils.LOCAL_DATE_FORMAT.get().parse(datePair.first));
            Long end = TimeUtils.sanitize(TimeUtils.LOCAL_DATE_FORMAT.get().parse(datePair.second));

            List<Long> dateLongs = new ArrayList<>();

            while (start <= end) {
                dateLongs.add(start);
                start += DAY_IN_MILLIS;
            }

            host.updateConferenceDates(dateLongs);
        }));
    }

    public void unregister()
    {
        compositeDisposable.clear();
    }

    public interface Host
    {
        void updateConferenceDates(List<Long> dates);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {
        @Inject
        AppPrefs                  appPrefs;
        private final boolean allEvents;

        Factory(boolean allEvents)
        {
            this.allEvents = allEvents;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new ConferenceDataViewModel(appPrefs);
        }
    }

    public static Factory factory(boolean allEvents)
    {
        ConferenceDataViewModel.Factory factory = new ConferenceDataViewModel.Factory(allEvents);
        AppManager.getInstance().getAppComponent().inject(factory);

        return factory;
    }

    @NonNull
    public static ConferenceDataViewModel forIos(boolean allEvents)
    {
        return factory(allEvents).create(ConferenceDataViewModel.class);
    }

    public enum AppScreens
    {
        Welcome,
        Schedule
    }

    @NonNull
    public AppScreens goToScreen()
    {
        boolean hasSeenWelcome = appPrefs.getHasSeenWelcome();
        if (!hasSeenWelcome)
        {
            return AppScreens.Welcome;
        }
        else
        {
            return AppScreens.Schedule;
        }
    }
}
