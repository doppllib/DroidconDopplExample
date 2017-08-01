package co.touchlab.droidconandroid.shared.interactors;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

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
@Singleton
public class FindUserInteractor
{
    private final DatabaseHelper  helper;
    private final FindUserRequest request;
    private final Map<Long, Observable<UserAccount>> cache = new HashMap<>();

    @Inject
    public FindUserInteractor(DatabaseHelper helper, FindUserRequest request)
    {
        this.helper = helper;
        this.request = request;
    }

    public Observable<UserAccount> loadUserAccount(final long userId)
    {
        if(cache.containsKey(userId))
        {
            return cache.get(userId);
        }

        Observable<UserAccount> userAccountObservable = loadUserInfo(userId).flatMap(info -> saveUserResponse(
                info.user)).onErrorResumeNext(getUserFromDb(userId)).cache();

        cache.put(userId, userAccountObservable);
        return userAccountObservable;
    }

    private Observable<UserAccount> getUserFromDb(final long userId)
    {
        return helper.getUserAccountForId(userId)
                .toObservable()
                .doOnError(e -> removeFromCache(userId));
    }

    private Observable<UserInfoResponse> loadUserInfo(final long userId)
    {
        return request.getUserInfo(userId).doOnError(e -> removeFromCache(userId));
    }

    private void removeFromCache(long userId)
    {
        if(cache.containsKey(userId))
        {
            cache.remove(userId);
        }
    }

    private Observable<UserAccount> saveUserResponse(NetworkUserAccount networkUserAccount)
    {
        final UserAccount newDbUser = new UserAccount();
        helper.userAccountToDb(networkUserAccount, newDbUser);

        return Completable.fromAction(() -> helper.saveUserAccount(newDbUser))
                .andThen(Observable.just(newDbUser));
    }

}
