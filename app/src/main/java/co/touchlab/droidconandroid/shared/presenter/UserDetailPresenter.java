package co.touchlab.droidconandroid.shared.presenter;

import com.google.j2objc.annotations.Weak;

import co.touchlab.droidconandroid.shared.network.dao.UserAccountInfo;
import co.touchlab.droidconandroid.shared.tasks.FindUserTask;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UserDetailPresenter {

    @Weak
    private UserDetailHost host;
    private FindUserTask task;
    private CompositeDisposable disposables = new CompositeDisposable();

    public UserDetailPresenter(UserDetailHost host, FindUserTask task) {
        this.host = host;
        this.task = task;
    }

    public void findUser(@NonNull final Long userId) {
        disposables.add(Observable.zip(task.loadUserAccount(userId),
                task.loadUserInfo(userId), UserAccountInfo::new)
                .flatMap(task::saveUserResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userAccount -> host.onUserFound(userAccount),
                        throwable -> host.findUserError()));
    }

    public void unregister() {
        host = null;
        disposables.clear();
    }

}
