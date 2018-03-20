package djs.com.mirror.ui.modules;

import android.app.Activity;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import djs.com.mirror.R;
import djs.com.mirror.basics.Consumer;
import djs.com.mirror.events.Notifier;
import djs.com.mirror.modules.events.NewDayEvent;

/**
 * Created by Daniel on 24/02/2018.
 */

public class DateActivity implements BasicActivity {

    private final Activity activity;

    private final Notifier notifier;

    private TextView dateValue;
    private TextView endingValue;
    private TextView monthValue;

    public DateActivity(Activity activity, Notifier notifier) {
        this.activity = activity;
        this.notifier = notifier;
        subscribe();
    }

    private void subscribe() {
        dateValue = (TextView) activity.findViewById(R.id.Date);
        endingValue = (TextView) activity.findViewById(R.id.Ending);
        monthValue = (TextView) activity.findViewById(R.id.Month);

        notifier.subscribe(NewDayEvent.class, new Consumer<NewDayEvent>() {
            @Override
            public void accept(NewDayEvent value) {

                Calendar cal = value.getCalendar();
                int dayNumber = cal.get(Calendar.DAY_OF_MONTH);
                String day = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()); //nameOfDay(cal.get(Calendar.DAY_OF_WEEK));
                String ordinal = getDayNumberSuffix(dayNumber);
                String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

                dateValue.setText(String.format("%s the %s", day, dayNumber));
                endingValue.setText(ordinal);
                monthValue.setText(String.format(" of %s", month));

            }
        });
    }

    private String nameOfDay(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                throw new RuntimeException("what?! missing day of the week");
        }
    }

    private String getCurrentDateInSpecificFormat(Calendar currentCalDate) {
        String dayNumberSuffix = getDayNumberSuffix(currentCalDate.get(Calendar.DAY_OF_MONTH));
        DateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' MMMM yyyy");
        return dateFormat.format(currentCalDate.getTime());
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

}
