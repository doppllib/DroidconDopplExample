package co.touchlab.droidconandroid.shared.network;
import co.touchlab.droidconandroid.shared.network.dao.JustId;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by kgalligan on 8/14/17.
 */

public interface AsdfApi
{
    @POST("/asdf/createGame")
    @FormUrlEncoded
    Call<AsdfPlayer> createGame(@Field("facebookId") String facebookId, @Field("facebookName") String facebookName);

}
