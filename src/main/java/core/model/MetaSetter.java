package core.model;

import java.util.Map;

public interface MetaSetter<T> {

    T meta(String key, String value);

    T meta(Map<String, String> meta);

    default T meta(String key, Number number) {
        return meta(key, String.valueOf(number));
    }

    default T meta(String key, boolean bool) {
        return meta(key, String.valueOf(bool));
    }

    default T type(String type) {
        return meta(MetaGetter.KEY_TYPE, type);
    }
}
