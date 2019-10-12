package com.github.ryoii.core.filter;

import com.github.ryoii.core.model.Persistence;

public interface Filter extends Persistence {

    boolean allow(String url);

    void add(String url);
}
