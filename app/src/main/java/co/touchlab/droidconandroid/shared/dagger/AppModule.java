package co.touchlab.droidconandroid.shared.dagger;


import android.content.Context;

import co.touchlab.droidconandroid.DroidconApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule
{
    private final DroidconApplication application;

    public AppModule(DroidconApplication application)
    {
        this.application = application;
    }

    @Provides
    DroidconApplication providesApplication()
    {
        return application;
    }

    @Provides
    Context providesApplicationContext()
    {
        return application.getApplicationContext();
    }
}
