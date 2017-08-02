package co.touchlab.droidconandroid.shared.presenter;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.google.j2objc.annotations.Weak;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import co.touchlab.droidconandroid.shared.interactors.SponsorsInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SponsorsViewModel extends ViewModel
{
    private final SponsorsInteractor task;
    @Weak
    private       SponsorsHost       host;
    private CompositeDisposable disposables = new CompositeDisposable();

    private SponsorsViewModel(@NotNull SponsorsInteractor task)
    {
        this.task = task;
    }

    public void register(@NotNull SponsorsHost host)
    {
        this.host = host;
    }

    public void getSponsors(int type)
    {
        disposables.add(task.getSponsors(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(host:: onSponsorsFound, e -> host.onError()));
    }

    public void unregister()
    {
        host = null;
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
}
