package co.touchlab.droidconandroid.shared.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.Update;

import java.util.List;

import co.touchlab.droidconandroid.shared.data.Event;
import co.touchlab.droidconandroid.shared.data.EventSpeaker;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import io.reactivex.Flowable;

@Dao
public interface EventDao {

    @Query("SELECT * FROM Event WHERE id = :eventId")
    Event getEventForId(long eventId);

    @Query("SELECT rsvpUuid FROM Event WHERE id = :eventId")
    String getRsvpUuidForEventWithId(long eventId);

    @Query("SELECT * FROM Event")
    List<Event> getEvents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(Event event);

    @Update
    void updateEvent(Event event);

    @Delete
    void deleteAll(List<Event> events);

}
