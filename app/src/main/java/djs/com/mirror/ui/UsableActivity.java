package djs.com.mirror.ui;

import djs.com.mirror.basics.Consumer;

/**
 * Created by Daniel on 24/02/2018.
 */

public interface UsableActivity {

    <T> void set(int resource, Consumer<T> resourceConsumer);

}
