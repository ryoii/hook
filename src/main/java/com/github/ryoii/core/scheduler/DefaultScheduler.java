package com.github.ryoii.core.scheduler;

import com.github.ryoii.core.config.Configurable;
import com.github.ryoii.core.config.Configuration;
import com.github.ryoii.core.filter.Filter;
import com.github.ryoii.core.filter.FilterFactory;
import com.github.ryoii.core.model.Task;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultScheduler implements Scheduler, Configurable {

    private final Configuration configuration;
    private Queue<Task> taskQueue;
    private Filter filter;
    private volatile boolean alive;
    private AtomicLong size = new AtomicLong(0);

    public DefaultScheduler(Configuration configuration) {
        this.configuration = configuration;
        taskQueue = new ConcurrentLinkedQueue<>();
        filter = FilterFactory.of(configuration.getFilterName(), configuration);
        alive = true;
    }

    @Override
    public Task getNextTask() {
        return taskQueue.poll();
    }

    @Override
    public void addTask(Task task) {
        if (task.isForce() || filter.allow(task.getUrl())) {
            taskQueue.add(task);
            size.incrementAndGet();
            if (!task.isIgnore()) {
                filter.add(task.getUrl());
            }
        } else {
            // TODO: logger: filtered
        }
    }

    @Override
    public void addTasks(Iterable<Task> tasks) {
        tasks.forEach(this::addTask);
    }

    @Override
    public void countDown() {
        if (size.decrementAndGet() <= 0) {
            close();
            // TODO: logger: closed
        }
    }

    @Override
    public void retry(Task task) {
        // TODO: logger: retry
        taskQueue.add(task);
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void close() {
        alive = false;
    }

    @Override
    public Configuration conf() {
        return configuration;
    }
}
