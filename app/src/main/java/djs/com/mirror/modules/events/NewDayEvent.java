package djs.com.mirror.modules.events;

import java.util.Calendar;

import djs.com.mirror.events.Event;

/**
 * Created by Daniel on 25/02/2018.
 */

public class NewDayEvent implements Event {

    private final Calendar calendar;

    public NewDayEvent(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
