package co.touchlab.droidconandroid.presenter;
import android.content.Context;

import com.google.j2objc.annotations.Weak;

import org.apache.commons.lang3.StringUtils;

import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.android.threading.tasks.utils.TaskQueueHelper;
import co.touchlab.droidconandroid.data.AppPrefs;
import co.touchlab.droidconandroid.tasks.AddRsvpTask;
import co.touchlab.droidconandroid.tasks.EventDetailLoadTask;
import co.touchlab.droidconandroid.tasks.EventVideoDetailsTask;
import co.touchlab.droidconandroid.tasks.RemoveRsvpTask;
import co.touchlab.droidconandroid.tasks.StartWatchVideoTask;
import co.touchlab.droidconandroid.utils.AnalyticsEvents;
import co.touchlab.droidconandroid.utils.SlackUtils;

/**
 * Created by kgalligan on 4/25/16.
 */
public class EventDetailPresenter extends AbstractEventBusPresenter
{
    private final long eventId;

    @Weak
    private EventDetailHost       host;
    private EventDetailLoadTask   eventDetailLoadTask;
    private EventVideoDetailsTask eventVideoDetailsTask;

    public EventDetailPresenter(Context context, long eventId, EventDetailHost host)
    {
        super(context);
        this.eventId = eventId;
        this.host = host;
    }

    public void refreshData()
    {
        TaskQueue.loadQueueDefault(getContext()).execute(new EventDetailLoadTask(this.eventId));
    }

    public void callStartVideo(String link, String cover)
    {
        recordAnalytics(AnalyticsEvents.STREAM_CLICKED);
        TaskQueue.loadQueueNetwork(getContext())
                .execute(new StartWatchVideoTask(eventId, link, cover));
    }

    public void onEventMainThread(EventDetailLoadTask task)
    {
        eventDetailLoadTask = task;
        recordAnalytics(AnalyticsEvents.OPEN_EVENT);
        refreshVideoData();
        host.dataRefresh();
    }

    private void recordAnalytics(String analyticsKey)
    {
        String eventName = eventDetailLoadTask != null && eventDetailLoadTask.getEvent() != null ? StringUtils.trimToEmpty(eventDetailLoadTask.getEvent().getName()) : "";
        AppManager.getPlatformClient().logEvent(
                analyticsKey,
                AnalyticsEvents.PARAM_ITEM_ID,
                Long.toString(eventId),
                AnalyticsEvents.PARAM_ITEM_NAME,
                eventName
        );
    }

    public void refreshVideoData()
    {
        TaskQueue.loadQueueNetwork(getContext()).execute(new EventVideoDetailsTask(eventId));
    }

    public void onEventMainThread(EventVideoDetailsTask task)
    {
        if(task.getEventId() == eventId)
        {
            eventVideoDetailsTask = task;
            host.videoDataRefresh();
        }
    }

    public void onEventMainThread(RemoveRsvpTask task)
    {
        refreshData();
    }

    public void onEventMainThread(AddRsvpTask task)
    {
        refreshData();
    }

    public void onEventMainThread(StartWatchVideoTask task)
    {
        host.resetStreamProgress();
        if(task.videoOk)
        {
            recordAnalytics(AnalyticsEvents.STREAM_SUCCESS);
            host.callStreamActivity(task);
        }
        else if(task.unauthorized)
        {

            host.showTicketOptions(AppPrefs.getInstance(getContext()).getEventbriteEmail(),
                    task.link,
                    task.cover);
        }
        else
        {
            host.reportError("Couldn't start video. Either server or network issue.");
        }
    }

    public boolean isStreamStarting()
    {
        return TaskQueueHelper.hasTasksOfType(TaskQueue.loadQueueNetwork(getContext()),
                StartWatchVideoTask.class);
    }

    private boolean ready()
    {
        return eventDetailLoadTask != null;
    }

    public EventDetailLoadTask getEventDetailLoadTask()
    {
        return eventDetailLoadTask;
    }

    public EventVideoDetailsTask getEventVideoDetailsTask()
    {
        return eventVideoDetailsTask;
    }

    @Override
    public void unregister()
    {
        super.unregister();
        host = null;
    }

    public void toggleRsvp()
    {
        if(! ready())
        {
            return;
        }

        long eventId = eventDetailLoadTask.event.id;
        if(eventDetailLoadTask.event.isRsvped())
        {
            TaskQueue.loadQueueDefault(getContext())
                    .execute(new RemoveRsvpTask(eventId));
            recordAnalytics(AnalyticsEvents.UNRSVP_EVENT);
        }
        else
        {
            TaskQueue.loadQueueDefault(getContext()).execute(new AddRsvpTask(eventId));
            recordAnalytics(AnalyticsEvents.RSVP_EVENT);
        }
    }

    public void setEventbriteEmail(String email, String link, String cover)
    {
        AppPrefs.getInstance(getContext()).setEventbriteEmail(email);
        callStartVideo(link, cover);
    }

    public void openSlack()
    {
        String slackLink = SlackUtils.createSlackLink(eventDetailLoadTask.event.venue);
        String slackLinkHttp = SlackUtils.createSlackLinkHttp(eventDetailLoadTask.event.venue);
        host.openSlack(slackLink,
                slackLinkHttp,
                AppPrefs.getInstance(getContext()).getShowSlackDialog());
    }
}
