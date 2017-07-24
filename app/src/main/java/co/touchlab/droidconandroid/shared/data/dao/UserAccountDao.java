package co.touchlab.droidconandroid.shared.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import co.touchlab.droidconandroid.shared.data.UserAccount;

@Dao
public interface UserAccountDao {

    @Query("SELECT * FROM UserAccount WHERE id = :userId")
    UserAccount getUserAccount(long userId);

    @Query("SELECT * FROM UserAccount WHERE userCode = :code")
    List<UserAccount> getUsersWithCode(String code);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(UserAccount userAccount);
}
