package co.touchlab.droidconandroid.shared.presenter;


import co.touchlab.droidconandroid.shared.network.SponsorsResult;

public interface SponsorsHost {

    void onSponsorsFound(SponsorsResult result);

    void onError();
}
