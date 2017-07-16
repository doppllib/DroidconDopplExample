package co.touchlab.droidconandroid.shared.tasks;

import android.content.Context;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.SQLException;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.network.DataHelper;
import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import co.touchlab.droidconandroid.shared.presenter.AppManager;
import retrofit.RetrofitError;
import retrofit2.Response;
import retrofit2.Retrofit;

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
                           Retrofit restAdapter = DataHelper.makeRetrofit2Client(AppManager.getPlatformClient().baseUrl());
                           FindUserRequest findUserRequest = restAdapter
                                   .create(FindUserRequest.class);
                           try
                           {
                               Response<UserInfoResponse> response = findUserRequest.getUserInfo(code).execute();
                               if(response.code() == 404)
                                   errorStringCode = "error_user_not_found";
                               else
                                   return response.body();
                           }
                           catch(IOException e)
                           {
                               errorStringCode = "network_error";
                           }
                           return null;
                       }
                   }

        );
    }
}
