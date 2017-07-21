package co.touchlab.droidconandroid.shared.network;

import co.touchlab.droidconandroid.shared.network.dao.Convention;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kgalligan on 7/20/14.
 */
public interface RefreshScheduleDataRequest {
    @GET("/dataTest/scheduleData/{conventionId}")
    Call<Convention> getScheduleData(@Path("conventionId") Integer conventionId);
}
