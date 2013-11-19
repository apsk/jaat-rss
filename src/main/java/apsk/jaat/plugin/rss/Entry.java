package apsk.jaat.plugin.rss;

import java.util.Map;

public class Entry {
    public static int entryCounter = 0;
    public static void main(String[] args) throws Exception {
        RssReader rssReader = RssReader.forURL("http://stackoverflow.com/feeds/tag/java", e -> {
            entryCounter += 1;
            return entryCounter > 2;
        });
        System.out.println(rssReader.getEntry(0).keySet());
        rssReader.getEntry(0).forEach((k, v) ->
            System.out.println(k + " = " + v)
        );
    }
}
