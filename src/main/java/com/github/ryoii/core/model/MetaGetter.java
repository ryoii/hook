package com.github.ryoii.core.model;

public interface MetaGetter {

    String KEY_TYPE = "hook_type";

    String meta(String key);

    default int metaAsInt(String key) {
        return Integer.parseInt(meta(key));
    }

    default boolean metaAsBoolean(String key) {
        return Boolean.valueOf(meta(key));
    }

    default double metaAsDouble(String key) {
        return Double.valueOf(meta(key));
    }

    default long metaAsLong(String key) {
        return Long.valueOf(meta(key));
    }

    default String type() {
        return meta(KEY_TYPE);
    }
}
