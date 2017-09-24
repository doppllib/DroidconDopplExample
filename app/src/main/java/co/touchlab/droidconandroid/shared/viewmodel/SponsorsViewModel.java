package co.touchlab.droidconandroid.shared.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import co.touchlab.droidconandroid.CrashReport;
import co.touchlab.droidconandroid.shared.interactors.SponsorsInteractor;
import co.touchlab.droidconandroid.shared.network.dao.SponsorsResult;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SponsorsViewModel extends ViewModel
{
    private final SponsorsInteractor sponsorsInteractor;

    private CompositeDisposable disposables = new CompositeDisposable();

    public static class SponsorSection
    {
        private final int spanCount;
        private final List<SponsorsResult.Sponsor> sponsors = new ArrayList<>();

        public SponsorSection(int spanCount)
        {
            this.spanCount = spanCount;
        }

        public void addSponsor(SponsorsResult.Sponsor sponsor)
        {
            sponsors.add(sponsor);
        }

        public int getSpanCount()
        {
            return spanCount;
        }

        public List<SponsorsResult.Sponsor> getSponsors()
        {
            return sponsors;
        }
    }

    public interface Host
    {
        void onShowSponsors(@NonNull List<SponsorSection> sections);
        void onError();
    }

    private SponsorsViewModel(@NonNull SponsorsInteractor sponsorsInteractor)
    {
        this.sponsorsInteractor = sponsorsInteractor;
    }

    public void wire(@NonNull Host host, int type)
    {
        disposables.clear();

        Observable<SponsorsResult> sponsorsResultObservable = sponsorsInteractor.getSponsors(type).cache();

        disposables.add(sponsorsResultObservable.subscribeOn(Schedulers.io())
                .map(sponsorsResult -> {
                    Map<Integer, SponsorSection> sectionMap = new TreeMap<>();

                    for(SponsorsResult.Sponsor sponsor : sponsorsResult.sponsors)
                    {
                        SponsorSection sponsors = sectionMap.get(sponsor.spanCount);
                        if(sponsors == null)
                        {
                            sponsors = new SponsorSection(sponsor.spanCount);
                            sectionMap.put(sponsor.spanCount, sponsors);
                        }
                        sponsors.addSponsor(sponsor);
                    }

                    ArrayList<SponsorSection> sponsorSections = new ArrayList<>(sectionMap.values());
                    Collections.reverse(sponsorSections);
                    return sponsorSections;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(host:: onShowSponsors, e -> {
                    CrashReport.logException(e);
                    host.onError();
                }));
    }

    public void unwire()
    {
        disposables.clear();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {
        @Inject
        SponsorsInteractor task;

        public Factory()
        {

        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new SponsorsViewModel(task);
        }
    }

    public static Factory factory()
    {
        Factory factory = new Factory();
        AppManager.getInstance().getAppComponent().inject(factory);
        return factory;
    }

    @NonNull
    public static SponsorsViewModel forIos()
    {
        return factory().create(SponsorsViewModel.class);
    }
}
