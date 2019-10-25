package com.github.ryoii.core.filter;

import com.github.ryoii.core.config.Configurable;
import com.github.ryoii.core.config.Configuration;
import com.github.ryoii.core.model.Task;
import com.github.ryoii.core.model.TaskList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HashFilter implements Filter, Configurable {

    private final Logger logger = LoggerFactory.getLogger("filter");
    private final Configuration configuration;

    public HashFilter(Configuration configuration) {
        this.configuration = configuration;
    }

    private Set<String> set = ConcurrentHashMap.newKeySet();

    @Override
    public TaskList filter(TaskList tasks) {
        Iterator<Task> iterator = tasks.iterator();
        Task task;
        while (iterator.hasNext()) {
            task = iterator.next();

            if (task.isForce() || allow(task.getUrl())) {
                if (!task.isIgnore()) {
                    add(task.getUrl());
                }
            } else {
                iterator.remove();
                logger.debug("Filter the task-" + task.getUrl());
            }
        }
        return tasks;
    }

    @Override
    public boolean allow(String url) {
        return !set.contains(url);
    }

    @Override
    public void add(String url) {
        set.add(url);
    }

    @Override
    public void persistence() {
        String fileName = conf().getName() + ".FUS";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(set);
            oos.flush();
            logger.info("Save " + set.size() + " filter url(s)");
        } catch (FileNotFoundException e) {
            logger.error("Can not create the file:" + fileName);
        } catch (IOException e) {
            logger.error("Can not write the file:" + fileName);
        }
    }

    @Override
    public void antiPersistence() {
        String fileName = conf().getName() + ".FUS";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Set set = (Set) ois.readObject();
            for (Object o : set) {
                this.set.add((String) o);
            }
            logger.info("Load " + set.size() + " filter url(s)");
        } catch (FileNotFoundException e) {
            logger.info("Start a new filter. Can not find the file: " + fileName);
        } catch (IOException e) {
            logger.error("Start a new filter. Can not read the file:" + fileName);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Configuration conf() {
        return this.configuration;
    }
}
