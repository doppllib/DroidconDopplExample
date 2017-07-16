package co.touchlab.droidconandroid.shared.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kgalligan on 7/20/14.
 */
public interface SponsorsRequest
{
    @GET("/droidconsponsers/{fileName}")
    Call<SponsorsResult> getSponsors(@Path("fileName") String fileName) throws NetworkErrorHandler.NetworkException;
}
