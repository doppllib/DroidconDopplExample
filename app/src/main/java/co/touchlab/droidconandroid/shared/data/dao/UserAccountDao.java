package co.touchlab.droidconandroid.shared.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import co.touchlab.droidconandroid.shared.data.UserAccount;
import io.reactivex.Flowable;

@Dao
public interface UserAccountDao {

    String SELECT_USER = "SELECT * FROM UserAccount WHERE id = :userId";

    @Query(SELECT_USER)
    Flowable<UserAccount> flowUserAccount(long userId);

    @Query(SELECT_USER)
    UserAccount getUserAccount(long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(UserAccount userAccount);
}
