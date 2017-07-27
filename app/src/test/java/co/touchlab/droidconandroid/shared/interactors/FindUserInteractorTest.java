package co.touchlab.droidconandroid.shared.interactors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.touchlab.droidconandroid.shared.data.DatabaseHelper;
import co.touchlab.droidconandroid.shared.network.FindUserRequest;
import co.touchlab.droidconandroid.shared.network.dao.UserAccount;
import co.touchlab.droidconandroid.shared.network.dao.UserInfoResponse;
import io.reactivex.Observable;


import static org.mockito.Matchers.anyInt;
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

    FindUserInteractor interactor;

    UserInfoResponse response, emptyResponse;

    @Before
    public void setUp() throws Exception
    {
        interactor = new FindUserInteractor(helper, request, 100);
        response = new UserInfoResponse();
        response.user = new UserAccount();

        emptyResponse = new UserInfoResponse();
    }

    @Test
    public void test()
    {
        // TODO: Fill out when new dbhelper gets merged in

        when(request.getUserInfo(anyInt())).thenReturn(Observable.just(emptyResponse));
    }
}