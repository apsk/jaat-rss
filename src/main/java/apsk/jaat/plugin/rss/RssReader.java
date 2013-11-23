package apsk.jaat.plugin.rss;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RssReader extends DefaultHandler {
    public static class Termination extends SAXException {}

    Deque<String> path = new LinkedList<>();
    boolean isReadingEntry = false;
    boolean isReadingChars = false;
    List<Map<String, String>> entries = new ArrayList<>();
    Map<String, String> curEntry;
    StringBuilder curChars;
    int entriesReadLimit;
    Predicate<Map<String,String>> earlyTerminationCondition;

    private RssReader(
        int entriesReadLimit,
        Predicate<Map<String,String>> earlyTerminationCondition
    ) {
        this.entriesReadLimit = entriesReadLimit;
        this.earlyTerminationCondition = earlyTerminationCondition;
    }

    @Override
    public void startElement(
        String namespaceURI,
        String localName,
        String qName,
        Attributes attrs
    ) {
        if (isReadingEntry) {
            path.addLast(localName);
            if (isReadingChars) {
                curChars.setLength(0);
            }
        } else if (localName.equals("entry") || localName.equals("item")) {
            isReadingEntry = true;
            curEntry = new HashMap<>();
        }
    }

    @Override
    public void characters(char[] chars, int start, int length) {
        if (isReadingEntry) {
            String str = new String(chars, start, length);
            if (isReadingChars) {
                curChars.append(str);
            } else if (path.size() > 0) {
                curChars = new StringBuilder(str);
                isReadingChars = true;
            }
        }
    }

    @Override
    public void endElement(
        String uri,
        String localName,
        String qName
    ) throws Termination {
        if (isReadingChars) {
            String content = curChars.toString().trim();
            if (content.length() > 0) {
                String field = path.stream().collect(Collectors.joining("."));
                curEntry.put(field, content);
            }
            isReadingChars = false;
        }
        if (localName.equals("entry") || localName.equals("item")) {
            isReadingEntry = false;
            entries.add(curEntry);
            boolean shouldTerminate =
                earlyTerminationCondition.test(curEntry) ||
                entries.size() == entriesReadLimit;
            if (shouldTerminate) {
                throw new Termination();
            }
        } else if (isReadingEntry) {
            path.removeLast();
        }
    }

    public static List<Map<String, String>> readFromStream(
        InputStream inputStream,
        int entriesReadLimit,
        Predicate<Map<String,String>> earlyTerminationCondition
    ) throws IOException, ParserConfigurationException, SAXException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        RssReader rssReader = new RssReader(entriesReadLimit, earlyTerminationCondition);
        xmlReader.setContentHandler(rssReader);
        try {
            xmlReader.parse(new InputSource(inputStream));
        } catch (Termination e) { /* fine */ }
        return rssReader.entries;
    }

    public static List<Map<String, String>> readFromURL(
        String resource,
        int entriesReadLimit,
        Predicate<Map<String,String>> earlyTerminationCondition
    ) throws IOException, ParserConfigurationException, SAXException {
        return readFromStream(
            new URL(resource).openStream(),
            entriesReadLimit,
            earlyTerminationCondition
        );
    }
}
