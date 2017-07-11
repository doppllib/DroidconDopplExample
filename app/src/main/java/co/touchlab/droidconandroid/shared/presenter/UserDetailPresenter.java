package co.touchlab.droidconandroid.shared.presenter;

import com.google.j2objc.annotations.Weak;

import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.tasks.FindUserTask;
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

    public void findUser(@android.support.annotation.NonNull String userCode) {
        subscriptions.add(task.loadUserAccount(userCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserAccount>() {
                    @Override
                    public void accept(@NonNull UserAccount userAccount) throws Exception {
                        host.onUserFound(userAccount);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        host.findUserError();
                    }
                }));

        // TODO: Make the other call to save user to db
    }

    public void unregister() {
        host = null;
        subscriptions.clear();
    }


}
