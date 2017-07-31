package co.touchlab.droidconandroid.shared.presenter;

import org.jetbrains.annotations.NotNull;

import co.touchlab.droidconandroid.shared.data.UserAccount;

public interface UserDetailHost {

    void findUserError();

    void onUserFound(@NotNull UserAccount userAccount);
}
