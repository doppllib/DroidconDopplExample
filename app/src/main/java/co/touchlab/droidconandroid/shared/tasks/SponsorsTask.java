package co.touchlab.droidconandroid.shared.tasks;

import co.touchlab.droidconandroid.shared.network.SponsorsRequest;
import co.touchlab.droidconandroid.shared.network.SponsorsResult;
import retrofit2.Retrofit;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;

public class SponsorsTask {
    public static final int SPONSOR_GENERAL = 0;
    public static final int SPONSOR_STREAMING = 1;
    public static final int SPONSOR_PARTY = 2;

    private final SponsorsRequest request;

    public SponsorsTask(SponsorsRequest request) {
        this.request = request;
    }

    public Observable<SponsorsResult> getSponsors(int type) {
        String fileName = getFileName(type);
        return RxJavaInterop.toV2Observable(request.getSponsors(fileName));
    }

    private String getFileName(int type) {
        switch (type) {
            case SPONSOR_STREAMING:
                return "sponsors_stream.json";
            case SPONSOR_PARTY:
                return "sponsors_party.json";
            default:
                return "sponsors_general.json";
        }
    }
}
