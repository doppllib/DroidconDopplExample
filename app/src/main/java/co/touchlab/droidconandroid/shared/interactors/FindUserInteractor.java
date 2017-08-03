package co.touchlab.droidconandroid.shared.interactors;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.dao.NetworkUserAccount;
import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by kgalligan on 4/8/16.
 */
@Singleton
public class FindUserInteractor
{
    private final DatabaseHelper  helper;
    private final FindUserRequest request;

    @Inject
    public FindUserInteractor(DatabaseHelper helper, FindUserRequest request)
    {
        this.helper = helper;
        this.request = request;
    }

    public Observable<UserAccount> loadUserAccount(final long userId)
    {

        return loadUserInfo(userId).flatMap(info -> saveUserResponse(info.user))
                .onErrorResumeNext(getUserFromDb(userId));
    }

    private Observable<UserAccount> getUserFromDb(final long userId)
    {
        return helper.getUserAccountForId(userId).toObservable();
    }

    private Observable<UserInfoResponse> loadUserInfo(final long userId)
    {
        return request.getUserInfo(userId);
    }

    private Observable<UserAccount> saveUserResponse(NetworkUserAccount networkUserAccount)
    {
        final UserAccount newDbUser = new UserAccount();
        helper.userAccountToDb(networkUserAccount, newDbUser);

        return Completable.fromAction(() -> helper.saveUserAccount(newDbUser))
                .andThen(Observable.just(newDbUser));
    }

}
