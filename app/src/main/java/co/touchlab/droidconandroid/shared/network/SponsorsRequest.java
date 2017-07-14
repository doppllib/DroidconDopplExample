package co.touchlab.droidconandroid.shared.network;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by kgalligan on 7/20/14.
 */
public interface SponsorsRequest {
    @GET("/droidconsponsers/{fileName}")
    rx.Observable<SponsorsResult> getSponsors(@Path("fileName") String fileName);
}
