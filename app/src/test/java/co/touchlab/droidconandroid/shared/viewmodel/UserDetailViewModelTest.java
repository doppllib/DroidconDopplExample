package co.touchlab.droidconandroid.shared.viewmodel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.interactors.FindUserInteractor;
import dagger.Provides;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
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
    UserDetailViewModel.Host     host;
    private UserDetailViewModel.Factory factory;
    private UserDetailViewModel         viewModel;
    private UserAccount user = new UserAccount();

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        FlowableTransformer<UserAccount, UserAccount> transformer = upstream -> upstream.subscribeOn(Schedulers.trampoline()).observeOn(Schedulers.trampoline());

        factory = new UserDetailViewModel.Factory();
        factory.task = interactor;
        factory.transformer = transformer;
        viewModel = factory.create(UserDetailViewModel.class);
    }



    @Provides
    FlowableTransformer providesFlowableTransformer()
    {
        return upstream -> upstream.subscribeOn(Schedulers.trampoline()).observeOn(Schedulers.trampoline());
    }




    @After
    public void tearDown()
    {
        viewModel.unwire();
    }

    @Test
    public void whenSuccessGettingUserDetail_shouldShowResults()
    {
        when(interactor.loadUserAccount(anyInt())).thenReturn(Flowable.just(user));

        viewModel.wire(host, 100);

        verify(host).onUserFound(user);
    }

    @Test
    public void whenErrorGettingUserDetail_shouldShowError()
    {
        when(interactor.loadUserAccount(anyInt())).thenReturn(Flowable.error(new Throwable()));

        viewModel.wire(host, 100);

        verify(host).findUserError();
    }

    @Test
    public void whenErrorLoadingUser_ShouldNotCacheResult()
    {
        when(interactor.loadUserAccount(anyInt())).thenReturn(Flowable.error(new Throwable()));

        viewModel.wire(host, 100);

        when(interactor.loadUserAccount(anyInt())).thenReturn(Flowable.just(user));

        viewModel.wire(host, 100);

        verify(interactor, times(2)).loadUserAccount(100);
    }

}