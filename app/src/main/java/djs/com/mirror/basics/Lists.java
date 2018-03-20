package djs.com.mirror.basics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 24/02/2018.
 */

public class Lists {

    private Lists(){}

    public static <T> List<T> newArrayList(T... ts){
        List<T> list = new ArrayList<>();
        for(T t : ts){
            list.add(t);
        }
        return  list;
    }

}
