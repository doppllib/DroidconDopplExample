package co.touchlab.droidconandroid.shared.tasks;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.dao.UserAccountInfo;
import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import co.touchlab.droidconandroid.shared.utils.UserDataHelper;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit.RestAdapter;

/**
 * Created by kgalligan on 4/8/16.
 */
public class FindUserTask {
    private final DatabaseHelper helper;
    private final RestAdapter restAdapter;

    public FindUserTask(DatabaseHelper helper, RestAdapter restAdapter) {
        this.helper = helper;
        this.restAdapter = restAdapter;
    }

    public Observable<UserAccount> loadUserAccount(final long userId) {
        return Observable.fromCallable(() -> UserAccount.findByCode(helper, userId));
    }

    public Observable<UserInfoResponse> loadUserInfo(final long userId) {
        FindUserRequest findUserRequest = restAdapter.create(FindUserRequest.class);
        return RxJavaInterop.toV2Observable(findUserRequest.getUserInfo(userId));
    }

    public Observable<UserAccount> saveUserResponse(UserAccountInfo userAccountInfo) {
        final UserAccount newDbUser = new UserAccount();
        UserAccount userAccount = userAccountInfo.userAccount;
        UserDataHelper.userAccountToDb(userAccountInfo.userInfoResponse.user, newDbUser);

        if (userAccount == null || !userAccount.equals(newDbUser)) {
            return Completable.fromAction(() -> helper.getUserAccountDao().createOrUpdate(newDbUser))
                    .andThen(Observable.just(newDbUser));
        }

        return Observable.just(userAccount);
    }

}
