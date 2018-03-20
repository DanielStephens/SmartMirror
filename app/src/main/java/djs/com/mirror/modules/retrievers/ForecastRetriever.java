package djs.com.mirror.modules.retrievers;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import djs.com.mirror.basics.Function;
import djs.com.mirror.basics.Optional;
import djs.com.mirror.events.Event;
import djs.com.mirror.events.Notifiable;
import djs.com.mirror.http.HttpService;
import djs.com.mirror.model.Temperatures;
import djs.com.mirror.modules.events.TemperatureEvent;
import djs.com.mirror.modules.events.WeatherEvent;

/**
 * Created by Daniel on 23/02/2018.
 */

public class ForecastRetriever implements Retriever {

    private final String key = "f8cc065ef27b546d587994a23f25eea7";

    private final HttpService httpService;

    private final Function<TemperatureAndWeatherResponse, Event> converter;

    private ForecastRetriever(HttpService httpService, Function<TemperatureAndWeatherResponse, Event> converter) {
        this.httpService = httpService;
        this.converter = converter;
    }

    public static ForecastRetriever temperatureRetriever(HttpService httpService,  final Temperatures.TemperatureUnit temperatureUnit){
        return new ForecastRetriever(httpService, new Function<TemperatureAndWeatherResponse, Event>(){
            @Override
            public Event apply(TemperatureAndWeatherResponse temperatureAndWeatherResponse) {
                return new TemperatureEvent(
                        temperatureAndWeatherResponse.currentTemperature.as(temperatureUnit),
                        temperatureAndWeatherResponse.lowTemperature.as(temperatureUnit),
                        temperatureAndWeatherResponse.highTemperature.as(temperatureUnit)
                );
            }
        });
    }

    public static ForecastRetriever weatherRetriever(HttpService httpService){
        return new ForecastRetriever(httpService, new Function<TemperatureAndWeatherResponse, Event>(){
            @Override
            public Event apply(TemperatureAndWeatherResponse temperatureAndWeatherResponse) {
                return new WeatherEvent(
                        resolveSubtype(temperatureAndWeatherResponse.weatherCode),
                        temperatureAndWeatherResponse.weatherCode
                );
            }
        });
    }

    private static WeatherEvent.WeatherType resolveSubtype(int code) {
        if(code < 300 || range(960, 962).contains(code)){
            return WeatherEvent.WeatherType.THUNDER;
        }else if(set(300, 302, 500).contains(code)) {
            return WeatherEvent.WeatherType.LIGHT_RAIN;
        }else if(range(300, 531).contains(code)) {
            return WeatherEvent.WeatherType.RAIN;
        }else if(code < 700){
            return WeatherEvent.WeatherType.SNOW;
        }else if(code < 800){
            return WeatherEvent.WeatherType.MIST;
        }else if(code < 801 || range(951, 956).contains(code)){
            return WeatherEvent.WeatherType.CLEAR;
        }else if(code < 802){
            return WeatherEvent.WeatherType.CLEAR_INTERVALS;
        }else if(code < 803){
            return WeatherEvent.WeatherType.PARTIAL_CLOUD;
        }else if(code < 900){
            return WeatherEvent.WeatherType.CLOUDY;
        }else if(code < 910){
            return WeatherEvent.WeatherType.EXTREME;
        }else if(code < 900) {
            return WeatherEvent.WeatherType.CLOUDY;
        }else if(range(957, 959).contains(code)){
            return WeatherEvent.WeatherType.WINDY;
        }else{
            return WeatherEvent.WeatherType.UNKNOWN;
        }
    }

    private static <T> Set<T> set(T... ts){
        Set<T> set = new HashSet<>();
        for(T t : ts){
            set.add(t);
        }
        return set;
    }

    private static Set<Integer> range(int a, int b){
        Set<Integer> set = new HashSet<>();
        for(int i = a; i < b+1; i++){
            set.add(i);
        }
        return set;
    }

    @Override
    public boolean retrieve(final Notifiable notifiable) {
        Optional<Event> event = get().map(converter);

        if(event.isPresent()){
            notifiable.notify(event.get());
        }

        return event.isPresent();
    }

    private Optional<TemperatureAndWeatherResponse> get(){
        try {
            return Optional.of(throwableParse());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private TemperatureAndWeatherResponse throwableParse() throws Exception {
        TemperatureAndWeatherResponse response = new TemperatureAndWeatherResponse();

        JSONObject json = httpService.asJson(String.format("http://api.openweathermap.org/data/2.5/weather?id=3333134&appid=%s", key));

        JSONObject temperatureObject = json.getJSONObject("main");
        response.setWeatherCode(json.getJSONArray("weather").getJSONObject(0).getInt("id"));
        response.setCurrentTemperature(Temperatures.kelvin(temperatureObject.getDouble("temp")));
        response.setLowTemperature(Temperatures.kelvin(temperatureObject.getDouble("temp_min")));
        response.setHighTemperature(Temperatures.kelvin(temperatureObject.getDouble("temp_max")));

        return response;
    }

    public static class TemperatureAndWeatherResponse {

        private int weatherCode;

        private Temperatures.Temperature currentTemperature;

        private Temperatures.Temperature lowTemperature;

        private Temperatures.Temperature highTemperature;

        public void setWeatherCode(int weatherCode) {
            this.weatherCode = weatherCode;
        }

        public void setCurrentTemperature(Temperatures.Temperature currentTemperature) {
            this.currentTemperature = currentTemperature;
        }

        public void setLowTemperature(Temperatures.Temperature lowTemperature) {
            this.lowTemperature = lowTemperature;
        }

        public void setHighTemperature(Temperatures.Temperature highTemperature) {
            this.highTemperature = highTemperature;
        }

    }

}
