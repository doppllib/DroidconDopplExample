package co.touchlab.droidconandroid.shared.network;

import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kgalligan on 7/26/14.
 */
public interface FindUserRequest2
{
    @GET("/dataTest/findUserByCode/{userCode}")
    Observable<UserInfoResponse> getUserInfo(@Path("userCode") String userCode);
}
