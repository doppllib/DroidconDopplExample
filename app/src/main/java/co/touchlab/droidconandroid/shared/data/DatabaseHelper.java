package co.touchlab.droidconandroid.shared.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import javax.annotation.Nonnull;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import co.touchlab.droidconandroid.shared.data.staff.EventAttendee;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import co.touchlab.squeaky.dao.Dao;
import co.touchlab.squeaky.db.sqlite.SQLiteDatabaseImpl;
import co.touchlab.squeaky.db.sqlite.SqueakyOpenHelper;
import co.touchlab.squeaky.table.TableUtils;

/**
 * Created by kgalligan on 6/28/14.
 */
public class DatabaseHelper extends SqueakyOpenHelper
{

    private static final String DATABASE_FILE_NAME = "droidcon";
    public static final  int    BASELINE           = 3;

    private static DatabaseHelper instance;

    // @reminder Ordering matters, create foreign key dependant classes later
    private final Class[] tableClasses = new Class[] {Venue.class, Event.class, Block.class, UserAccount.class, EventAttendee.class, EventSpeaker.class};

    private DatabaseHelper(Context context)
    {
        super(context, DATABASE_FILE_NAME, null, BASELINE);
    }

    @Nonnull
    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DatabaseHelper(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            TableUtils.createTables(new SQLiteDatabaseImpl(db), tableClasses);
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            TableUtils.dropTables(new SQLiteDatabaseImpl(db), false, Event.class);
            TableUtils.createTables(new SQLiteDatabaseImpl(db), Event.class);
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Nonnull
    public Dao<Venue> getVenueDao()
    {
        return getDao(Venue.class);
    }

    @Nonnull
    public Dao<Event> getEventDao()
    {
        return getDao(Event.class);
    }

    @Nonnull
    public Dao<UserAccount> getUserAccountDao()
    {
        return getDao(UserAccount.class);
    }

    @Nonnull
    public Dao<EventSpeaker> getEventSpeakerDao()
    {
        return getDao(EventSpeaker.class);
    }

    @Nonnull
    public Dao<Block> getBlockDao()
    {
        return getDao(Block.class);
    }

    public void deleteEventsNotIn(Set<Long> goodStuff) throws SQLException
    {
        final Dao<Event> eventDao = getEventDao();
        final List<Event> allEvents = eventDao.queryForAll().list();
        final Iterator<Event> iterator = allEvents.iterator();
        while(iterator.hasNext())
        {
            Event event = iterator.next();
            if(goodStuff.contains(event.id))
                iterator.remove();
        }

        if(allEvents.size() > 0)
            eventDao.delete(allEvents);
    }

    /**
     * @param transaction .
     * @throws RuntimeException on {@link SQLException}
     */
    public void performTransactionOrThrowRuntime(Callable<Void> transaction)
    {
        SQLiteDatabase db = getWritableDatabase();
        try
        {
            db.beginTransaction();
            transaction.call();
            db.setTransactionSuccessful();
        }
        catch(Exception e)
        {
            AppManager.getPlatformClient().logException(e);
            throw new RuntimeException(e);
        }
        finally
        {
            db.endTransaction();
        }
    }
}
