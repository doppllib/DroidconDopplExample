package co.touchlab.droidconandroid.shared.interactors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import static org.mockito.Mockito.when;

public class FindUserInteractorTest
{
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
        MockitoAnnotations.initMocks(this);
        interactor = new FindUserInteractor(helper, request);
        response = new UserInfoResponse();
        response.user = new NetworkUserAccount();
        user = new UserAccount();
    }
/*
    @Test
    public void whenNetworkReturns_ShouldNotGoToDatabase()
    {
        when(request.getUserInfo(anyInt())).thenReturn(Observable.just(response));
        when(helper.saveUserAccount(anyObject())).thenReturn(Completable.complete());
        when(helper.getUserAccountForId(anyInt())).thenReturn(Single.error(new Throwable()));

        interactor.loadUserAccount(100).test().assertNoErrors();
    }

    @Test
    public void whenNetworkReturnsError_ShouldGetUserFromDatabase()
    {
        Throwable error = new Throwable("Error");
        when(request.getUserInfo(anyInt())).thenReturn(Observable.error(error));
        when(helper.getUserAccountForId(anyLong())).thenReturn(Single.just(user));

        interactor.loadUserAccount(100).test().assertValue(user);
    }

    @Test
    public void whenNetworkAndDatabaseError_ShouldError()
    {
        Throwable error = new Throwable("Error");
        when(request.getUserInfo(anyInt())).thenReturn(Observable.error(error));
        when(helper.getUserAccountForId(anyLong())).thenReturn(Single.error(error));

        interactor.loadUserAccount(100).test().assertError(error);
    }*/
}