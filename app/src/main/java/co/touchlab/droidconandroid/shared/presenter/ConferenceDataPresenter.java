package co.touchlab.droidconandroid.shared.presenter;
import android.content.Context;
import android.text.TextUtils;

import com.google.j2objc.annotations.AutoreleasePool;
import com.google.j2objc.annotations.Weak;

import java.util.List;

import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.Block;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.tasks.SeedScheduleDataTask;
import co.touchlab.droidconandroid.shared.tasks.UpdateAlertsInteractor;
import co.touchlab.droidconandroid.shared.tasks.persisted.RefreshScheduleData;
import co.touchlab.droidconandroid.shared.utils.SlackUtils;

/**
 * Created by kgalligan on 4/15/16.
 */
public class ConferenceDataPresenter extends AbstractEventBusPresenter
{
    public static final long SERVER_REFRESH_TIME = 3600000 * 6; // 6 hours

    @Weak
    private final ConferenceDataHost conferenceDataHost;
    private final boolean allEvents;

    public ConferenceDataPresenter(Context context, ConferenceDataHost conferenceDataHost, boolean allEvents)
    {
        super(context);
        this.conferenceDataHost = conferenceDataHost;
        this.allEvents = allEvents;
        refreshConferenceData();
    }

    public void onEventMainThread(SeedScheduleDataTask task)
    {
        refreshConferenceData();
    }

    @AutoreleasePool
    public void refreshConferenceData()
    {
        TaskQueue.loadQueueDefault(getContext()).execute(new LoadConferenceDataTask(allEvents));
    }

    public void refreshFromServer()
    {
        AppPrefs prefs = AppPrefs.getInstance(getContext());

        if ((System.currentTimeMillis() - prefs.getRefreshTime() > SERVER_REFRESH_TIME))
        {
            RefreshScheduleData.callMe(getContext());
        }
    }

    public void onEventMainThread(LoadConferenceDataTask task)
    {
        TaskQueue.loadQueueDefault(getContext()).execute(new UpdateAlertsInteractor());

        // Since this task is called from both the Schedule and My Agenda, make sure this flag is the same
        if(allEvents == task.allEvents)
        {
            conferenceDataHost.loadCallback(task.conferenceDayHolders);
        }
    }

    public void onEventMainThread(RefreshScheduleData task)
    {
        refreshConferenceData();
    }

    public static void styleEventRow(ScheduleBlockHour scheduleBlockHour, List dataSet, EventRow row, boolean allEvents)
    {
        boolean isFirstInBlock = !scheduleBlockHour.hourStringDisplay.isEmpty();
        row.setTimeGap(isFirstInBlock);

        if(scheduleBlockHour.getScheduleBlock().isBlock())
        {
            Block block = (Block)scheduleBlockHour.scheduleBlock;
            row.setTitleText(block.name);
            row.setTimeText(scheduleBlockHour.hourStringDisplay.toLowerCase());
            row.setDetailText("");
            row.setDescription(block.description);
            row.setLiveNowVisible(false);
            row.setRsvpVisible(false, false);
            row.setRsvpConflict(false);
        }
        else
        {
            Event event = (Event)scheduleBlockHour.scheduleBlock;
            row.setTimeText(scheduleBlockHour.hourStringDisplay.toLowerCase());
            row.setTitleText(event.name);
            row.setDetailText(event.getVenue().name);
            row.setDescription(event.description);
            row.setLiveNowVisible(event.isNow());
            row.setRsvpVisible(allEvents && event.isRsvped(), event.isPast());
            row.setRsvpConflict(allEvents && hasConflict(event, dataSet));
        }
    }

    public static boolean hasConflict(Event event, List dataSet)
    {
        if (event.isRsvped() && !event.isPast())
        {
            for(Object o : dataSet)
            {
                if(o instanceof ScheduleBlockHour && ((ScheduleBlockHour) o).scheduleBlock instanceof Event)
                {
                    Event e = (Event) ((ScheduleBlockHour) o).scheduleBlock;
                    if(event.id != e.id && ! TextUtils.isEmpty(e.rsvpUuid) &&
                            event.startDateLong < e.endDateLong &&
                            event.endDateLong > e.startDateLong) return true;
                }
            }
        }

        return false;
    }

    public String getSlackLink()
    {
        return SlackUtils.createSlackLink(null);
    }

    public String getSlackLinkHttp()
    {
        return SlackUtils.createSlackLinkHttp(null);
    }

    public boolean shouldShowSlackDialog()
    {
        return AppPrefs.getInstance(getContext()).getShowSlackDialog();
    }

    public interface EventRow
    {
        void setTimeGap(boolean b);
        void setTitleText(String s);
        void setTimeText(String s);
        void setDetailText(String s);
        void setDescription(String s);
        void setLiveNowVisible(boolean b);
        void setRsvpVisible(boolean rsvp, boolean past);
        void setRsvpConflict(boolean b);
    }
}
