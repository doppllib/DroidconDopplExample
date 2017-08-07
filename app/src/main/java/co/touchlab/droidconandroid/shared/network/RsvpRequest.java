package co.touchlab.droidconandroid.shared.network;


import co.touchlab.droidconandroid.shared.network.dao.JustId;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by kgalligan on 7/20/14.
 */
public interface RsvpRequest {
    @POST("/dataTest/rsvpEventAnon/{id}")
    Observable<JustId> rsvp(@Path("id") Long eventId);

    @POST("/dataTest/unRsvpEventAnon/{id}")
    Observable<JustId> unRsvp(@Path("id") Long eventId);
}
