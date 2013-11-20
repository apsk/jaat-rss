package apsk.jaat.plugin.rss;


import apsk.jaat.core.aggregator.template.AbstractAggregator;
import apsk.jaat.core.option.Option;
import apsk.jaat.core.option.template.OptionManager;
import apsk.jaat.core.option.validation.ValidationException;

import java.util.Map;
import java.util.stream.Stream;

public class RssAggregator extends AbstractAggregator {
    String lastLink = "";
    public RssAggregator() {
        super(new OptionManager(new Option[] {
            Option.string("resource"),
            Option.integral("timeout"),
            Option.integral("max_fetch_count")
        }));
        try { setOption("timeout", "60"); } catch (ValidationException e) {
            System.err.println("RssAggregator - failed to initialize `timeout`(30000):");
            System.err.println("  " + e.getMessage());
        }
        try { setOption("max_fetch_count", "100"); } catch (ValidationException e) {
            System.err.println("RssAggregator - failed to initialize `max_fetch_count`(100):");
            System.err.println("  " + e.getMessage());
        }
    }
    @Override public String getName() { return "rss"; }
    @Override public String getVersion() { return "1.0.0"; }
    @Override
    public Stream<Map<String, String>> receiveData() {
        String resource = getResource();
        try {
            return RssReader.readFromURL(
                resource,
                Integer.parseInt(getOption("timeout")),
                entry -> lastLink == null || lastLink.equals(entry.get("link"))
            ).stream();
        } catch (Exception e) {
            System.err.println("RssAggregator(" + resource + ") - failed receiving data:");
            System.err.println("  " + e.getMessage());
            return null;
        }
    }
}
