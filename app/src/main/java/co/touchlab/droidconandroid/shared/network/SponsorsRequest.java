package co.touchlab.droidconandroid.shared.network;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kgalligan on 7/20/14.
 */
public interface SponsorsRequest {
    @GET("/droidconsponsers/{fileName}")
    Observable<SponsorsResult> getSponsors(@Path("fileName") String fileName);
}
