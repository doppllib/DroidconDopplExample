package co.touchlab.droidconandroid.presenter;
import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by kgalligan on 4/25/16.
 */
public abstract class AbstractEventBusPresenter
{
    private final Context context;

    public AbstractEventBusPresenter(Context context)
    {
        this.context = context;
        EventBusExt.getDefault().register(this);
    }

    public void unregister()
    {
        EventBusExt.getDefault().unregister(this);
    }

    public Context getContext()
    {
        return context;
    }
}
