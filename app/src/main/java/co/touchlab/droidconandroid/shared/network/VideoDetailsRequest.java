package co.touchlab.droidconandroid.shared.network;


import co.touchlab.droidconandroid.shared.network.dao.EventVideoDetails;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VideoDetailsRequest {
    @GET("/dataTest/eventVideoDetails/{eventId}")
    Observable<EventVideoDetails> getEventVideoDetails(@Path("eventId")Long eventId);
}
