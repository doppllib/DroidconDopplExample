package co.touchlab.droidconandroid.shared.data2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import co.touchlab.droidconandroid.shared.data2.Event;

@Dao
public interface EventDao {

    @Query("SELECT * FROM Event WHERE id = :eventId")
    Event getEventForId(int eventId);

    @Query("SELECT * FROM Event")
    List<Event> getEvents();

    @Query("SELECT * FROM Event WHERE rsvpUuid NOT NULL")
    List<Event> rsvpUuidNotNull();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(Event event);

    @Update
    void updateEvent(Event event);

    @Delete
    void deleteAll(List<Event> events);

}
