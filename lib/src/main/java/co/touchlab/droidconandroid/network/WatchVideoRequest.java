package co.touchlab.droidconandroid.network;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by kgalligan on 8/17/16.
 */
public interface WatchVideoRequest
{
    @FormUrlEncoded
    @POST("/video/startWatchVideo")
    Response startWatchVideo(@Field("conventionId") Long conventionId, @Field("email") String email, @Field("viewerUuid") String viewerUuid);

    @FormUrlEncoded
    @POST("/video/checkWatchVideo")
    Response checkWatchVideo(@Field("conventionId") Long conventionId, @Field("email") String email, @Field("viewerUuid") String viewerUuid, @Field("eventId") String eventId);


}
