package apsk.jaat.plugin.rss;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        bundleContext.registerService(RssAggregator.class.getName(), new RssAggregator(), null);
    }
    @Override
    public void stop(BundleContext bundleContext) throws Exception {}
}
