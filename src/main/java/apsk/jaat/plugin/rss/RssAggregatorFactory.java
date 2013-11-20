package apsk.jaat.plugin.rss;

import apsk.jaat.core.aggregator.Aggregator;
import apsk.jaat.core.aggregator.AggregatorFactory;

public class RssAggregatorFactory implements AggregatorFactory {
    @Override
    public Aggregator create() {
        return new RssAggregator();
    }
}
