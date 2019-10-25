package com.github.ryoii.core.filter;

import com.github.ryoii.core.model.Persistence;
import com.github.ryoii.core.model.TaskList;

public interface Filter extends Persistence {

    TaskList filter(TaskList tasks);

    boolean allow(String url);

    void add(String url);
}
