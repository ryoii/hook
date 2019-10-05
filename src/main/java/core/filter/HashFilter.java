package core.filter;

import core.config.Configurable;
import core.config.Configuration;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HashFilter implements Filter, Configurable {

    private final Configuration configuration;

    public HashFilter(Configuration configuration) {
        this.configuration = configuration;
    }

    private Set<String> set = ConcurrentHashMap.newKeySet();

    @Override
    public boolean allow(String url) {
        return !set.contains(url);
    }

    @Override
    public void add(String url) {
        set.add(url);
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }
}
