package co.touchlab.droidconandroid.shared.interactors;


import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.network.dao.Convention;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper;
import co.touchlab.droidconandroid.shared.presenter.LoadDataSeed;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


import static co.touchlab.droidconandroid.shared.presenter.AppManager.FIRST_SEED;

@Singleton
public class SeedInteractor
{
    private final ConferenceDataHelper conferenceDataHelper;
    private final AppPrefs             appPrefs;

    @Inject
    public SeedInteractor(ConferenceDataHelper conferenceDataHelper, AppPrefs appPrefs)
    {
        this.conferenceDataHelper = conferenceDataHelper;
        this.appPrefs = appPrefs;
    }

    public void seedDatabase(LoadDataSeed loadDataSeed)
    {
        if(appPrefs.once(FIRST_SEED))
        {
            try
            {
                final String seed = loadDataSeed.dataSeed();
                conferenceDataHelper.saveConvention(new Gson().fromJson(seed, Convention.class))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() ->
                        {
                        }, CrashReport:: logException);
            }
            catch(RuntimeException e)
            {
                CrashReport.logException(e);
            }
        }
    }
}
