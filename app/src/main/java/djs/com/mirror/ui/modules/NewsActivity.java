package djs.com.mirror.ui.modules;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import djs.com.mirror.R;
import djs.com.mirror.basics.Consumer;
import djs.com.mirror.events.Notifier;
import djs.com.mirror.modules.events.NewsEvent;

/**
 * Created by Daniel on 24/02/2018.
 */

public class NewsActivity implements BasicActivity {

    private final Activity activity;

    private final Notifier notifier;

    private final Handler handler;

    private final long readingDuration = 6_000;

    private final long swapDuration = 1_000;

    private final List<NewsEvent.News> newsItems = new ArrayList<>();

    private int nextIndex;

    private LinearLayout.LayoutParams nextLayout;

    private NewsEvent.News nextNewsItem;

    public NewsActivity(Activity activity, Handler handler, Notifier notifier) {
        this.handler = handler;
        this.activity = activity;
        this.notifier = notifier;
        subscribe();
    }

    private TextView newsHeading;
    private TextView newsSummary;
    private LinearLayout newsContainer;

    private int width;
    private float textSize;
    private int padding;

    private void subscribe() {
        newsHeading = (TextView)activity.findViewById(R.id.NewsHeading);
        newsSummary = (TextView)activity.findViewById(R.id.NewsSummary);
        newsContainer = (LinearLayout)activity.findViewById(R.id.NewsBracket);
        setColour(0);
        handler.post(fadeIn(time()));

        notifier.subscribe(NewsEvent.class, new Consumer<NewsEvent>() {
            @Override
            public void accept(NewsEvent value) {
                width = newsSummary.getWidth();
                padding = newsSummary.getCompoundPaddingBottom() + newsSummary.getCompoundPaddingTop();

                int bodyHeight = 0;
                int headerHeight = 0;
                for(NewsEvent.News news : value.getNewsItems()){
                    bodyHeight = Math.max(bodyHeight, getTextHeight(newsSummary, news.getBody()));
                    headerHeight = Math.max(headerHeight, getTextHeight(newsHeading, news.getHeading()));
                }
                nextLayout = new LinearLayout.LayoutParams(newsContainer.getWidth(), bodyHeight + headerHeight);
                newsItems.clear();
                newsItems.addAll(value.getNewsItems());
                nextIndex = 0;
                nextNewsItem = getOrDefault(nextIndex);
            }
        });
    }

    private Runnable fadeOut(final long startTime){
        return new Runnable() {
            @Override
            public void run() {

                long halfTime = swapDuration/2;
                int colour = 255 - (int)((255*(time()-startTime))/halfTime);

                if(colour <= 0){
                    setColour(0);
                    swapNews();
                    handler.post(fadeIn(time()));
                }else{
                    setColour(Color.rgb(colour, colour, colour));
                    handler.post(fadeOut(startTime));
                }

            }
        };
    }

    private void swapNews() {
        newsHeading.setText(nextNewsItem.getHeading());
        newsSummary.setText(nextNewsItem.getBody());
        newsContainer.setLayoutParams(nextLayout);
        nextIndex = (nextIndex + 1) % newsItems.size();
        nextNewsItem = getOrDefault(nextIndex);
    }

    private Runnable fadeIn(final long startTime){
        return new Runnable() {
            @Override
            public void run() {

                long halfTime = swapDuration/2;
                int colour = (int)((255*(time()-startTime))/halfTime);

                if(colour >= 255){
                    setColour(Color.rgb(255, 255, 255));
                    handler.postDelayed(fadeOut(time() + readingDuration), readingDuration);
                }else{
                    setColour(Color.rgb(colour, colour, colour));
                    handler.post(fadeIn(startTime));
                }
            }
        };
    }

    private void setColour(int colour) {
        newsHeading.setTextColor(colour);
        newsSummary.setTextColor(colour);
    }

    private long time(){
        return new Date().getTime();
    }

    private NewsEvent.News getOrDefault(int i){
        if(i >= 0 && i < newsItems.size()){
            return newsItems.get(i);
        }
        return new NewsEvent.News("No news to display", "");
    }

    private int getTextHeight(TextView textView, CharSequence source) {
        TextPaint paintCopy = new TextPaint(textView.getPaint());
        // Measure using a static layout
        StaticLayout layout = new StaticLayout(source, paintCopy, textView.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true);
        return layout.getHeight();
    }

}
