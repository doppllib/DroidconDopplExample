package co.touchlab.droidconandroid.shared.network.dao;

import co.touchlab.droidconandroid.shared.data.UserAccount;

public class UserAccountInfo {
    public UserAccount userAccount;
    public UserInfoResponse userInfoResponse;

    public UserAccountInfo(UserAccount userAccount, UserInfoResponse userInfoResponse) {
        this.userAccount = userAccount;
        this.userInfoResponse = userInfoResponse;
    }
}
