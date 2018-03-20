package djs.com.mirror.modules.events;

import java.util.List;

import djs.com.mirror.events.Event;

/**
 * Created by Daniel on 25/02/2018.
 */

public class NewsEvent implements Event {

    private final List<News> newsItems;

    public NewsEvent(List<News> newsItems) {
        this.newsItems = newsItems;
    }

    public List<News> getNewsItems() {
        return newsItems;
    }

    public static class News {

        private final String heading;

        private final String body;

        public News(String heading, String body) {
            this.heading = heading;
            this.body = body;
        }

        public String getHeading() {
            return heading;
        }

        public String getBody() {
            return body;
        }
    }

}
