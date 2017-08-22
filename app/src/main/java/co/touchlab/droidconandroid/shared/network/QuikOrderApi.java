package co.touchlab.droidconandroid.shared.network;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kgalligan on 8/10/17.
 */

public interface QuikOrderApi
{
    //*************************
    //AUTH METHODS
    //*************************

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<TokenResponse> oauthAccessToken(
            @Field("grant_type") String grantType,
            @Field("scope") String scope,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("provision_key") String provisionKey,
            @Field("authenticated_userid") String authenticatedUserid
    );


}
