package co.touchlab.droidconandroid.shared.interactors;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.dao.NetworkUserAccount;
import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by kgalligan on 4/8/16.
 */
public class FindUserInteractor
{
    private final DatabaseHelper  helper;
    private final FindUserRequest request;
    private final long            userId;
    private Observable<UserAccount> userAccountObservable = null;

    public FindUserInteractor(DatabaseHelper helper, FindUserRequest request, long userId)
    {
        this.helper = helper;
        this.request = request;
        this.userId = userId;
    }

    public Observable<UserAccount> loadUserAccount()
    {
        if(userAccountObservable == null)
        {
            userAccountObservable = loadUserInfo(userId).flatMap(info -> saveUserResponse(info.user))
                    .onErrorResumeNext(getUserFromDb(userId))
                    .cache();
        }

        return userAccountObservable;
    }

    private Observable<UserAccount> getUserFromDb(final long userId)
    {
        return helper.getUserAccountForId(userId)
                .toObservable()
                .doOnError(e -> userAccountObservable = null);
    }

    private Observable<UserInfoResponse> loadUserInfo(final long userId)
    {
        return request.getUserInfo(userId).doOnError(e -> userAccountObservable = null);
    }

    private Observable<UserAccount> saveUserResponse(NetworkUserAccount networkUserAccount)
    {
        final UserAccount newDbUser = new UserAccount();
        helper.userAccountToDb(networkUserAccount, newDbUser);

        return Completable.fromAction(() -> helper.saveUserAccount(newDbUser))
                .andThen(Observable.just(newDbUser));
    }

}
