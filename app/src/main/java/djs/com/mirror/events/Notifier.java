package djs.com.mirror.events;

import java.io.Serializable;

import djs.com.mirror.basics.Consumer;

/**
 * Created by Daniel on 23/02/2018.
 */

public interface Notifier {

    <T extends Event> Serializable subscribe(Class<T> eventType, Consumer<T> consumer);

    boolean unsubscribe(Serializable id);

}
