package co.touchlab.droidconandroid.shared.interactors;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.network.RefreshScheduleDataRequest;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper;
import co.touchlab.droidconandroid.shared.presenter.DaySchedule;
import co.touchlab.droidconandroid.shared.presenter.PlatformClient;
import co.touchlab.droidconandroid.shared.utils.EventBusExt;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class RefreshScheduleInteractor
{
    private final DatabaseHelper             databaseHelper;
    private final AppPrefs                   appPrefs;
    private final RefreshScheduleDataRequest request;
    private BehaviorSubject<DaySchedule[]> conferenceDataSubject = BehaviorSubject.create();

    @Inject
    public RefreshScheduleInteractor(AppPrefs appPrefs, DatabaseHelper databaseHelper, RefreshScheduleDataRequest request)
    {
        this.databaseHelper = databaseHelper;
        this.appPrefs = appPrefs;
        this.request = request;
    }

    public Observable<DaySchedule[]> getDataStream()
    {
        return conferenceDataSubject;
    }

    public void refreshFromDatabase(boolean allEvents)
    {
        ConferenceDataHelper.getDays(databaseHelper, allEvents)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(conferenceDataSubject:: onNext, e ->
                {
                    CrashReport.logException(e);
                    refreshFromServer();
                });
    }

    public void refreshFromServer()
    {
        final PlatformClient platformClient = AppManager.getInstance().getPlatformClient();

        appPrefs.setRefreshTime(System.currentTimeMillis());

        request.getScheduleData(platformClient.getConventionId())
                .flatMapCompletable(convention -> ConferenceDataHelper.saveConvention(databaseHelper,
                        appPrefs,
                        convention))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> EventBusExt.getDefault().post(this), CrashReport:: logException);

        // FIXME: Can get rid of the EventBus here because this links straight to the Activity
    }
}