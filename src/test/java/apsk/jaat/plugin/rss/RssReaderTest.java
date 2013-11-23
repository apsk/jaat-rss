package apsk.jaat.plugin.rss;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RssReaderTest {
    @Test
    public void testEntry() throws
            URISyntaxException, IOException, ParserConfigurationException, SAXException
    {
        URI testFileURI = getClass().getClassLoader().getResource("test-feed.xml").toURI();
        assertNotNull("test-feed.xml not found", testFileURI);
        InputStream in = new FileInputStream(new File(testFileURI));
        Map<String, String> entry = RssReader.readFromStream(in, 1, e -> false).get(0);
        assertEquals("id mismatch", entry.get("id"), "http://stackoverflow.com/q/20160707");
        assertEquals("author.name mismatch", entry.get("author.name"), "Espryon");
    }
}
