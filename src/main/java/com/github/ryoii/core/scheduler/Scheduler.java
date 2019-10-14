package com.github.ryoii.core.scheduler;

import com.github.ryoii.core.model.Persistence;
import com.github.ryoii.core.model.Task;
import com.github.ryoii.core.model.TaskList;

public interface Scheduler extends Persistence {

    /**
     * Get a task that is going to be executed, returning null while task queue is empty.
     *
     * @return the next crawler task
     */
    Task poll();

    /**
     * Add origin seeds for crawler.
     *
     * @param tasks the origin seed for crawler
     */
    void addSeeds(TaskList tasks);

    /**
     * Add a task
     *
     * @param task task
     */
    void addTask(Task task);

    /**
     * Add tasks for crawler. This method differs from {@link #addSeeds(TaskList)},
     * it will invoke {@link #finish(Task)} after this method.
     *
     * @param tasks the task returned from the page
     */
    void addNextTasks(TaskList tasks);

    /**
     * Remove the finish task from the scheduler.
     *
     * @param task the task will be remove
     */
    void finish(Task task);

    /**
     * Retry the task that failed because of an exception or other cause,
     * the task will be put back to the schedule.
     *
     * @param task the task will retry
     */
    void retry(Task task);

    /**
     * If the scheduler is alive.
     *
     * @return {@code true} if the scheduler has no any active task, else {@code false}
     */
    boolean isAlive();

    /**
     * Close the scheduler and set {@code isAlive = false}
     */
    void close();
}
