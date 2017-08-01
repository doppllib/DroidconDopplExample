package co.touchlab.droidconandroid.shared.dagger;

import javax.inject.Singleton;

import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.RefreshScheduleDataRequest;
import co.touchlab.droidconandroid.shared.network.SponsorsRequest;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule
{

    @Provides
    String providesBaseUrl()
    {
        return "https://droidcon-server.herokuapp.com/";
    }

    @Provides
    @Singleton
    GsonConverterFactory providesGsonConverterFactory()
    {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    FindUserRequest providesFindUserRequest(GsonConverterFactory factory, String baseUrl)
    {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .build()
                .create(FindUserRequest.class);
    }

    @Provides
    @Singleton
    RefreshScheduleDataRequest providesRefreshScheduleRequest(GsonConverterFactory factory, String baseUrl)
    {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .build()
                .create(RefreshScheduleDataRequest.class);
    }

    @Provides
    @Singleton
    SponsorsRequest providesSponsorRequest(GsonConverterFactory factory, String baseUrl)
    {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .build()
                .create(SponsorsRequest.class);
    }
}
