package djs.com.mirror.ui.modules;

import android.app.Activity;
import android.widget.TextView;

import djs.com.mirror.R;
import djs.com.mirror.basics.Consumer;
import djs.com.mirror.events.Notifier;
import djs.com.mirror.modules.events.WordOfTheDayEvent;

/**
 * Created by Daniel on 24/02/2018.
 */

public class WordOfTheDayActivity implements BasicActivity {

    private final Activity activity;

    private final Notifier notifier;

    private TextView wordOfDay;
    private TextView wordDefinitions;

    public WordOfTheDayActivity(Activity activity, Notifier notifier) {
        this.activity = activity;
        this.notifier = notifier;
        subscribe();
    }

    private void subscribe() {
        wordOfDay = (TextView) activity.findViewById(R.id.WordOfDay);
        wordDefinitions = (TextView) activity.findViewById(R.id.Definition);

        notifier.subscribe(WordOfTheDayEvent.class, new Consumer<WordOfTheDayEvent>() {
            @Override
            public void accept(WordOfTheDayEvent value) {
                wordOfDay.setText(value.getWord());
                wordDefinitions.setText(value.getDefinition());
            }
        });
    }
}
