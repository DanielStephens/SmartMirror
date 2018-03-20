package djs.com.mirror.util;

import java.util.Map;

import djs.com.mirror.basics.Maps;

/**
 * Created by Daniel on 03/03/2018.
 */

public class HtmlSanitiser {

    private static final Map<String, String> tagReplacements = Maps.<String, String>hashMapBuilder()
            .put("<b>", "")
            .put("</b>", "")
            .put("<strong>", "")
            .put("</strong>", "")
            .put("<i>", "")
            .put("</i>", "")
            .put("<em>", "(")
            .put("</em>", ")")
            .put("<mark>", "")
            .put("</mark>", "")
            .put("<small>", "")
            .put("</small>", "")
            .put("<del>.*</del>", "")
            .put("<ins>", "")
            .put("</ins>", "")
            .put("<sub>", "")
            .put("</sub>", "")
            .put("<sup>", "")
            .put("</sup>", "")
            .build();

    private static final Map<String, String> escapeReplacements = Maps.<String, String>hashMapBuilder()
            .put("&quot;", "\"")
            .put("&apos;", "'")
            .put("&lt;", "<")
            .put("&gt;", ">")
            .put("&amp;", "&")
            .build();

    private HtmlSanitiser(){
    }

    public static String sanitise(String value){
        return sanitiseEscapedCharacters(sanitiseTags(value));
    }

    private static String sanitiseTags(String value){
        String temp = value;
        for(Map.Entry<String, String> entry : tagReplacements.entrySet()){
            temp = temp.replace(entry.getKey(), entry.getValue());
        }
        return temp;
    }

    private static String sanitiseEscapedCharacters(String value){
        String temp = value;
        for(Map.Entry<String, String> entry : escapeReplacements.entrySet()){
            temp = temp.replace(entry.getKey(), entry.getValue());
        }
        return temp;
    }

}
