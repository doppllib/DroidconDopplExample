package co.touchlab.droidconandroid.shared.network;

import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by kgalligan on 7/26/14.
 */
public interface FindUserRequest
{
    @GET("/dataTest/findUserByCode/{userCode}")
    UserInfoResponse getUserInfo(@Path("userCode") String userCode);
}
