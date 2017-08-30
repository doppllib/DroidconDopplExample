package co.touchlab.droidconandroid.shared.dagger;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import co.touchlab.droidconandroid.shared.data.DroidconDatabase;
import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule
{
    @Provides
    @Singleton
    static DroidconDatabase providesDatabase(Context context)
    {
        return Room.databaseBuilder(context, DroidconDatabase.class, "droidcon").build();
    }
}
