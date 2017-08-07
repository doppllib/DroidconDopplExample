package co.touchlab.droidconandroid.shared.utils;


import de.greenrobot.event.EventBus;
import de.greenrobot.event.SubscriberExceptionEvent;

public class EventBusExt {
    private static EventBusExt instance = new EventBusExt();
    private EventBus eventBus = new EventBus();

    public EventBusExt() {
        this.eventBus.register(new EventBusExt.ErrorListener());
    }

    public static EventBus getDefault() {
        return instance.eventBus;
    }

    public static class ErrorListener {
        public ErrorListener() {
        }

        public void onEvent(SubscriberExceptionEvent exceptionEvent) {
            final Throwable throwable = exceptionEvent.throwable;
            (new Thread() {
                public void run() {
                    if (throwable instanceof RuntimeException) {
                        throw (RuntimeException) throwable;
                    } else if (throwable instanceof Error) {
                        throw (Error) throwable;
                    } else {
                        throw new RuntimeException(throwable);
                    }
                }
            }).start();
        }
    }
}

