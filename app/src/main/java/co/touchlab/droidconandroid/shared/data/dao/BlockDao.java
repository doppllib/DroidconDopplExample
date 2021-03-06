package co.touchlab.droidconandroid.shared.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import co.touchlab.droidconandroid.shared.data.Block;

@Dao
public interface BlockDao {

    @Query("SELECT * FROM Block")
    List<Block> getBlocks();

    // Potentially annoying to delete this way, could truncate the table instead
    @Delete
    void deleteAll(List<Block> blocks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(Block block);
}
