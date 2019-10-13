package com.github.ryoii.core.scheduler;

import com.github.ryoii.core.config.Configurable;
import com.github.ryoii.core.config.Configuration;
import com.github.ryoii.core.filter.Filter;
import com.github.ryoii.core.filter.FilterFactory;
import com.github.ryoii.core.model.Persistence;
import com.github.ryoii.core.model.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultScheduler implements Scheduler, Configurable {

    private final Logger logger = LogManager.getLogger("scheduler");
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
            logger.debug("Filter the task-" + task.getUrl());
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
            logger.info("Scheduler has been closed");
        }
        logger.info(size + " task(s) left");
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
        } catch (FileNotFoundException e) {
            logger.error("Can not create the file: " + fileName);
        } catch (IOException e) {
            logger.error("Can not write the file: " + fileName);
        }
        filter.persistence();
    }

    @Override
    public void antiPersistence() {
        String fileName = conf().getName() + ".STQ";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Queue queue = (Queue) ois.readObject();
            for (Object o : queue) {
                taskQueue.add((Task) o);
            }
            size.set(taskQueue.size());
        } catch (FileNotFoundException e) {
            logger.info("Start a new scheduler. Can not find the file: " + fileName);
        } catch (IOException e) {
            logger.error("Start a new scheduler. Can not read the file:" + fileName);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        filter.antiPersistence();
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
