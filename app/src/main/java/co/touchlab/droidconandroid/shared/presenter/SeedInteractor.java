package co.touchlab.droidconandroid.shared.presenter;


import com.google.gson.Gson;

import javax.inject.Inject;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.network.dao.Convention;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


import static co.touchlab.droidconandroid.shared.presenter.AppManager.FIRST_SEED;

public class SeedInteractor
{
    private final DatabaseHelper helper;
    private final AppPrefs       appPrefs;

    @Inject
    public SeedInteractor(DatabaseHelper helper, AppPrefs appPrefs)
    {
        this.helper = helper;
        this.appPrefs = appPrefs;
    }

    public void seedDatabase(LoadDataSeed loadDataSeed)
    {
        if(appPrefs.once(FIRST_SEED))
        {
            try
            {
                final String seed = loadDataSeed.dataSeed();
                ConferenceDataHelper.saveConvention(helper,
                        appPrefs,
                        new Gson().fromJson(seed, Convention.class))
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
