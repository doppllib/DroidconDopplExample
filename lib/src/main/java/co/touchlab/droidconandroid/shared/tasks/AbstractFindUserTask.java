package co.touchlab.droidconandroid.shared.tasks;
import android.content.Context;

import java.sql.SQLException;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import co.touchlab.droidconandroid.shared.utils.UserDataHelper;

/**
 * Created by kgalligan on 4/8/16.
 */
public abstract class AbstractFindUserTask extends Task
{
    public String errorStringCode;
    public UserAccount user;

    @Override
    protected boolean handleError(Context context, Throwable e)
    {
        CrashReport.logException(e);
        errorStringCode = "error_unknown";
        return true;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
    }

    interface LoadFromDb
    {
        UserAccount load() throws SQLException;
    }

    interface LoadUserInfo
    {
        UserInfoResponse load();
    }

    void handleData(Context context, LoadFromDb loadFromDb, LoadUserInfo loadUserInfo) throws SQLException
    {
        user = loadFromDb.load();

        if (user != null)
        {
            EventBusExt.getDefault().post(this);
        }

        UserInfoResponse response = loadUserInfo.load();
        if(response != null)
        {
            UserAccount updatedUser = saveUserResponse(context, user, response);

            if (updatedUser == null)
            {
                //                    cancelPost()
            }
            else
            {
                this.user = updatedUser;
            }
        }

    }

    public boolean isError()
    {
        return errorStringCode != null;
    }

    public static UserAccount saveUserResponse(Context context, UserAccount user, UserInfoResponse response) throws SQLException
    {
        UserAccount newDbUser = new UserAccount();
        UserDataHelper.userAccountToDb(response.user, newDbUser);

        if(user == null || !user.equals(newDbUser))
        {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
            databaseHelper.getUserAccountDao().createOrUpdate(newDbUser);

            return newDbUser;
        }

        return null;
    }
}
