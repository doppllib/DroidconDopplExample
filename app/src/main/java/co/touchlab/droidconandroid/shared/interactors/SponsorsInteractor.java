package co.touchlab.droidconandroid.shared.interactors;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.network.SponsorsRequest;
import co.touchlab.droidconandroid.shared.network.SponsorsResult;
import io.reactivex.Observable;

public class SponsorsInteractor
{
    public static final int SPONSOR_GENERAL = 0;
    public static final int SPONSOR_PARTY   = 1;

    private final SponsorsRequest            request;
    private       Observable<SponsorsResult> sponsorsObservable;

    @Inject
    public SponsorsInteractor(SponsorsRequest request)
    {
        this.request = request;
    }

    public Observable<SponsorsResult> getSponsors(int type)
    {
        if(sponsorsObservable == null)
        {
            String fileName = getFileName(type);
            sponsorsObservable = request.getSponsors(fileName).cache();
        }

        return sponsorsObservable;
    }

    private String getFileName(int type)
    {
        switch(type)
        {
            case SPONSOR_PARTY:
                return "sponsors_party.json";
            default:
                return "sponsors_general.json";
        }
    }
}
