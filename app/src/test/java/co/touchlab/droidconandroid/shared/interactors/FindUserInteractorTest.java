package co.touchlab.droidconandroid.shared.interactors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.data.UserAccount;
import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.dao.NetworkUserAccount;
import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FindUserInteractorTest
{
    @Rule
    public final RxTrampolineSchedulerRule schedulerRule = new RxTrampolineSchedulerRule();

    @Mock
    DatabaseHelper helper;

    @Mock
    FindUserRequest request;

    private FindUserInteractor interactor;
    private UserInfoResponse   response;
    private UserAccount        user;

    @Before
    public void setUp() throws Exception
    {
        interactor = new FindUserInteractor(helper, request, 100);
        response = new UserInfoResponse();
        response.user = new NetworkUserAccount();
        user = new UserAccount();
    }

    @Test
    public void whenNetworkReturns_ShouldNotGoToDatabase()
    {
        when(request.getUserInfo(anyInt())).thenReturn(Observable.just(response));
        when(helper.saveUserAccount(anyObject())).thenReturn(Completable.complete());
        when(helper.getUserAccountForId(anyInt())).thenReturn(Single.error(new Throwable()));

        interactor.loadUserAccount().test().assertNoErrors();
    }

    @Test
    public void whenNetworkReturnsError_ShouldGetUserFromDatabase()
    {
        Throwable error = new Throwable("Error");
        when(request.getUserInfo(anyInt())).thenReturn(Observable.error(error));
        when(helper.getUserAccountForId(anyLong())).thenReturn(Single.just(user));

        interactor.loadUserAccount().test().assertValue(user);
    }

    @Test
    public void whenNetworkAndDatabaseError_ShouldError()
    {
        Throwable error = new Throwable("Error");
        when(request.getUserInfo(anyInt())).thenReturn(Observable.error(error));
        when(helper.getUserAccountForId(anyLong())).thenReturn(Single.error(error));

        interactor.loadUserAccount().test().assertError(error);
    }

    // Have a test to test the cache and stuff

    @Test
    public void whenNetworkFails_ShouldResetCache()
    {
        when(request.getUserInfo(anyInt())).thenReturn(Observable.error(new Throwable()));
        when(helper.getUserAccountForId(anyLong())).thenReturn(Single.just(user));

        interactor.loadUserAccount().test().assertValue(user);
        interactor.loadUserAccount().test().assertValue(user);
        verify(request, times(2)).getUserInfo(anyInt());
    }

    @Test
    public void whenNetworkSucceeds_ShouldCache()
    {
        when(request.getUserInfo(anyInt())).thenReturn(Observable.just(response));
        when(helper.getUserAccountForId(anyLong())).thenReturn(Single.error(new Throwable()));

        interactor.loadUserAccount().test().assertComplete();
        interactor.loadUserAccount().test().assertComplete();
        verify(request, times(1)).getUserInfo(anyInt());
    }

    @Test
    public void whenNetworkFailsAndDatabaseSucceeds_ShouldResetCache()
    {
        Throwable exception = new Throwable();
        when(request.getUserInfo(anyInt())).thenReturn(Observable.error(exception));
        when(helper.getUserAccountForId(anyLong())).thenReturn(Single.just(user));

        interactor.loadUserAccount().test().assertValue(user);

        when(request.getUserInfo(anyInt())).thenReturn(Observable.just(response));
        when(helper.getUserAccountForId(anyLong())).thenReturn(Single.error(exception));

        interactor.loadUserAccount().test().assertComplete();

        verify(request, times(2)).getUserInfo(anyInt());
    }
}