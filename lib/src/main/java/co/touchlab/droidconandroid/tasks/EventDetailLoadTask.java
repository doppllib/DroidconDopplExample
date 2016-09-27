package co.touchlab.droidconandroid.tasks;
import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.droidconandroid.data.DatabaseHelper;
import co.touchlab.droidconandroid.data.Event;
import co.touchlab.droidconandroid.data.EventSpeaker;
import co.touchlab.droidconandroid.data.UserAccount;
import co.touchlab.squeaky.dao.Dao;

/**
 * Created by kgalligan on 4/7/16.
 */
public class EventDetailLoadTask extends Task
{
    public final long eventId;

    public Event event;
    public boolean conflict;
    public List<UserAccount> speakers;

    public EventDetailLoadTask(long eventId)
    {
        this.eventId = eventId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        Dao<Event> dao = DatabaseHelper.getInstance(context).getEventDao();
        event = dao.queryForId(eventId);

        if(event.isRsvped())
            conflict = hasConflict(event, dao.queryForAll().list());

        Dao<EventSpeaker> eventSpeakerDao = DatabaseHelper.getInstance(context).getEventSpeakerDao();
        //        val results = Where<EventSpeaker, Long>(eventSpeakerDao).eq("event_id", eventId)!!.query()!!.list()!!

        List<EventSpeaker> results = eventSpeakerDao.queryForEq("event_id", eventId).list();

        speakers = new ArrayList(results.size());

        for (EventSpeaker eventSpeaker : results)
        {
            speakers.add(eventSpeaker.userAccount);
        }

        event.speakerList = results;
    }

    public long getEventId()
    {
        return eventId;
    }

    public Event getEvent()
    {
        return event;
    }

    public boolean isConflict()
    {
        return conflict;
    }

    public List<UserAccount> getSpeakers()
    {
        return speakers;
    }

    @Override
    protected boolean handleError(Context context, Throwable e)
    {
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
    }

    public static boolean hasConflict(Event event, List dataSet)
    {
        for (Object ce : dataSet) {
            if(ce instanceof Event) {
                Event cee = (Event)ce;
                if (event.id != cee.id && ! TextUtils.isEmpty(cee.rsvpUuid) && event.startDateLong < cee.endDateLong && event.endDateLong > cee.startDateLong)
                    return true;
            }
        }

        return false;
    }
}
