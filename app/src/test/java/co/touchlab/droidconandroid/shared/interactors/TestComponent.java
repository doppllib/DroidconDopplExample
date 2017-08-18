package co.touchlab.droidconandroid.shared.interactors;

import dagger.Component;
import io.reactivex.ObservableTransformer;

@Component(modules = TestSchedulerModule.class)
public interface TestComponent
{
    ObservableTransformer getTransformer();
}
