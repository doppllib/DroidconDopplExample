package co.touchlab.droidconandroid.tasks.persisted;
import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.helper.RetrofitPersistedTask;
import co.touchlab.android.threading.tasks.persisted.PersistedTask;
import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.data.AppPrefs;
import co.touchlab.droidconandroid.data.Block;
import co.touchlab.droidconandroid.data.DatabaseHelper;
import co.touchlab.droidconandroid.data.Event;
import co.touchlab.droidconandroid.data.EventSpeaker;
import co.touchlab.droidconandroid.data.UserAccount;
import co.touchlab.droidconandroid.data.Venue;
import co.touchlab.droidconandroid.network.DataHelper;
import co.touchlab.droidconandroid.network.RefreshScheduleDataRequest;
import co.touchlab.droidconandroid.network.dao.Convention;
import co.touchlab.droidconandroid.presenter.AppManager;
import co.touchlab.droidconandroid.presenter.PlatformClient;
import co.touchlab.droidconandroid.utils.TimeUtils;
import co.touchlab.droidconandroid.utils.UserDataHelper;
import co.touchlab.squeaky.dao.Dao;
import co.touchlab.squeaky.stmt.Where;
import retrofit.RestAdapter;

/**
 * Created by kgalligan on 4/7/16.
 */
public class RefreshScheduleData extends RetrofitPersistedTask
{
    public static void callMe(Context c)
    {
        PersistedTaskQueueFactory.getInstance(c).execute(new RefreshScheduleData());
    }

    @Override
    protected void runNetwork(Context context)
    {
        final PlatformClient platformClient = AppManager.getPlatformClient();
        RestAdapter restAdapter = DataHelper.makeRequestAdapter(context, platformClient);

        RefreshScheduleDataRequest request = restAdapter.create(RefreshScheduleDataRequest.class);

        Convention convention = request.getScheduleData(platformClient.getConventionId());
        saveConventionData(context, convention);

        AppPrefs.getInstance(context).setRefreshTime(System.currentTimeMillis());

    }

    @Override
    protected boolean handleError(Context context, Throwable e)
    {
        CrashReport.logException(e);
        return true;
    }

    @Override
    protected String logSummary()
    {
        return getClass().getSimpleName();
    }

    @Override
    protected boolean same(PersistedTask persistedTask)
    {
        return persistedTask instanceof RefreshScheduleData;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
    }

    public static void saveConventionData(final Context context, final Convention convention) {
        if (convention == null)
            throw new IllegalStateException("No convention results");

        final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        databaseHelper.performTransactionOrThrowRuntime(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                Dao<Event> eventDao = databaseHelper.getEventDao();
                Dao<Venue> venueDao = databaseHelper.getVenueDao();
                Dao<Block> blockDao = databaseHelper.getBlockDao();
                Dao<UserAccount> userAccountDao = databaseHelper.getUserAccountDao();
                Dao<EventSpeaker> eventSpeakerDao = databaseHelper.getEventSpeakerDao();

                AppPrefs.getInstance(context).setConventionStartDate(convention.startDate);
                AppPrefs.getInstance(context).setConventionEndDate(convention.endDate);

                List<co.touchlab.droidconandroid.network.dao.Venue> venues = convention.venues;
                List<co.touchlab.droidconandroid.network.dao.Block> blocks = convention.blocks;
                Set<Long> foundEvents = new HashSet<Long>();
                try {

                    for (co.touchlab.droidconandroid.network.dao.Venue venue : venues) {
                        venueDao.createOrUpdate(venue);
                        final Iterator<co.touchlab.droidconandroid.network.dao.Event> venueEventsIterator = venue.events
                                .iterator();
                        while(venueEventsIterator.hasNext())
                        {
                            co.touchlab.droidconandroid.network.dao.Event event = venueEventsIterator
                                    .next();

                            foundEvents.add(event.id);
                            Event dbEvent = eventDao.queryForId(event.id);
                            event.venue = venue;

                            if (StringUtils.isEmpty(event.startDate) || StringUtils.isEmpty(event.endDate))
                                continue;

                            event.startDateLong = TimeUtils.DATE_FORMAT.get().parse(event.startDate).getTime();
                            event.endDateLong = TimeUtils.DATE_FORMAT.get().parse(event.endDate).getTime();

                            if (dbEvent != null)
                                event.rsvpUuid = dbEvent.rsvpUuid;

                            eventDao.createOrUpdate(event);

                            Iterator<co.touchlab.droidconandroid.network.dao.UserAccount> iterator = event.speakers.iterator();
                            int speakerCount = 0;

                            while(iterator.hasNext())
                            {
                                co.touchlab.droidconandroid.network.dao.UserAccount ua = iterator
                                        .next();

                                UserAccount userAccount = userAccountDao.queryForId(ua.id);

                                if (userAccount == null) {
                                    userAccount = new UserAccount();
                                }

                                UserDataHelper.userAccountToDb(ua, userAccount);

                                userAccountDao.createOrUpdate(userAccount);

                                List<EventSpeaker> resultList = new Where<EventSpeaker>(eventSpeakerDao)
                                .and()
                                        .eq("event_id", event.id)
                                .eq("userAccount_id", userAccount.id)
                                .query().list();

                                EventSpeaker eventSpeaker = ((resultList.size() == 0) ? new EventSpeaker() : resultList.get(0));

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
                        databaseHelper.deleteEventsNotIn(foundEvents);
                    }

                    if(blocks.size() > 0) {
                        //Dump all of them first
                        blockDao.delete(blockDao.queryForAll().list());

                        for (co.touchlab.droidconandroid.network.dao.Block block : blocks) {
                            block.startDateLong = TimeUtils.DATE_FORMAT.get().parse(block.startDate).getTime();
                            block.endDateLong = TimeUtils.DATE_FORMAT.get().parse(block.endDate).getTime();

                            blockDao.createOrUpdate(block);
                        }
                    }

                } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

                return null;
            }
        });
    }
}
