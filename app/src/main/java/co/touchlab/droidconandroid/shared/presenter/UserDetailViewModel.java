package co.touchlab.droidconandroid.shared.presenter;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.google.j2objc.annotations.Weak;

import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.tasks.FindUserTask;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

public class UserDetailViewModel extends ViewModel {

    @Weak
    private UserDetailHost host;
    private FindUserTask task;
    private ReplaySubject<UserAccount> accountBehaviorSubject = ReplaySubject.create();
    private Observable<UserAccount> accountObservable = null;
    private CompositeDisposable disposables = new CompositeDisposable();

    private UserDetailViewModel(FindUserTask task) {
        this.task = task;
    }

    public void register(UserDetailHost host) {
        this.host = host;
    }

    public void findUser(@NonNull final Long userId) {
        if (accountObservable == null) {
            Log.d("TAG", "Requesting the account");
            accountObservable = task.loadUserAccount(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            accountObservable.subscribe(account -> accountBehaviorSubject.onNext(account),
                    e -> {
                        Log.d("TAG", "Observable " + e.getMessage());
                        accountBehaviorSubject.onError(e);
                    });
        }

        accountBehaviorSubject.subscribe(userAccount -> {
                    host.onUserFound(userAccount);
                    Log.d("TAG", "Getting from the subject");
                },
                throwable -> {
                    host.findUserError();
                    Log.d("TAG", "Subject " + throwable.getMessage());
                });

    }

    @Override
    protected void onCleared() {
        super.onCleared();
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
