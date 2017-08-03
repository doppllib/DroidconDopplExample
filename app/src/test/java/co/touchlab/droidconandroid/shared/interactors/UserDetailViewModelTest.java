package co.touchlab.droidconandroid.shared.interactors;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.presenter.UserDetailHost;
import co.touchlab.droidconandroid.shared.presenter.UserDetailViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;


import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailViewModelTest
{
    @Rule
    public final RxTrampolineSchedulerRule schedulerRule = new RxTrampolineSchedulerRule();

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
        factory = new UserDetailViewModel.Factory(interactor);
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
        UserAccount user = new UserAccount();
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