package djs.com.mirror.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import djs.com.mirror.R;
import djs.com.mirror.basics.Supplier;
import djs.com.mirror.events.EventManager;
import djs.com.mirror.events.Notifiable;
import djs.com.mirror.modules.Retrievers;
import djs.com.mirror.schedule.ScheduleTask;
import djs.com.mirror.setup.ModuleSetup;
import djs.com.mirror.time.Time;
import djs.com.mirror.time.TimeRange;
import djs.com.mirror.ui.modules.DateActivity;
import djs.com.mirror.ui.modules.NewsActivity;
import djs.com.mirror.ui.modules.OverlayActivity;
import djs.com.mirror.ui.modules.TemperatureActivity;
import djs.com.mirror.ui.modules.TimeActivity;
import djs.com.mirror.ui.modules.WeatherActivity;
import djs.com.mirror.ui.modules.WordOfTheDayActivity;
import djs.com.mirror.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends Activity {

    private boolean first = true;
    private boolean visibleMenus = true;
    private static int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
    private SystemUiHider mSystemUiHider;
    private View contentView;

    private static final Handler handler = new Handler();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(first){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            first = false;
        }
        setContentView(R.layout.activity_fullscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        contentView = findViewById(R.id.fullscreen_content);
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delayedHide(0);
            }
        });


        EventManager eventManager = new EventManager(handler);

        new WeatherActivity(this, eventManager);
        new TemperatureActivity(this, eventManager);
        new TimeActivity(this, eventManager);
        new DateActivity(this, eventManager);
        new WordOfTheDayActivity(this, eventManager);
        new NewsActivity(this, handler, eventManager);
        new OverlayActivity(this, eventManager);

        executorService.submit(scheduleRetrievers(eventManager));

        visibleMenus = true;
        delayedHide(5000);
    }

    private Runnable scheduleRetrievers(final Notifiable notifiable) {
        return new Runnable(){
            @Override
            public void run() {
                ModuleSetup moduleSetup = new ModuleSetup(notifiable);

                moduleSetup.schedule(
                        ScheduleTask.of("WeatherRetriever", Retrievers.weatherRetriever(), "*/10 7-22 * * *"),
                        ScheduleTask.of("TemperatureRetriever", Retrievers.temperatureRetriever(), "0 7-22 * * *"),

                        ScheduleTask.of("WordOfTheDay", Retrievers.wordOfTheDayRetriever(), "1 0 * * *"),
                        ScheduleTask.of("News", Retrievers.newsRetriever(), "0 * * * *"),

                        ScheduleTask.of("NewDay", Retrievers.dateRetriever(), "1 0 * * *"),

                        ScheduleTask.of("Sleep", Retrievers.sleepRetriever(), "0 22 * * *", betweenTimeOfDay(Time.of(22).until(Time.of(5, 59)))),
                        ScheduleTask.of("Wake", Retrievers.wakeRetriever(), "0 6 * * *", betweenTimeOfDay(Time.of(22).until(Time.of(5, 59))))
                );
            }
        };
    }

    private Supplier<Boolean> betweenTimeOfDay(final TimeRange inclusion){
        return new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return inclusion.contains(Time.now());
            }
        };
    }

    public static void print(final String value){
        handler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println(value);
            }
        });
    }

    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();

            visibleMenus = !visibleMenus;
            if(visibleMenus){
                delayedHide(5000);
            }
        }
    };

    private void delayedHide(int delayMillis) {
        handler.removeCallbacks(mHideRunnable);
        handler.postDelayed(mHideRunnable, delayMillis);
    }

}
