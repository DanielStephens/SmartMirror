package djs.com.mirror.basics;

/**
 * Created by Daniel on 23/02/2018.
 */

public interface Consumer<T> {

    void accept(T value);

}
