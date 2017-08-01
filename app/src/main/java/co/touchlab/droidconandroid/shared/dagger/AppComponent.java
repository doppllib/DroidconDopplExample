package co.touchlab.droidconandroid.shared.dagger;

import javax.inject.Singleton;

import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.RefreshScheduleDataRequest;
import co.touchlab.droidconandroid.shared.network.SponsorsRequest;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class, NetworkModule.class})
public interface AppComponent
{
    FindUserRequest getFindUserRequest();

    SponsorsRequest getSponsorsRequest();

    RefreshScheduleDataRequest getRefreshScheduleRequest();
}
