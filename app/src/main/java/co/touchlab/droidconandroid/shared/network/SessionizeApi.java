package co.touchlab.droidconandroid.shared.network;


import java.util.List;

import co.touchlab.droidconandroid.shared.network.sessionize.SessionGroup;
import co.touchlab.droidconandroid.shared.network.sessionize.Speaker;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SessionizeApi
{

    @GET("api/v2/{id}/view/Sessions")
    Single<List<SessionGroup>> fetchSessions(@Path("id") String id);

    @GET("api/v2/{id}/view/Speakers")
    Single<List<Speaker>> fetchSpeakers(@Path("id") String id);

}
