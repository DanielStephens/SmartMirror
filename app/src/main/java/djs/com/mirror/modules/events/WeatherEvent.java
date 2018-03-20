package djs.com.mirror.modules.events;

import djs.com.mirror.events.Event;

/**
 * Created by Daniel on 23/02/2018.
 */

public class WeatherEvent implements Event {

    private final WeatherType weatherType;

    private final int weatherCode;

    public WeatherEvent(WeatherType weatherType, int weatherCode) {
        this.weatherType = weatherType;
        this.weatherCode = weatherCode;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public enum WeatherType {

        CLEAR,
        CLEAR_INTERVALS,
        PARTIAL_CLOUD,
        CLOUDY,
        LIGHT_RAIN,
        RAIN,
        THUNDER,
        SNOW,
        MIST,
        WINDY,
        EXTREME,
        UNKNOWN

    }

}
