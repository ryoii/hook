package com.github.ryoii.core.scheduler;

import com.github.ryoii.core.config.Configurable;
import com.github.ryoii.core.config.Configuration;
import com.github.ryoii.core.model.Task;
import com.github.ryoii.core.model.TaskList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultScheduler implements Scheduler, Configurable {

    private final Logger logger = LoggerFactory.getLogger("scheduler");
    private final Configuration configuration;
    private Queue<Task> taskQueue;
    private volatile boolean alive;
    private AtomicLong activeTaskNum = new AtomicLong(0);

    public DefaultScheduler(Configuration configuration) {
        this.configuration = configuration;
        taskQueue = new ConcurrentLinkedQueue<>();

        alive = true;
    }

    @Override
    public Task poll() {
        return taskQueue.poll();
    }

    @Override
    public void addSeeds(TaskList tasks) {
        tasks.forEach(this::addTask);
        if (taskQueue.size() <= 0) {
            logger.info("No seeds available.");
            close();
        }
    }

    @Override
    public void addTask(Task task) {
        taskQueue.add(task);
        activeTaskNum.incrementAndGet();
    }

    @Override
    public void addNextTasks(TaskList tasks) {
        tasks.forEach(this::addTask);
        logger.info("Add " + tasks.size() + " task(s), " + (activeTaskNum.get() - taskQueue.size()) + " task(s) running, left " + taskQueue.size() + " task(s)");
    }

    @Override
    public void finish(Task task) {
        /* The task has been remove from the taskQueue, so ignore it.
        *  Use activeTaskNum to recode the number of active tasks instead of the size of taskQueue.
        *  The reason why recode the number of active tasks is to judge whether the schedule
        *  should be closed.
        *  Once it finish a task and no active tasks any more, it means that no more tasks
        *  will be added in the future. */
        if (activeTaskNum.decrementAndGet() <= 0) {
            close();
        }
    }

    @Override
    public void retry(Task task) {
        logger.info("retry task-" + task.getUrl());
        taskQueue.add(task);
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void persistence() {
        String fileName = conf().getName() + ".STQ";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(taskQueue);
            oos.flush();
            logger.info("Save " + taskQueue.size() + "task(s)");
        } catch (FileNotFoundException e) {
            logger.error("Can not create the file: " + fileName);
        } catch (IOException e) {
            logger.error("Can not write the file: " + fileName);
        }
    }

    @Override
    public void antiPersistence() {
        String fileName = conf().getName() + ".STQ";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Queue queue = (Queue) ois.readObject();
            for (Object o : queue) {
                taskQueue.add((Task) o);
            }
            activeTaskNum.set(taskQueue.size());
            logger.info("Load " + taskQueue.size() + " task(s)");
        } catch (FileNotFoundException e) {
            logger.info("Start a new scheduler. Can not find the file: " + fileName);
        } catch (IOException e) {
            logger.error("Start a new scheduler. Can not read the file:" + fileName);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        alive = false;
        logger.info("Scheduler has been closed");
    }

    @Override
    public Configuration conf() {
        return configuration;
    }
}
