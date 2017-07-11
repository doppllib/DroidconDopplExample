package co.touchlab.droidconandroid.shared.presenter;

import co.touchlab.droidconandroid.shared.data.UserAccount;

public interface UserDetailHost {

    void findUserError();

    void onUserFound(UserAccount userAccount);
}
