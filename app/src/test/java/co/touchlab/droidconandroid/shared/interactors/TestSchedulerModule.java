package co.touchlab.droidconandroid.shared.interactors;

import dagger.Module;
import dagger.Provides;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

@Module
class TestSchedulerModule
{
    @Provides
    ObservableTransformer providesTransformer()
    {
        return upstream -> upstream.subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline());
    }
}
