package djs.com.mirror.modules.retrievers;

import djs.com.mirror.events.Notifiable;

/**
 * Created by Daniel on 23/02/2018.
 */

public interface Retriever {

    boolean retrieve(Notifiable notifiable);

}
