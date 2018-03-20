package djs.com.mirror.basics;

import java.util.HashMap;

/**
 * Created by Daniel on 25/02/2018.
 */

public class Maps {

    private Maps(){}

    public static <A, B> Builder<A, B> hashMapBuilder(){
        return new Builder<>();
    }

    public static class Builder<A, B> {

        private final HashMap<A, B> map = new HashMap<>();

        private Builder(){}

        public Builder<A, B> put(A a, B b){
            map.put(a, b);
            return this;
        }

        public HashMap<A, B> build(){
            return map;
        }

    }

}
