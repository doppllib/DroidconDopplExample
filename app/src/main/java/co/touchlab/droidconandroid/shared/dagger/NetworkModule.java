package co.touchlab.droidconandroid.shared.dagger;

import javax.inject.Singleton;

import co.touchlab.droidconandroid.BuildConfig;
import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.RefreshScheduleDataRequest;
import co.touchlab.droidconandroid.shared.network.RsvpRequest;
import co.touchlab.droidconandroid.shared.network.SponsorsRequest;
import dagger.Module;
import dagger.Provides;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule
{

    @Provides
    @Singleton
    @DroidconServer
    String providesBaseUrl()
    {
        return BuildConfig.BASE_URL;
    }

    @Provides
    @Singleton
    @AmazonServer
    String providesAmazonBaseUrl()
    {
        return "https://s3.amazonaws.com/";
    }

    @Provides
    @Singleton
    GsonConverterFactory providesGsonConverterFactory()
    {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    @DroidconServer
    Retrofit providesDroidconRetrofit(GsonConverterFactory factory, @DroidconServer String baseUrl)
    {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory).build();
    }

    @Provides
    @Singleton
    @AmazonServer
    Retrofit providesAmazonRetrofit(GsonConverterFactory factory, @AmazonServer String baseUrl)
    {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .build();
    }

    @Provides
    @Singleton
    FindUserRequest providesFindUserRequest(@DroidconServer Retrofit retrofit)
    {
        return retrofit.create(FindUserRequest.class);
    }


    @Provides
    @Singleton
    RefreshScheduleDataRequest providesRefreshScheduleRequest(@DroidconServer Retrofit retrofit)
    {
        return retrofit.create(RefreshScheduleDataRequest.class);
    }

    @Provides
    @Singleton
    SponsorsRequest providesSponsorRequest(@AmazonServer Retrofit retrofit)
    {
        return retrofit.create(SponsorsRequest.class);
    }

    @Provides
    @Singleton
    RsvpRequest providesRsvpRequest(@DroidconServer Retrofit retrofit)
    {
        return retrofit.create(RsvpRequest.class);
    }

    @Provides
    ObservableTransformer providesTransformer()
    {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
