package co.touchlab.droidconandroid.shared.viewmodel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.interactors.FindUserInteractor;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;


import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDetailViewModelTest
{
    @Mock
    FindUserInteractor interactor;
    @Mock
    UserDetailHost     host;
    private UserDetailViewModel.Factory factory;
    private UserDetailViewModel         viewModel;
    private UserAccount user = new UserAccount();

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        ObservableTransformer<UserAccount, UserAccount> transformer = upstream -> upstream.subscribeOn(
                Schedulers.trampoline()).observeOn(Schedulers.trampoline());
        factory = new UserDetailViewModel.Factory();
        factory.task = interactor;
        factory.transformer = transformer;
        viewModel = factory.create(UserDetailViewModel.class);
        viewModel.register(host);
    }

    @After
    public void tearDown()
    {
        viewModel.unregister();
    }

    @Test
    public void whenSuccessGettingUserDetail_shouldShowResults()
    {
        when(interactor.loadUserAccount(anyInt())).thenReturn(Observable.just(user));

        viewModel.findUser(100);

        verify(host).onUserFound(user);
    }

    @Test
    public void whenErrorGettingUserDetail_shouldShowError()
    {
        when(interactor.loadUserAccount(anyInt())).thenReturn(Observable.error(new Throwable()));

        viewModel.findUser(100);

        verify(host).findUserError();
    }

    @Test
    public void whenLoadUserSuccessful_ShouldCacheResult()
    {
        when(interactor.loadUserAccount(anyInt())).thenReturn(Observable.just(user));

        viewModel.findUser(100);

        when(interactor.loadUserAccount(anyInt())).thenReturn(Observable.just(user));

        viewModel.findUser(100);

        verify(interactor, times(1)).loadUserAccount(100);
    }

    @Test
    public void whenErrorLoadingUser_ShouldNotCacheResult()
    {
        when(interactor.loadUserAccount(anyInt())).thenReturn(Observable.error(new Throwable()));

        viewModel.findUser(100);

        when(interactor.loadUserAccount(anyInt())).thenReturn(Observable.just(user));

        viewModel.findUser(100);

        verify(interactor, times(2)).loadUserAccount(100);
    }

}