package co.touchlab.droidconandroid.shared.presenter;

import com.google.j2objc.annotations.Weak;

import co.touchlab.droidconandroid.shared.tasks.FindUserTask;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UserDetailPresenter {

    @Weak
    private UserDetailHost host;
    private FindUserTask task;
    private CompositeDisposable disposables = new CompositeDisposable();

    public UserDetailPresenter(FindUserTask task) {
        this.task = task;
    }

    public void register(UserDetailHost host) {
        this.host = host;
    }

    public void findUser(@NonNull final Long userId) {
        disposables.add(task.loadUserAccount(userId)
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
