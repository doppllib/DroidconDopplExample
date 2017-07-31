package co.touchlab.droidconandroid.shared.presenter;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import co.touchlab.droidconandroid.shared.interactors.SponsorsInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SponsorsViewModel extends ViewModel {
    private final SponsorsInteractor task;
    private SponsorsHost host;
    private CompositeDisposable disposables = new CompositeDisposable();

    private SponsorsViewModel(@NotNull SponsorsInteractor task) {
        this.task = task;
    }

    public void register(@NotNull SponsorsHost host) {
        this.host = host;
    }

    public void getSponsors() {
        disposables.add(task.getSponsors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(host::onSponsorsFound,
                        e -> host.onError()));
    }

    public void unregister() {
        host = null;
        disposables.clear();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final SponsorsInteractor task;

        public Factory(SponsorsInteractor task) {
            this.task = task;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new SponsorsViewModel(task);
        }
    }
}
