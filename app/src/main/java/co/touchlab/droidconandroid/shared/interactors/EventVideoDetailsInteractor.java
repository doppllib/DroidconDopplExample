package co.touchlab.droidconandroid.shared.interactors;

import co.touchlab.droidconandroid.shared.network.VideoDetailsRequest;
import co.touchlab.droidconandroid.shared.network.dao.EventVideoDetails;
import io.reactivex.Observable;

/**
 * Created by kgalligan on 9/14/16.
 */
public class EventVideoDetailsInteractor {
    private final long eventId;
    private final VideoDetailsRequest request;
    private Observable<EventVideoDetails> videoDetailsObservable;

    public EventVideoDetailsInteractor(VideoDetailsRequest request, long eventId) {
        this.request = request;
        this.eventId = eventId;
    }

    public Observable<EventVideoDetails> getVideoDetails() {
        if (videoDetailsObservable == null) {
            videoDetailsObservable = request.getEventVideoDetails(eventId).cache();
        }

        return videoDetailsObservable;
    }
}
