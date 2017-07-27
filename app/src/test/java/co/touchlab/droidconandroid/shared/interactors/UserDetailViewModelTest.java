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
    private UserDetailViewModel viewModel;

    @Before
    public void setUp()
    {
        viewModel = new UserDetailViewModel.Factory(interactor).create(UserDetailViewModel.class);
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
        when(interactor.loadUserAccount()).thenReturn(Observable.just(user));

        viewModel.findUser();

        verify(host).onUserFound(user);
    }

    @Test
    public void whenErrorGettingUserDetail_shouldShowError()
    {
        when(interactor.loadUserAccount()).thenReturn(Observable.error(new Throwable()));

        viewModel.findUser();

        verify(host).findUserError();
    }


}