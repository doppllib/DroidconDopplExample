package co.touchlab.droidconandroid.shared.network;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Single;

/**
 * Created by kgalligan on 7/20/14.
 */
public interface SponsorsRequest
{
    @GET("/droidconsponsers/{fileName}")
    SponsorsResult getSponsors(@Path("fileName") String fileName) throws NetworkErrorHandler.NetworkException;

    @GET("/droidconsponsers/{fileName}")
    Single<SponsorsResult> getSponsorsSingle(@Path("fileName") String fileName);
}
