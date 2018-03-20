package djs.com.mirror.ui.modules;

import android.app.Activity;
import android.widget.TextView;

import djs.com.mirror.R;
import djs.com.mirror.basics.Consumer;
import djs.com.mirror.events.Notifier;
import djs.com.mirror.modules.events.TemperatureEvent;

/**
 * Created by Daniel on 24/02/2018.
 */

public class TemperatureActivity implements BasicActivity {

    private final Activity activity;

    private final Notifier notifier;

    private TextView tempValue;
    private TextView curTempValue;

    public TemperatureActivity(Activity activity, Notifier notifier) {
        this.activity = activity;
        this.notifier = notifier;
        subscribe();
    }

    private void subscribe() {
        tempValue = (TextView) activity.findViewById(R.id.Temperature);
        curTempValue = (TextView) activity.findViewById(R.id.CurTemperature);

        notifier.subscribe(TemperatureEvent.class, new Consumer<TemperatureEvent>() {
            @Override
            public void accept(TemperatureEvent value) {
                tempValue.setText(String.format("%s | %s", value.getLowTemperature(), value.getHighTemperature()));
                curTempValue.setText(value.getCurrentTemperature().toString());
            }
        });
    }
}
