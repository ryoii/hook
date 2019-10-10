package core.model;

public interface MetaSetter<T> {

    T meta(String key, String value);

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
