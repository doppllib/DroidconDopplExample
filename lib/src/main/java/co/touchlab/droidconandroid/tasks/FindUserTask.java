package co.touchlab.droidconandroid.tasks;
import android.content.Context;

import java.net.HttpURLConnection;
import java.sql.SQLException;

import co.touchlab.droidconandroid.data.DatabaseHelper;
import co.touchlab.droidconandroid.data.UserAccount;
import co.touchlab.droidconandroid.network.DataHelper;
import co.touchlab.droidconandroid.network.FindUserRequest;
import co.touchlab.droidconandroid.network.dao.UserInfoResponse;
import co.touchlab.droidconandroid.presenter.AppManager;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by kgalligan on 4/8/16.
 */
public class FindUserTask extends AbstractFindUserTask
{
    final String code;

    public FindUserTask(String code)
    {
        this.code = code;
    }

    @Override
    protected void run(final Context context) throws Throwable
    {
        handleData(context, new LoadFromDb()
                   {
                       @Override
                       public UserAccount load() throws SQLException
                       {
                           DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
                           return UserAccount.findByCode(databaseHelper, code);
                       }
                   }, new LoadUserInfo()
                   {
                       @Override
                       public UserInfoResponse load()
                       {
                           RestAdapter restAdapter = DataHelper.makeRequestAdapter(context,
                                                                                   AppManager.getPlatformClient());
                           FindUserRequest findUserRequest = restAdapter
                                   .create(FindUserRequest.class);
                           try
                           {
                               return findUserRequest.getUserInfo(code);
                           }
                           catch(RetrofitError e)
                           {
                               if(e.getResponse().getStatus() == HttpURLConnection.HTTP_NOT_FOUND)
                               {
                                   errorStringCode = "error_user_not_found";
                               }
                               else if(e.getKind() == RetrofitError.Kind.NETWORK)
                               {
                                   errorStringCode = "network_error";
                               }
                               else
                               {
                                   throw new RuntimeException(e);
                               }
                           }
                           return null;
                       }
                   }

        );
    }
}
