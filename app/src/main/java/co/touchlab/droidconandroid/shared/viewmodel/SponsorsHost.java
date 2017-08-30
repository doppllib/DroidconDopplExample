package co.touchlab.droidconandroid.shared.viewmodel;


import co.touchlab.droidconandroid.shared.network.dao.SponsorsResult;

public interface SponsorsHost {

    void onSponsorsFound(SponsorsResult result);

    void onError();
}
