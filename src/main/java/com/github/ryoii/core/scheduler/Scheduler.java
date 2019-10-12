package com.github.ryoii.core.scheduler;

import com.github.ryoii.core.model.Persistence;
import com.github.ryoii.core.model.Task;

public interface Scheduler extends Persistence {

    Task getNextTask();

    void addTask(Task task);

    void addTasks(Iterable<Task> tasks);

    void countDown();

    void retry(Task task);

    boolean isAlive();

    void close();
}
