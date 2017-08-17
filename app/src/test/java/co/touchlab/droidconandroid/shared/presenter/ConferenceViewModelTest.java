package co.touchlab.droidconandroid.shared.presenter;

import android.util.Pair;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import co.touchlab.doppl.testing.DopplContextDelegateTestRunner;
import co.touchlab.droidconandroid.shared.data.AppPrefs;
import co.touchlab.droidconandroid.shared.presenter.ConferenceDataViewModel;
import io.reactivex.Observable;


import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(DopplContextDelegateTestRunner.class)
public class ConferenceViewModelTest
{
    @Mock
    AppPrefs appPrefs;

    @Mock
    ConferenceDataViewModel.Host host;

    ConferenceDataViewModel conferenceDataViewModel;
    ConferenceDataViewModel.Factory factory;

    @Before
    public void setUpMockito()
    {
        MockitoAnnotations.initMocks(this);
        factory = new ConferenceDataViewModel.Factory(true);
        factory.appPrefs = appPrefs;
        conferenceDataViewModel = factory.create(ConferenceDataViewModel.class);
    }

    @Test
    public void whenDuplicateDates_ShouldUpdateHostOnce()
    {
        Pair<String, String> pair1 = new Pair<>("09/29/2016 08:00AM", "09/30/2016 06:00PM");
        Pair<String, String> pair2 = new Pair<>("09/29/2016 08:00AM", "09/30/2016 06:00PM");
        Pair<String, String> pair3 = new Pair<>("09/29/2016 08:00AM", "09/30/2016 06:00PM");

        Observable<Pair<String, String>> obs = Observable.just(pair1, pair2, pair3).distinctUntilChanged();
        when(appPrefs.observeConventionDates()).thenReturn(obs);

        conferenceDataViewModel.register(host);

        verify(host, times(1)).updateConferenceDates(anyList());
    }

    @Test
    public void whenDistinctDates_ShouldUpdateHost()
    {
        Pair<String, String> pair1 = new Pair<>("09/29/2016 08:00AM", "09/30/2016 06:00PM");
        Pair<String, String> pair2 = new Pair<>("09/30/2016 08:00AM", "09/30/2016 06:00PM");
        Pair<String, String> pair3 = new Pair<>("09/28/2016 08:00AM", "09/30/2016 06:00PM");

        Observable<Pair<String, String>> obs = Observable.just(pair1, pair2, pair3).distinctUntilChanged();
        when(appPrefs.observeConventionDates()).thenReturn(obs);

        conferenceDataViewModel.register(host);

        verify(host, times(3)).updateConferenceDates(anyList());
    }

    @Test
    public void whenDateReverts_ShouldUpdateHost()
    {
        Pair<String, String> pair1 = new Pair<>("09/29/2016 08:00AM", "09/30/2016 06:00PM");
        Pair<String, String> pair2 = new Pair<>("09/30/2016 08:00AM", "09/30/2016 06:00PM");
        Pair<String, String> pair3 = new Pair<>("09/29/2016 08:00AM", "09/30/2016 06:00PM");

        Observable<Pair<String, String>> obs = Observable.just(pair1, pair2, pair3).distinctUntilChanged();
        when(appPrefs.observeConventionDates()).thenReturn(obs);

        conferenceDataViewModel.register(host);

        verify(host, times(3)).updateConferenceDates(anyList());
    }
}
