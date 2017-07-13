package co.touchlab.droidconandroid.shared.presenter;

import com.google.j2objc.annotations.Weak;

import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import co.touchlab.droidconandroid.shared.tasks.FindUserTask;
import co.touchlab.droidconandroid.shared.utils.UserDataHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserDetailPresenter {

    @Weak
    private UserDetailHost host;
    private FindUserTask task;
    private CompositeDisposable subscriptions = new CompositeDisposable();

    public UserDetailPresenter(UserDetailHost host, FindUserTask task) {
        this.host = host;
        this.task = task;
    }

    public void findUser(@NonNull final Long userId) {

        subscriptions.add(task.loadUserInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserInfoResponse>() {
                    @Override
                    public void accept(@NonNull UserInfoResponse userInfoResponse) throws Exception {
                        UserAccount user = new UserAccount();
                        UserDataHelper.userAccountToDb(userInfoResponse.user, user);
                        host.onUserFound(user);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        host.findUserError();
                    }
                }));

    }

    public void unregister() {
        host = null;
        subscriptions.clear();
    }

}
