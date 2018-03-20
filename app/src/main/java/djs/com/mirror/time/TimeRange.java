package djs.com.mirror.time;

/**
 * Created by Daniel on 11/03/2018.
 */

public class TimeRange {

    private final Time lower;

    private final Time upper;

    protected TimeRange(Time lower, Time upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public boolean contains(Time time){
        if(lower.compareTo(time) == 0 || upper.compareTo(time) == 0){
            return true;
        }

        if(lower.compareTo(upper) > 0){
            return !new TimeRange(upper, lower).contains(time);
        }

        return lower.compareTo(time) < 0 && upper.compareTo(time) > 0;
    }

    public TimeRange invert(){
        return new TimeRange(upper.plusSeconds(1), lower.minusSeconds(1));
    }

    public static class EmptyTimeRange extends TimeRange {

        protected EmptyTimeRange(Time lower, Time upper) {
            super(lower, upper);
        }

        @Override
        public boolean contains(Time time) {
            return false;
        }

        @Override
        public TimeRange invert() {
            return new TimeRange(Time.midnight(), Time.midnight().minusSeconds(1));
        }
    }

}
