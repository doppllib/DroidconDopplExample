package co.touchlab.droidconandroid.shared.interactors;

import com.birbit.android.jobqueue.JobManager;

import java.util.List;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.TimeBlock;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper;
import co.touchlab.droidconandroid.shared.presenter.DaySchedule;
import co.touchlab.droidconandroid.shared.tasks.persisted.RefreshScheduleJob;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class RefreshScheduleInteractor
{
    private final JobManager     jobManager;
    private final DatabaseHelper databaseHelper;
    private BehaviorSubject<List<TimeBlock>> conferenceDataSubject = BehaviorSubject.create();

    public RefreshScheduleInteractor(JobManager jobManager, DatabaseHelper databaseHelper)
    {
        this.databaseHelper = databaseHelper;
        this.jobManager = jobManager;
    }

    public Observable<DaySchedule[]> getDataStream(boolean allEvents)
    {
        return conferenceDataSubject
                .flatMap(list -> filterAndSortBlocks(list, allEvents))
                .map(ConferenceDataHelper:: formatHourBlocks)
                .map(ConferenceDataHelper:: convertMapToDaySchedule)
                .map(dayScheduleList -> dayScheduleList.toArray(new DaySchedule[dayScheduleList.size()]));
    }

    public void refreshFromDatabase()
    {
        ConferenceDataHelper.getDays(databaseHelper)
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
        jobManager.addJobInBackground(new RefreshScheduleJob());
    }

    private Observable<List<TimeBlock>> filterAndSortBlocks(List<TimeBlock> list, boolean allEvents) {
        return Flowable.fromIterable(list)
                .filter(timeBlock -> allEvents ||
                        timeBlock.isBlock() ||
                        ((timeBlock instanceof Event) && (((Event) timeBlock).rsvpUuid != null)))
                .toSortedList(ConferenceDataHelper:: sortTimeBlocks)
                .toObservable();
    }
}