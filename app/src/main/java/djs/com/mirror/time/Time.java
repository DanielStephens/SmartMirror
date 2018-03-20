package djs.com.mirror.time;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Created by Daniel on 11/03/2018.
 */

public class Time implements Comparable<Time> {

    private static final Time MIDNIGHT = new Time(0, 0, 0);

    private final int hour;

    private final int minute;

    private final int second;

    private Time(int hour, int minute, int seconds) {
        this.hour = hour;
        this.minute = minute;
        this.second = seconds;
    }

    public static Time midnight(){
        return MIDNIGHT;
    }

    public static Time now(){
        Calendar cal = Calendar.getInstance();
        return new Time(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }

    public static Time of(int hour){
        return new Time(hour, 0, 0);
    }

    public static Time of(int hour, int minute){
        return new Time(hour, minute, 0);
    }

    public static Time of(int hour, int minute, int second){
        return new Time(hour, minute, second);
    }

    private static Time composite(int secondOfDay){
        int second = secondOfDay % 60;
        int minute = (secondOfDay/60) % 60;
        int hour = (secondOfDay/3600) % 24;
        return new Time(hour, 0, 0);
    }

    public TimeRange until(Time time){
        return new TimeRange(this, time);
    }

    public Time minusSeconds(int seconds){
        return Time.composite(this.asSecondOfDay()-seconds);
    }

    public Time plusSeconds(int seconds){
        return Time.composite(this.asSecondOfDay()+seconds);
    }

    private int asSecondOfDay(){
        return second + (minute*60) + (hour*3600);
    }

    @Override
    public int compareTo(@NonNull Time o) {
        int cur = comp(this.hour, o.hour);
        if(cur != 0){
            return cur;
        }

        cur = comp(this.minute, o.minute);
        if(cur != 0){
            return cur;
        }

        return comp(this.second, o.second);
    }

    private int comp(int x, int y){
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}
