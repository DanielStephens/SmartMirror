package djs.com.mirror.events;

import android.os.Handler;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import djs.com.mirror.basics.Consumer;
import djs.com.mirror.ui.MainActivity;

/**
 * Created by Daniel on 23/02/2018.
 */

public class EventManager implements Notifier, Notifiable {

    private final Map<UUID, Subscriber<?>> subscribers = new ConcurrentHashMap<>();

    private final Handler handler;

    public EventManager(Handler handler) {
        this.handler = handler;
    }

    @Override
    public <T> void notify(final Event event) {
        MainActivity.print(String.format("Sending an event [%s] to up to %s subscribers", event.getClass().getSimpleName(), subscribers.size()));

        handler.post(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Subscriber<?>> entry : subscribers.entrySet()) {
                    entry.getValue().call(event);
                }
            }
        });

    }

    @Override
    public <T extends Event> Serializable subscribe(Class<T> eventType, Consumer<T> consumer) {
        MainActivity.print("Subscribing!");
        UUID id = UUID.randomUUID();
        subscribers.put(id, new Subscriber<>(eventType, consumer));
        return id;
    }

    @Override
    public boolean unsubscribe(Serializable id) {
        return subscribers.remove(id) != null;
    }

    private static class Subscriber<T> {

        private final Class<T> eventType;

        private final Consumer<T> consumer;

        private Subscriber(Class<T> eventType, Consumer<T> consumer) {
            this.eventType = eventType;
            this.consumer = consumer;
        }

        public boolean call(Event event) {

            if (eventType.isAssignableFrom(event.getClass())) {
                consumer.accept((T) event);
                return true;
            }
            return false;

        }

    }

}
