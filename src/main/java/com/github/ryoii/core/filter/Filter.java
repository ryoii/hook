package com.github.ryoii.core.filter;

public interface Filter {

    boolean allow(String url);

    void add(String url);
}
