package djs.com.mirror.model;

/**
 * Created by Daniel on 24/02/2018.
 */

public class Temperatures {

    private Temperatures(){}

    public static Temperature celsius(double value){
        return new Temperature(value, TemperatureUnit.CELSIUS);
    }

    public static Temperature farenheit(double value){
        return new Temperature(value, TemperatureUnit.FARENHEIT);
    }

    public static Temperature kelvin(double value){
        return new Temperature(value, TemperatureUnit.KELVIN);
    }

    public static Temperature of(double value, TemperatureUnit unit){
        return new Temperature(value, unit);
    }

    public static final class Temperature {

        private final double value;

        private final TemperatureUnit temperatureUnit;

        public Temperature(double value, TemperatureUnit temperatureUnit) {
            this.value = value;
            this.temperatureUnit = temperatureUnit;
        }

        public Temperature as(TemperatureUnit newTemperatureUnit){
            return new Temperature(newTemperatureUnit.fromKelvin(temperatureUnit.toKelvin(value)), newTemperatureUnit);
        }

        public double getValue() {
            return value;
        }

        public TemperatureUnit getTemperatureUnit() {
            return temperatureUnit;
        }

        @Override
        public String toString() {
            return Math.round(value) + temperatureUnit.toString();
        }
    }

    public enum TemperatureUnit {

        CELSIUS ("°C"){
            @Override
            protected double toKelvin(double celsius) {
                return celsius + 273.15d;
            }

            @Override
            protected double fromKelvin(double kelvin) {
                return kelvin - 273.15d;
            }
        },
        FARENHEIT ("°F"){
            @Override
            protected double toKelvin(double farenheit) {
                return (farenheit + 459.67d)* 5d/9d;
            }

            @Override
            protected double fromKelvin(double kelvin) {
                return (kelvin * 9d/5d) - 459.67d;
            }
        },
        KELVIN ("°K"){
            @Override
            protected double toKelvin(double kelvin) {
                return kelvin;
            }

            @Override
            protected double fromKelvin(double kelvin) {
                return kelvin;
            }
        };

        private String unitString;

        TemperatureUnit(String unitString) {
            this.unitString = unitString;
        }

        protected abstract double toKelvin(double value);

        protected abstract double fromKelvin(double kelvin);


        @Override
        public String toString() {
            return unitString;
        }
    }

}
