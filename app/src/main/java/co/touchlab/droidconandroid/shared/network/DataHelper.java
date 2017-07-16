package co.touchlab.droidconandroid.shared.network;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kgalligan on 6/28/14.
 */
public class DataHelper
{
    public static Retrofit makeRetrofit2Client(String baseUrl)
    {
        GsonConverterFactory factory = GsonConverterFactory.create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .build();

        return retrofit;
    }

    private Class asdf = HttpsURLConnection.class;
}
