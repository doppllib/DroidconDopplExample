package co.touchlab.droidconandroid.shared.presenter;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.google.j2objc.annotations.Weak;

import co.touchlab.droidconandroid.shared.tasks.FindUserTask;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UserDetailViewModel extends ViewModel {

    @Weak
    private UserDetailHost host;
    private FindUserTask task;
    private CompositeDisposable disposables = new CompositeDisposable();

    private UserDetailViewModel(FindUserTask task) {
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
        disposables.clear();
        host = null;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final FindUserTask task;

        public Factory(FindUserTask task) {
            this.task = task;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new UserDetailViewModel(task);
        }
    }

}
