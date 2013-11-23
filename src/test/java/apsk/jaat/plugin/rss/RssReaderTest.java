package apsk.jaat.plugin.rss;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RssReaderTest {
    @Test
    public void testEntry() throws Exception {
        Map<String, String> entry = RssReader.readFromGlobalResourceFile("test-feed.xml", 1, e -> false).get(0);
        assertEquals("id mismatch", entry.get("id"), "http://stackoverflow.com/q/20160707");
        assertEquals("author.name mismatch", entry.get("author.name"), "Espryon");
    }
}
