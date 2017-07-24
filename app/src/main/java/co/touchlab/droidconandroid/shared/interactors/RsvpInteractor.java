package co.touchlab.droidconandroid.shared.interactors;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;

import java.util.UUID;

import co.touchlab.droidconandroid.shared.data2.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data2.Event;
import co.touchlab.droidconandroid.shared.tasks.persisted.RsvpJob;
import io.reactivex.Single;

/**
 * Created by kgalligan on 4/7/16.
 */
public class RsvpInteractor {
    private final Long           eventId;
    private final DatabaseHelper helper;
    private final JobManager     jobManager;

    public RsvpInteractor(JobManager jobManager, DatabaseHelper helper, Long eventId)
    {
        this.eventId = eventId;
        this.helper = helper;
        this.jobManager = jobManager;
    }

    public Single<Event> addRsvp() {
        return helper.getEventForId(eventId)
                .filter(event -> event != null && event.rsvpUuid == null)
                .map(event -> setRsvp(event, UUID.randomUUID().toString()))
                .flatMapSingle(this::updateEvent);
    }

    public Single<Event> removeRsvp() {
        return helper.getEventForId(eventId)
                .filter(event -> event != null)
                .map(event -> setRsvp(event, null))
                .flatMapSingle(this::updateEvent);
    }

    private Single<Event> updateEvent(Event event) {
        return Single.fromCallable(() -> {
            helper.updateEvent(event).subscribe();
            jobManager.addJobInBackground(new RsvpJob(eventId, event.rsvpUuid));
            return event;
        });
    }

    @NonNull
    private Event setRsvp(Event event, String uuid) {
        event.rsvpUuid = uuid;
        return event;
    }

}