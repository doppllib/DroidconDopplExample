package co.touchlab.droidconandroid.shared.dagger;


import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule
{
    private final Application application;

    public AppModule(Application application)
    {
        this.application = application;
    }

    @Provides
    Application providesApplication()
    {
        return application;
    }

    @Provides
    Context providesApplicationContext()
    {
        return application.getApplicationContext();
    }
}
