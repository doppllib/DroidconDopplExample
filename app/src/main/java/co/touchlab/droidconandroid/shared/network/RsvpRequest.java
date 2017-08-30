package co.touchlab.droidconandroid.shared.network;


import co.touchlab.droidconandroid.shared.network.dao.JustId;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by kgalligan on 7/20/14.
 */
public interface RsvpRequest {
    @POST("/dataTest/rsvpEventAnon/{id}")
    @FormUrlEncoded
    Observable<JustId> rsvp(@Path("id") Long eventId, @Field("uuid") String uuid);

    @POST("/dataTest/unRsvpEventAnon/{id}")
    @FormUrlEncoded
    Observable<JustId> unRsvp(@Path("id") Long eventId, @Field("uuid") String uuid);
}
