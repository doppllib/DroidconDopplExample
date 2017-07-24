package co.touchlab.droidconandroid.shared.presenter;

import co.touchlab.droidconandroid.shared.data2.UserAccount;
import io.reactivex.annotations.NonNull;

public interface UserDetailHost {

    void findUserError();

    void onUserFound(@NonNull UserAccount userAccount);
}
