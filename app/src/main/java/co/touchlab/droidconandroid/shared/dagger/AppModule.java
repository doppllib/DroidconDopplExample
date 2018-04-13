package co.touchlab.droidconandroid.shared.dagger;


import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import co.touchlab.droidconandroid.shared.data.DroidconDatabase;
import co.touchlab.droidconandroid.shared.network.sessionize.SessionizeDatabase;
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
    @Singleton
    DroidconDatabase providesDroidconDatabase(Application application)
    {
        return Room.databaseBuilder(application, DroidconDatabase.class, "droidcon").build();
    }

    @Provides
    @Singleton
    SessionizeDatabase provideSessionizeDatabase(Application application)
    {
        return Room.databaseBuilder(application, SessionizeDatabase.class, "droidcon-sessionize-database").build();
    }
}
