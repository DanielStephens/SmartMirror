package djs.com.mirror.modules.events;

import djs.com.mirror.events.Event;
import djs.com.mirror.model.Temperatures;

/**
 * Created by Daniel on 23/02/2018.
 */

public class TemperatureEvent implements Event {

    private final Temperatures.Temperature currentTemperature;

    private final Temperatures.Temperature lowTemperature;

    private final Temperatures.Temperature highTemperature;

    public TemperatureEvent(Temperatures.Temperature currentTemperature, Temperatures.Temperature lowTemperature, Temperatures.Temperature highTemperature) {
        this.currentTemperature = currentTemperature;
        this.lowTemperature = lowTemperature;
        this.highTemperature = highTemperature;
    }

    public Temperatures.Temperature getCurrentTemperature() {
        return currentTemperature;
    }

    public Temperatures.Temperature getLowTemperature() {
        return lowTemperature;
    }

    public Temperatures.Temperature getHighTemperature() {
        return highTemperature;
    }
}
