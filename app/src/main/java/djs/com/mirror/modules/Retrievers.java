package djs.com.mirror.modules;

import djs.com.mirror.events.Notifiable;
import djs.com.mirror.http.HttpService;
import djs.com.mirror.model.Temperatures;
import djs.com.mirror.modules.events.SleepEvent;
import djs.com.mirror.modules.events.WakeEvent;
import djs.com.mirror.modules.retrievers.DateRetriever;
import djs.com.mirror.modules.retrievers.ForecastRetriever;
import djs.com.mirror.modules.retrievers.NewsRetriever;
import djs.com.mirror.modules.retrievers.Retriever;
import djs.com.mirror.modules.retrievers.WordOfTheDayRetriever;

/**
 * Created by Daniel on 24/02/2018.
 */

public class Retrievers {

    private static final HttpService HTTP_SERVICE = new HttpService();

    private Retrievers(){}

    public static String retriever(String name, String retriever, String cronPattern){
        return String.format("%s|%s|%s", name, retriever, cronPattern);
    }

    public static Retriever temperatureRetriever(){
        return ForecastRetriever.temperatureRetriever(HTTP_SERVICE, Temperatures.TemperatureUnit.CELSIUS);
    }

    public static Retriever weatherRetriever(){
        return ForecastRetriever.weatherRetriever(HTTP_SERVICE);
    }

    public static Retriever dateRetriever(){
        return new DateRetriever();
    }

    public static Retriever wordOfTheDayRetriever(){
        return new WordOfTheDayRetriever(HTTP_SERVICE);
    }

    public static Retriever newsRetriever(){
        return new NewsRetriever(HTTP_SERVICE);
    }

    public static Retriever sleepRetriever(){
        return new Retriever(){
            @Override
            public boolean retrieve(Notifiable notifiable) {
                notifiable.notify(new SleepEvent());
                return true;
            }
        };
    }

    public static Retriever wakeRetriever(){
        return new Retriever(){
            @Override
            public boolean retrieve(Notifiable notifiable) {
                notifiable.notify(new WakeEvent());
                return true;
            }
        };
    }

}
