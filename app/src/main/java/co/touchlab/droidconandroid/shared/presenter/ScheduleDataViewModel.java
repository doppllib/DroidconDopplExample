package co.touchlab.droidconandroid.shared.presenter;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.interactors.RefreshScheduleInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ScheduleDataViewModel extends ViewModel
{
    private RefreshScheduleInteractor interactor;
    private CompositeDisposable disposables = new CompositeDisposable();

    private ScheduleDataViewModel(RefreshScheduleInteractor interactor)
    {
        this.interactor = interactor;
    }

    public interface Host
    {
        void loadCallback(DaySchedule[] daySchedules);
    }

    public void register(Host host, boolean allEvents)
    {
        disposables.clear();
        interactor.refreshFromDatabase();
        disposables.add(interactor.getFullConferenceData(allEvents)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(host:: loadCallback));
    }

    public void unregister()
    {
        disposables.clear();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {
        @Inject
        RefreshScheduleInteractor interactor;

        Factory()
        {
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new ScheduleDataViewModel(interactor);
        }
    }

    public static Factory factory()
    {
        Factory factory = new Factory();
        AppManager.getInstance().getAppComponent().inject(factory);

        return factory;
    }

    public static ScheduleDataViewModel forIos()
    {
        return factory().create(ScheduleDataViewModel.class);
    }
}