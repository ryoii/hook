package core.filter;

import core.config.Configuration;

public class FilterFactory {

    public static Filter of(Filters filterName, Configuration configuration) {
        Filter filter;
        switch (filterName) {
            case HASH_FILTER:
                filter = new HashFilter(configuration);
                break;
            default:
                filter = new HashFilter(configuration);
                break;
        }
        return filter;
    }

    public enum Filters {
        HASH_FILTER
    }
}
