package djs.com.mirror.ui.modules;

import android.app.Activity;
import android.widget.ImageView;

import java.util.Calendar;

import djs.com.mirror.R;
import djs.com.mirror.basics.Consumer;
import djs.com.mirror.events.Notifier;
import djs.com.mirror.modules.events.WeatherEvent;

/**
 * Created by Daniel on 24/02/2018.
 */

public class WeatherActivity implements BasicActivity {

    private final Activity activity;

    private final Notifier notifier;

    private ImageView weatherImg;

    public WeatherActivity(Activity activity, Notifier notifier) {
        this.activity = activity;
        this.notifier = notifier;
        subscribe();
    }

    private void subscribe() {
        weatherImg = (ImageView) activity.findViewById(R.id.WeatherIcon);

        notifier.subscribe(WeatherEvent.class, new Consumer<WeatherEvent>() {
            @Override
            public void accept(WeatherEvent value) {
                int weatherImageId = resolveWeatherResource(value.getWeatherType(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 20);
                weatherImg.setImageResource(weatherImageId);
            }
        });
    }

    private int resolveWeatherResource(WeatherEvent.WeatherType weatherType, boolean night) {
        switch (weatherType) {
            case CLEAR:
                return getResource(augment("clear", night));
            case CLEAR_INTERVALS:
                return getResource(augment("clear_intervals", night));
            case MIST:
                return getResource(augment("misty", night));
            case RAIN:
                return getResource(augment("heavy_rain", night));
            case SNOW:
                return getResource(augment("heavy_snow", night));
            case WINDY:
                return getResource(augment("windy", night));
            case CLOUDY:
                return getResource(augment("heavy_cloud", night));
            case EXTREME:
                return getResource(augment("storm", night));
            case THUNDER:
                return getResource(augment("storm", night));
            case LIGHT_RAIN:
                return getResource(augment("light_rain", night));
            case PARTIAL_CLOUD:
                return getResource(augment("light_cloud", night));
            default:
                return getResource("nullimage");
        }
    }

    private String augment(String phrase, boolean night) {
        return String.format("%s_%s", phrase, night ? "night" : "day");
    }

    private int getResource(String name) {
        return activity.getResources().getIdentifier(name, "drawable", activity.getPackageName());
    }
}
