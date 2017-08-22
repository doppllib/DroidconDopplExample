package co.touchlab.droidconandroid.shared.network;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import co.touchlab.droidconandroid.BuildConfig;
import co.touchlab.droidconandroid.shared.network.dao.Convention;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .build();
    }

    private Class asdf = HttpsURLConnection.class;

    public static void callPie()
    {
        try
        {
            /*{
                Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RefreshScheduleDataRequest refreshScheduleDataRequest = retrofit.create(
                        RefreshScheduleDataRequest.class);

                Call<Convention> scheduleDataSync = refreshScheduleDataRequest.getScheduleDataSync(
                        61100);
                Response<Convention> conventionResponse = scheduleDataSync.execute();
                Convention con = conventionResponse.body();

                System.out.println(con.description);
            }*/

            {
                Retrofit build = new Retrofit.Builder().baseUrl(
                        "https://droidcon-server.herokuapp.com/")
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AsdfApi asdfApi = build.create(AsdfApi.class);
                Call<AsdfPlayer> playerCall = asdfApi.createGame("124", "Hello Guy");
                //                Call<TokenResponse> responseResponse = quikOrderApi.oauthAccessToken("password",
//                        "email",
//                        "f0147c04613f401ab7522bf12a28273b",
//                        "08117a1c74a44833979ae4fbed9cb97c",
//                        "f53bb29583a54673a103b5cda5cd1e5f",
//                        "droga");

                safeRun(playerCall);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //        observable.subscribe(tokenResponse -> {
//            System.out.println(tokenResponse.access_token);
//        });
    }

    private static void safeRun(Call<AsdfPlayer> playerCall)
    {
//        try
//        {
            playerCall.enqueue(new Callback<AsdfPlayer>()
            {
                @Override
                public void onResponse(Call<AsdfPlayer> call, Response<AsdfPlayer> response)
                {
                    System.out.println("Facebook name: "+ response.body().getFacebookName());
                }

                @Override
                public void onFailure(Call<AsdfPlayer> call, Throwable t)
                {
                    System.out.println("Failure: "+ t.getMessage());
                }
            });
//            Response<AsdfPlayer> execute = playerCall.execute();
//            AsdfPlayer body = execute.body();
//            System.out.println(body.getFacebookName());
//        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
    }
}
