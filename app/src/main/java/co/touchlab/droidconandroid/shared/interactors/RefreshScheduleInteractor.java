package co.touchlab.droidconandroid.shared.interactors;

import android.util.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.List;
import java.util.concurrent.TimeUnit;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.network.RefreshScheduleDataRequest;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.TimeBlock;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper;
import co.touchlab.droidconandroid.shared.presenter.DaySchedule;
import co.touchlab.droidconandroid.shared.presenter.PlatformClient;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class RefreshScheduleInteractor
{
    private final ConferenceDataHelper       conferenceDataHelper;
    private final AppPrefs                   appPrefs;
    private final RefreshScheduleDataRequest request;
    private BehaviorSubject<List<TimeBlock>> conferenceDataSubject = BehaviorSubject.create();

    private static final long SERVER_REFRESH_TIME = 3600000 * 6; // 6 hours
    private static final int  RETRY_COUNT         = 5;
    private static final int  INITIAL_DELAY_SEC   = 60;

    @Inject
    public RefreshScheduleInteractor(ConferenceDataHelper conferenceDataHelper, AppPrefs appPrefs, RefreshScheduleDataRequest request)
    {
        this.conferenceDataHelper = conferenceDataHelper;
        this.appPrefs = appPrefs;
        this.request = request;

        refreshFromDatabase();

        if((System.currentTimeMillis() - appPrefs.getRefreshTime() > SERVER_REFRESH_TIME))
        {
            refreshFromServer();
        }
    }

    public Observable<DaySchedule[]> getFullConferenceData(boolean allEvents)
    {
        return conferenceDataSubject.flatMap(list -> filterAndSortBlocks(list, allEvents))
                .map(conferenceDataHelper:: formatHourBlocks)
                .map(conferenceDataHelper:: convertMapToDaySchedule)
                .map(dayScheduleList -> dayScheduleList.toArray(new DaySchedule[dayScheduleList.size()]));
    }

    public void refreshFromDatabase()
    {
        conferenceDataHelper.getDays()
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
        request.getScheduleData(platformClient.getConventionId())
                .retryWhen(error -> error
                        .zipWith(Observable.range(1, RETRY_COUNT), (e, integer) ->
                        {
                            if(integer == RETRY_COUNT) return new Pair<>(error, 0);
                            return new Pair<>(error, integer);
                        })
                        .flatMap(this :: retryWithExponentialBackoff)
                )
                .flatMapCompletable(conferenceDataHelper:: saveConvention)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            appPrefs.setRefreshTime(System.currentTimeMillis());
                            refreshFromDatabase();
                        },
                        CrashReport:: logException);
    }

    private Observable retryWithExponentialBackoff(Pair<Observable<Throwable>, Integer> errorRetryCountTuple)
    {
        int retryAttempt = errorRetryCountTuple.second;

        if(retryAttempt == 0)
        {
            return errorRetryCountTuple.first;
        }

        long delay = INITIAL_DELAY_SEC * (long) Math.pow(2, Math.max(0, retryAttempt - 1));
        return Observable.timer(delay, TimeUnit.SECONDS);
    }

    private Observable<List<TimeBlock>> filterAndSortBlocks(List<TimeBlock> list, boolean allEvents)
    {
        return Observable.fromIterable(list)
                .filter(timeBlock -> allEvents || timeBlock.isBlock() ||
                        ((timeBlock instanceof Event) && (((Event) timeBlock).rsvpUuid != null)))
                .toSortedList(conferenceDataHelper:: sortTimeBlocks)
                .toObservable();
    }
}