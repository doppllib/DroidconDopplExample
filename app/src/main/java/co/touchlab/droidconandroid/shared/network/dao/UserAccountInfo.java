package co.touchlab.droidconandroid.shared.network.dao;


import co.touchlab.droidconandroid.shared.data.UserAccount;

public class UserAccountInfo
{
    private UserAccount      userAccountFromDb;
    private UserInfoResponse userAccountFromNetwork;

    public UserAccountInfo(UserAccount userAccountFromDb, UserInfoResponse userAccountFromNetwork)
    {
        this.userAccountFromDb = userAccountFromDb;
        this.userAccountFromNetwork = userAccountFromNetwork;
    }

    public UserAccount getUserAccountFromDb()
    {
        return userAccountFromDb;
    }

    public void setUserAccountFromDb(UserAccount userAccountFromDb)
    {
        this.userAccountFromDb = userAccountFromDb;
    }

    public UserInfoResponse getUserAccountFromNetwork()
    {
        return userAccountFromNetwork;
    }

    public void setUserAccountFromNetwork(UserInfoResponse userAccountFromNetwork)
    {
        this.userAccountFromNetwork = userAccountFromNetwork;
    }
}
