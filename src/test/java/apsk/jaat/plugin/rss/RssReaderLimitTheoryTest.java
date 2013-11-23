package apsk.jaat.plugin.rss;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(Theories.class)
public class RssReaderLimitTheoryTest {
    @DataPoints
    public static int[] limits = new int[] { 1, 2, 3, 4 };

    @Theory
    public void readEntriesCountLeqLimit(int limit) throws Exception {
        List<Map<String, String>> entries = RssReader
            .readFromGlobalResourceFile("test-feed.xml", limit, e -> false);
        assertTrue("read entries count greater than reader's limit", entries.size() <= limit);
    }
}
