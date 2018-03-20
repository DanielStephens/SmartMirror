package djs.com.mirror.modules.retrievers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import djs.com.mirror.basics.Optional;
import djs.com.mirror.events.Notifiable;
import djs.com.mirror.http.HttpService;
import djs.com.mirror.modules.events.NewsEvent;
import djs.com.mirror.util.HtmlSanitiser;

/**
 * Created by Daniel on 23/02/2018.
 */

public class NewsRetriever implements Retriever {

    private final HttpService httpService;

    public NewsRetriever(HttpService httpService){
        this.httpService = httpService;
    }

    @Override
    public boolean retrieve(final Notifiable notifiable) {
        try {
            Optional<NewsEvent> event = throwableParse();
            if (event.isPresent()) {
                notifiable.notify(event.get());
            }
            return event.isPresent();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Optional<NewsEvent> throwableParse() throws IOException {
        List<NewsEvent.News> newsItems = new ArrayList<>();

        String[] urlList = new String[]{"http://www.bbc.co.uk/news", "http://www.bbc.co.uk/news/uk", "http://www.bbc.co.uk/news/world", "http://www.bbc.co.uk/news/politics", "http://www.bbc.co.uk/news/technology", "http://www.bbc.co.uk/news/science_and_environment", "http://www.bbc.co.uk/news/health"};
        int[] topicList = new int[]{1, 1, 1, 1, 1, 3, 3};
        String html;
        URL url1;
        InputStream is;
        int ptr;
        StringBuffer buffer;

        for (int i = 0; i < urlList.length; i++) {
            html = httpService.asString(urlList[i]);

            for (int j = 0; j < topicList[i]; j++) {
                NewsEvent.News n = null;
                while (true) {
                    n = null;
                    if (j > 2) {
                        break;
                    }
                    n = getBBCStory(html, j);
                    if (n == null) {
                        j++;
                        continue;
                    }

                    boolean isNewStory = true;
                    for (int k = 0; k < newsItems.size(); k++) {
                        if (similar(n, newsItems.get(k))) {
                            isNewStory = false;
                        }
                    }
                    if (isNewStory) {
                        newsItems.add(n);
                        break;
                    } else {
                        j++;
                        continue;
                    }
                }
            }
        }

        return Optional.of(new NewsEvent(newsItems));
    }

    private NewsEvent.News getBBCStory(String html, int i) {
        if (i < 0 || i > 2) {
            return null;
        }
        NewsEvent.News n = null;
        if (html.isEmpty()) {
            return n;
        }
        String source = html;
        switch (i) {
            case 0:
                n = getFirstStory(source);
                break;
            case 1:
                n = getSecondStory(source);
                break;
            case 2:
                n = getThirdStory(source);
                break;
        }
        return n;
    }

    private NewsEvent.News getFirstStory(String source) {
        try {
            String title = source.split("title-link__title-text\">")[1];
            title = title.split("</span>")[0];

            String body = source.split("__summary\">")[1];
            body = body.split("</p>")[0];

            return new NewsEvent.News(escape(title), escape(body));
        } catch (Exception e) {
            return null;
        }
    }

    private NewsEvent.News getSecondStory(String source) {
        try {
            String title = source.split("__summary\">")[1];
            String[] strs = title.split("title-link__title-text\">");
            title = strs[strs.length - 1].split("</span>")[0];

            String body = source.split("__summary\">")[2];
            body = body.split("</p>")[0];

            return new NewsEvent.News(escape(title), escape(body));
        } catch (Exception e) {
            return null;
        }
    }

    private NewsEvent.News getThirdStory(String source) {
        try {
            String title = source.split("__summary\">")[2];
            String[] strs = title.split("title-link__title-text\">");
            title = strs[strs.length - 1].split("</span>")[0];

            String body = source.split("__summary\">")[3];
            body = body.split("</p>")[0];

            return new NewsEvent.News(escape(title), escape(body));
        } catch (Exception e) {
            return null;
        }
    }

    private boolean similar(NewsEvent.News n1, NewsEvent.News n2) {
        String[] words1 = n1.getHeading().split(" ");
        String[] words2 = n2.getHeading().split(" ");
        int count = 0;
        for (int i = 0; i < words1.length; i++) {
            for (int j = 0; j < words2.length; j++) {
                if (words1[i].contains(words2[j]) || words2[j].contains(words1[i])) {
                    count++;
                }
            }
        }
        if ((float) count / (float) words1.length > 0.8f) {
            return true;
        }
        return false;
    }

    private String escape(String xmlText){
        return HtmlSanitiser.sanitise(xmlText);
    }

}
