package djs.com.mirror.modules.retrievers;

import java.util.Calendar;

import djs.com.mirror.events.Notifiable;
import djs.com.mirror.modules.events.NewDayEvent;

/**
 * Created by Daniel on 23/02/2018.
 */

public class DateRetriever implements Retriever {

    @Override
    public boolean retrieve(final Notifiable notifiable) {
        notifiable.notify(new NewDayEvent(Calendar.getInstance()));
        return true;
    }

}
