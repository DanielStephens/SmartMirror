package djs.com.mirror.modules.retrievers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import djs.com.mirror.basics.Optional;
import djs.com.mirror.events.Notifiable;
import djs.com.mirror.http.HttpService;
import djs.com.mirror.modules.events.WordOfTheDayEvent;
import djs.com.mirror.ui.MainActivity;
import djs.com.mirror.util.HtmlSanitiser;

/**
 * Created by Daniel on 25/02/2018.
 */

public class WordOfTheDayRetriever implements Retriever {

    private final Pattern definitionPattern = Pattern.compile("<li .*?><span>(.*?)</span></li>");

    private final HttpService httpService;

    public WordOfTheDayRetriever(HttpService httpService) {
        this.httpService = httpService;
    }

    @Override
    public boolean retrieve(Notifiable notifiable) {
        try {
            Optional<WordOfTheDayEvent> event = throwableParse();
            if(event.isPresent()){
                notifiable.notify(event.get());
            }

            return event.isPresent();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Optional<WordOfTheDayEvent> throwableParse() throws IOException {
        String html = httpService.asString("http://dictionary.reference.com/wordoftheday/");
        if(html == null || html.isEmpty()){
            return Optional.empty();
        }

        String restriction = html.split("Definitions for <strong>")[1].split("</ol>")[0];
        String word = capitalise(restriction.split("<")[0]);
        String definitionGroup = restriction.split("<ol .*?>")[1].trim();

        List<String> definitions = new ArrayList<>();

        Matcher matcher = definitionPattern.matcher(definitionGroup);
        while(matcher.find()){
            MainActivity.print("XXXX" + matcher.group(1));
            definitions.add(capitalise(escape(matcher.group(1))));
        }

        StringBuilder sb = new StringBuilder();
        int i = 0;

        for(String definition : definitions){
            sb.append("\u2022 ").append(definition);
            if(i != definitions.size()-1){
                sb.append("\n");
            }
            i++;
        }

        String definitionText = sb.toString();
        return Optional.of(new WordOfTheDayEvent(word, definitionText));
    }

    private String escape(String text){
        return HtmlSanitiser.sanitise(text);
    }

    private String capitalise(String word) {
        if(word.isEmpty()){
            return word;
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
