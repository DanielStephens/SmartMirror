package djs.com.mirror.modules.events;

import djs.com.mirror.events.Event;

/**
 * Created by Daniel on 25/02/2018.
 */

public class WordOfTheDayEvent implements Event {

    private final String word;

    private final String definition;

    public WordOfTheDayEvent(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

}
