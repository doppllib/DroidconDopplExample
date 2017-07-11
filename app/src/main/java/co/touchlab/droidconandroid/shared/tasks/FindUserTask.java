package co.touchlab.droidconandroid.shared.tasks;

import java.util.concurrent.Callable;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import co.touchlab.droidconandroid.shared.utils.UserDataHelper;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
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

    public Single<UserAccount> loadUserAccount(final String userCode) {
        return Single.fromCallable(new Callable<UserAccount>() {
            @Override
            public UserAccount call() throws Exception {
                return UserAccount.findByCode(helper, userCode);
            }
        });
    }

    public Single<UserInfoResponse> loadUserInfo(final String userCode) {
        FindUserRequest findUserRequest = restAdapter.create(FindUserRequest.class);
        return RxJavaInterop.toV2Single(findUserRequest.getUserInfoSingle(userCode));
    }

    public Completable saveUserResponse(UserAccount user, UserInfoResponse response) {
        final UserAccount newDbUser = new UserAccount();
        UserDataHelper.userAccountToDb(response.user, newDbUser);

        if (user == null || !user.equals(newDbUser)) {
            return Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    helper.getUserAccountDao().createOrUpdate(newDbUser);
                }
            });
        }

        return Completable.complete();
    }

}
