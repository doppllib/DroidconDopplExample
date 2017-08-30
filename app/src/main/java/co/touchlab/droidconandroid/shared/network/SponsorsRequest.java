package co.touchlab.droidconandroid.shared.network;


import co.touchlab.droidconandroid.shared.network.dao.SponsorsResult;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kgalligan on 7/20/14.
 */
public interface SponsorsRequest
{
    @GET("/droidconsponsers/{fileName}")
    Observable<SponsorsResult> getSponsors(@Path("fileName") String fileName);
}
