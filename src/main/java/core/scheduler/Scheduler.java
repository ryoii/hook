package core.scheduler;

import core.model.Task;

public interface Scheduler {

    Task getNextTask();

    void addTask(Task task);

    void addTasks(Iterable<Task> tasks);

    void countDown();

    void retry(Task task);

    boolean isAlive();

    void close();
}
