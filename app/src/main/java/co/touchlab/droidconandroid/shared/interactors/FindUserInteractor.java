package co.touchlab.droidconandroid.shared.interactors;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.dao.UserAccountInfo;
import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by kgalligan on 4/8/16.
 */
public class FindUserInteractor {
    private final DatabaseHelper  helper;
    private final FindUserRequest request;
    private final long            userId;
    private Observable<UserAccount> userAccountObservable = null;

    public FindUserInteractor(DatabaseHelper helper, FindUserRequest request, long userId) {
        this.helper = helper;
        this.request = request;
        this.userId = userId;
    }

    public Observable<UserAccount> loadUserAccount() {
        if (userAccountObservable == null) {
            Observable<UserAccount> accountObservable = helper.getUserAccountForId(userId)
                    .toObservable();

            userAccountObservable =
                    Observable.zip(accountObservable, loadUserInfo(userId), UserAccountInfo::new)
                            .flatMap(this::saveUserResponse)
                            .cache();
        }

        return userAccountObservable;
    }

    private Observable<UserInfoResponse> loadUserInfo(final long userId) {
        return request.getUserInfo(userId);
    }

    private Observable<UserAccount> saveUserResponse(UserAccountInfo userAccountInfo) {
        final UserAccount networkUser = userAccountInfo.getUserAccountFromNetwork().user;
        final UserAccount dbUser = userAccountInfo.getUserAccountFromDb();

        if(dbUser == null || ! dbUser.equals(networkUser))
        {
            return Completable.fromAction(() -> helper.saveUserAccount(networkUser))
                    .andThen(Observable.just(networkUser));
        }

        return Observable.just(dbUser);
    }

}
