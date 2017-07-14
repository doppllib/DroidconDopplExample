package co.touchlab.droidconandroid.shared.presenter;

import android.support.annotation.NonNull;

import javax.annotation.Nonnull;

import co.touchlab.droidconandroid.shared.tasks.SponsorsTask;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SponsorsPresenter {
    private final SponsorsTask task;
    private SponsorsHost host;
    private CompositeDisposable disposables = new CompositeDisposable();

    public SponsorsPresenter(@Nonnull SponsorsTask task) {
        this.task = task;
    }

    public void register(@NonNull SponsorsHost host) {
        this.host = host;
    }

    public void getSponsors(int type) {
        disposables.add(task.getSponsors(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(host::onSponsorsFound,
                        e -> host.onError()));
    }

    public void unregister() {
        host = null;
        disposables.clear();
    }
}
