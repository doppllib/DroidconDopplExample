package co.touchlab.droidconandroid.shared.presenter;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.VisibleForTesting;

import com.google.j2objc.annotations.Weak;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.interactors.FindUserInteractor;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UserDetailViewModel extends ViewModel
{

    @Weak
    private UserDetailHost     host;
    private FindUserInteractor task;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Observable<UserAccount> userAccountObservable;

    private UserDetailViewModel(FindUserInteractor task)
    {
        this.task = task;
    }

    public void register(UserDetailHost host)
    {
        this.host = host;
    }

    public void findUser(final long userId)
    {
        if(userAccountObservable == null)
        {
            userAccountObservable = task.loadUserAccount(userId).cache();
        }

        disposables.add(userAccountObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e -> userAccountObservable = null)
                .subscribe(userAccount -> host.onUserFound(userAccount),
                        throwable -> host.findUserError()));
    }

    public void unregister()
    {
        disposables.clear();
        host = null;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {
        @Inject
        FindUserInteractor task;

        public Factory()
        {

        }

        @VisibleForTesting
        public Factory(FindUserInteractor task)
        {
            this.task = task;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new UserDetailViewModel(task);
        }
    }

}
