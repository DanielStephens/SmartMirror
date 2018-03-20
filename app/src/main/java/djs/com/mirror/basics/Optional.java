package djs.com.mirror.basics;

/**
 * Created by Daniel on 23/02/2018.
 */

public class Optional<T> {

    private static final None<Object> NONE = new None<>();

    private final T value;

    private final boolean hasValue;

    private Optional(T value, boolean hasValue) {
        this.value = value;
        this.hasValue = hasValue;
    }

    public static <T> Optional<T> of(T value){
        return new Some<T>(value);
    }

    public static <T> Optional<T> ofNullable(T value){
        if(value == null){
            return (Optional<T>) NONE;
        }else{
            return new Some<T>(value);
        }
    }

    public static <T> Optional<T> empty(){
        return (Optional<T>) NONE;
    }

    public boolean isPresent(){
        return hasValue;
    }

    public <R> Optional<R> map(Function<T, R> func){
        if(isPresent()){
            return of(func.apply(value));
        }else{
            return empty();
        }
    }

    public void ifPresent(Consumer<T> consumer){
        if(isPresent()){
            consumer.accept(value);
        }
    }

    public T get() {
        if(isPresent()){
            return value;
        }else{
            throw new RuntimeException("Nothing to get!");
        }
    }

    private static final class Some<T> extends Optional<T> {

        private Some(T value) {
            super(requireNonNull(value), true);
        }

    }

    private static final class None<T> extends Optional<T> {

        private None() {
            super(null, false);
        }

    }

    private static <T> T requireNonNull(T t){
        if(t == null){
            throw new RuntimeException("required not null");
        }else{
            return t;
        }
    }

}
