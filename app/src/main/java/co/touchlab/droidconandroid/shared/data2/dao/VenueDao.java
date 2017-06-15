package co.touchlab.droidconandroid.shared.data2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import co.touchlab.droidconandroid.shared.data2.Venue;

@Dao
public interface VenueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(Venue venue);
}
