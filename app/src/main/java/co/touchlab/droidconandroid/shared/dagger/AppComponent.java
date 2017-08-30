package co.touchlab.droidconandroid.shared.dagger;

import javax.inject.Singleton;

import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.interactors.RefreshScheduleInteractor;
import co.touchlab.droidconandroid.shared.interactors.UpdateAlertsInteractor;
import co.touchlab.droidconandroid.shared.viewmodel.ConferenceDataViewModel;
import co.touchlab.droidconandroid.shared.viewmodel.EventDetailViewModel;
import co.touchlab.droidconandroid.shared.viewmodel.ScheduleDataViewModel;
import co.touchlab.droidconandroid.shared.interactors.SeedInteractor;
import co.touchlab.droidconandroid.shared.viewmodel.SponsorsViewModel;
import co.touchlab.droidconandroid.shared.viewmodel.UserDetailViewModel;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class, NetworkModule.class})
public interface AppComponent
{
    AppPrefs getPrefs();

    SeedInteractor seedInteractor();

    UpdateAlertsInteractor updateAlertsInteractor();

    RefreshScheduleInteractor refreshScheduleInteractor();

    void inject(UserDetailViewModel.Factory target);

    void inject(SponsorsViewModel.Factory target);

    void inject(ScheduleDataViewModel.Factory target);

    void inject(EventDetailViewModel.Factory target);

    void inject(ConferenceDataViewModel.Factory target);

}
