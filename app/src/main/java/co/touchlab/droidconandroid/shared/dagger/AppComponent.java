package co.touchlab.droidconandroid.shared.dagger;

import javax.inject.Singleton;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataViewModel;
import co.touchlab.droidconandroid.shared.presenter.EventDetailViewModel;
import co.touchlab.droidconandroid.shared.presenter.ScheduleDataViewModel;
import co.touchlab.droidconandroid.shared.presenter.SeedInteractor;
import co.touchlab.droidconandroid.shared.presenter.SponsorsViewModel;
import co.touchlab.droidconandroid.shared.presenter.UserDetailViewModel;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class, NetworkModule.class})
public interface AppComponent
{
    AppPrefs getPrefs();

    SeedInteractor seedInteractor();

    void inject(UserDetailViewModel.Factory target);

    void inject(SponsorsViewModel.Factory target);

    void inject(ScheduleDataViewModel.Factory target);

    void inject(EventDetailViewModel.Factory target);

    void inject(ConferenceDataViewModel.Factory target);

}
