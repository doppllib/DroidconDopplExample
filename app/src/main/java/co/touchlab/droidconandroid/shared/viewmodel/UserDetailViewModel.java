package co.touchlab.droidconandroid.shared.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.google.j2objc.annotations.Weak;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.interactors.FindUserInteractor;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class UserDetailViewModel extends ViewModel
{
    private FindUserInteractor interactor;

    private CompositeDisposable disposables = new CompositeDisposable();
    private FlowableTransformer<UserAccount, UserAccount> transformer;

    public interface Host
    {
        void findUserError();

        void onUserFound(@NonNull UserAccount userAccount);
    }

    private UserDetailViewModel(FindUserInteractor interactor, FlowableTransformer transformer)
    {
        this.interactor = interactor;
        this.transformer = transformer;
    }

    public void wire(Host host, final long userId)
    {
        disposables.add(interactor.loadUserAccount(userId)
                .compose(transformer)
                .subscribe(host:: onUserFound, throwable -> host.findUserError()));
    }

    public void unwire()
    {
        disposables.clear();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {
        @Inject
        FindUserInteractor    task;
        @Inject
        FlowableTransformer transformer;

        Factory()
        {

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
