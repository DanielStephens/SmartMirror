package djs.com.mirror.events;

/**
 * Created by Daniel on 23/02/2018.
 */

public interface Notifiable {

    <T> void notify(Event event);

}
