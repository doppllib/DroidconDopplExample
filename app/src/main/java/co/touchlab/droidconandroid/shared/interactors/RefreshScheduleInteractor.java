package co.touchlab.droidconandroid.shared.interactors;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.DroidconApplication;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.data.Block;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.EventSpeaker;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.data.Venue;
import co.touchlab.droidconandroid.shared.network.dao.Convention;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataHelper;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDayHolder;
import co.touchlab.droidconandroid.shared.tasks.persisted.RefreshScheduleJob;
import co.touchlab.droidconandroid.shared.utils.StringUtils;
import co.touchlab.droidconandroid.shared.utils.TimeUtils;
import co.touchlab.droidconandroid.shared.utils.UserDataHelper;
import co.touchlab.squeaky.dao.Dao;
import co.touchlab.squeaky.stmt.Where;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class RefreshScheduleInteractor
{
    private final JobManager jobManager;
    private final Context    context;
    private BehaviorSubject<ConferenceDayHolder[]> conferenceDataSubject = BehaviorSubject.create();

    public RefreshScheduleInteractor(Context context)
    {
        this.context = context;
        this.jobManager = DroidconApplication.getInstance().getJobManager();
    }

    public Observable<ConferenceDayHolder[]> getDataStream()
    {
        return conferenceDataSubject.hide();
    }

    public void refreshFromDatabase(boolean allEvents)
    {
        try
        {
            ConferenceDayHolder[] newData = ConferenceDataHelper.listDays(context, allEvents);
            conferenceDataSubject.onNext(newData);
        }
        catch(SQLException e)
        {
            CrashReport.logException(e);
            refreshFromServer();
        }
    }

    public void refreshFromServer()
    {
        jobManager.addJobInBackground(new RefreshScheduleJob());
    }

    public static void saveConventionData(final Context context, final Convention convention)
    {
        if(convention == null)
        {
            throw new IllegalStateException("No convention results");
        }

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        AppPrefs appPrefs = AppPrefs.getInstance(context);
        helper.performTransactionOrThrowRuntime(() ->
        {
            Dao<Event> eventDao = helper.getEventDao();
            Dao<Venue> venueDao = helper.getVenueDao();
            Dao<Block> blockDao = helper.getBlockDao();
            Dao<UserAccount> userAccountDao = helper.getUserAccountDao();
            Dao<EventSpeaker> eventSpeakerDao = helper.getEventSpeakerDao();

            appPrefs.setConventionStartDate(convention.startDate);
            appPrefs.setConventionEndDate(convention.endDate);

            List<co.touchlab.droidconandroid.shared.network.dao.Venue> venues = convention.venues;
            List<co.touchlab.droidconandroid.shared.network.dao.Block> blocks = convention.blocks;
            Set<Long> foundEvents = new HashSet<Long>();
            try
            {

                for(co.touchlab.droidconandroid.shared.network.dao.Venue venue : venues)
                {
                    venueDao.createOrUpdate(venue);
                    final Iterator<co.touchlab.droidconandroid.shared.network.dao.Event> venueEventsIterator = venue.events
                            .iterator();
                    while(venueEventsIterator.hasNext())
                    {
                        co.touchlab.droidconandroid.shared.network.dao.Event event = venueEventsIterator
                                .next();

                        foundEvents.add(event.id);
                        Event dbEvent = eventDao.queryForId(event.id);
                        event.venue = venue;

                        if(StringUtils.isEmpty(event.startDate) ||
                                StringUtils.isEmpty(event.endDate))
                        {
                            continue;
                        }

                        event.startDateLong = TimeUtils.DATE_FORMAT.get()
                                .parse(event.startDate)
                                .getTime();
                        event.endDateLong = TimeUtils.DATE_FORMAT.get()
                                .parse(event.endDate)
                                .getTime();

                        if(dbEvent != null)
                        {
                            event.rsvpUuid = dbEvent.rsvpUuid;
                        }

                        eventDao.createOrUpdate(event);

                        Iterator<co.touchlab.droidconandroid.shared.network.dao.UserAccount> iterator = event.speakers
                                .iterator();
                        int speakerCount = 0;

                        while(iterator.hasNext())
                        {
                            co.touchlab.droidconandroid.shared.network.dao.UserAccount ua = iterator
                                    .next();

                            UserAccount userAccount = userAccountDao.queryForId(ua.id);

                            if(userAccount == null)
                            {
                                userAccount = new UserAccount();
                            }

                            UserDataHelper.userAccountToDb(ua, userAccount);

                            userAccountDao.createOrUpdate(userAccount);

                            List<EventSpeaker> resultList = new Where<EventSpeaker>(eventSpeakerDao)
                                    .and()
                                    .eq("event_id", event.id)
                                    .eq("userAccount_id", userAccount.id)
                                    .query()
                                    .list();

                            EventSpeaker eventSpeaker = ((resultList.size() == 0)
                                    ? new EventSpeaker()
                                    : resultList.get(0));

                            eventSpeaker.event = event;
                            eventSpeaker.userAccount = userAccount;
                            eventSpeaker.displayOrder = speakerCount++;

                            eventSpeakerDao.createOrUpdate(eventSpeaker);
                        }
                    }

                }

                // don't clear the db if no events are returned
                if(! foundEvents.isEmpty())
                {
                    helper.deleteEventsNotIn(foundEvents);
                }

                if(blocks.size() > 0)
                {
                    //Dump all of them first
                    blockDao.delete(blockDao.queryForAll().list());

                    for(co.touchlab.droidconandroid.shared.network.dao.Block block : blocks)
                    {
                        block.startDateLong = TimeUtils.DATE_FORMAT.get()
                                .parse(block.startDate)
                                .getTime();
                        block.endDateLong = TimeUtils.DATE_FORMAT.get()
                                .parse(block.endDate)
                                .getTime();

                        blockDao.createOrUpdate(block);
                    }
                }

            }
            catch(SQLException | ParseException e)
            {
                throw new RuntimeException(e);
            }
            return null;
        });
    }
}