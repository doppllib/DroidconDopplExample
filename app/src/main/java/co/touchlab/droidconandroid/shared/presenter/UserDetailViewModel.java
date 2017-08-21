package co.touchlab.droidconandroid.shared.presenter;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.VisibleForTesting;

import com.google.j2objc.annotations.Weak;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.interactors.FindUserInteractor;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

public class UserDetailViewModel extends ViewModel
{

    @Weak
    private UserDetailHost     host;
    private FindUserInteractor task;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Observable<UserAccount> userAccountObservable;
    private final ObservableTransformer<UserAccount, UserAccount> transformer;

    private UserDetailViewModel(FindUserInteractor task, ObservableTransformer transformer)
    {
        this.task = task;
        this.transformer = transformer;
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

        disposables.add(userAccountObservable.compose(transformer)
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

        @Inject
        ObservableTransformer transformer;

        private Factory()
        {

        }

        @VisibleForTesting
        public Factory(FindUserInteractor task, ObservableTransformer transformer)
        {
            this.task = task;
            this.transformer = transformer;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new UserDetailViewModel(task, transformer);
        }
    }

    public static Factory factory()
    {
        Factory factory = new Factory();
        AppManager.getInstance().getAppComponent().inject(factory);

        return factory;
    }

    public static UserDetailViewModel forIos()
    {
        return factory().create(UserDetailViewModel.class);
    }
}
