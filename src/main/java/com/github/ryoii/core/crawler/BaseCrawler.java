package com.github.ryoii.core.crawler;

import com.github.ryoii.core.config.Configurable;
import com.github.ryoii.core.config.Configuration;
import com.github.ryoii.core.model.AddOnlyTaskList;
import com.github.ryoii.core.model.Page;
import com.github.ryoii.core.model.Task;
import com.github.ryoii.core.model.TaskList;
import com.github.ryoii.core.pipeline.Pipeline;
import com.github.ryoii.core.requester.Requester;
import com.github.ryoii.core.requester.RequesterFactory;
import com.github.ryoii.core.scheduler.DefaultScheduler;
import com.github.ryoii.core.scheduler.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public abstract class BaseCrawler implements Configurable {

    private Logger logger = LogManager.getLogger("crawler");
    private Configuration configuration;
    private Scheduler scheduler;
    private RequesterFactory requesterFactory;
    private Pipeline pipeline;

    private CountDownLatch latch;
    private TaskList seed = new TaskList();

    public BaseCrawler() {
        this(Configuration.defaultConfiguration());
    }

    public BaseCrawler(Configuration configuration) {
        this.configuration = configuration;
        if (configuration.getName() == null) {
            configuration.setName("crawler-" + UUID.randomUUID().toString().substring(0, 6));
        }
    }

    public Task addSeed(String url) {
        return addSeed(url, false);
    }

    public Task addSeed(String url, boolean force) {
        Task task = new Task(url, force);
        task.setLife(configuration.getRetryTime());
        seed.add(task);
        return task;
    }

    public Task addSeed(String url, String type) {
        return addSeed(url, type, false);
    }

    public Task addSeed(String url, String type, boolean force) {
        Task task = new Task(url, force).type(type);
        task.setLife(configuration.getRetryTime());
        seed.add(task);
        return task;
    }

    public BaseCrawler setSeeds(TaskList seeds) {
        this.seed = seeds;
        return this;
    }

    public abstract void visit(Page page, AddOnlyTaskList tasks);

    protected void afterVisit(Page page, AddOnlyTaskList taskList) {
    }

    @Override
    public Configuration conf() {
        return this.configuration;
    }

    public BaseCrawler conf(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    private void init() {
        requesterFactory = RequesterFactory.of(configuration.getRequesterType(), configuration);
        scheduler = new DefaultScheduler(configuration);
        if (conf().isPersistence()) {
            scheduler.antiPersistence();
        }
        scheduler.addSeeds(seed);
    }

    public void start() {
        init();

        CrawlerThread[] hookTasks = new CrawlerThread[configuration.getThreadNum()];
        latch = new CountDownLatch(configuration.getThreadNum());

        for (int i = 0; i < hookTasks.length; i++) {
            hookTasks[i] = new CrawlerThread("crawler-" + i);
            hookTasks[i].start();
        }

        new DaemonSafeStopThead(hookTasks).start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (conf().isPersistence()) {
            scheduler.persistence();
        }
    }

    /**
     * CrawlerThread
     */
    private class CrawlerThread extends Thread {

        private CrawlerThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            int taskLife = configuration.getRetryTime();
            long restTime = configuration.getRestTime();
            Requester requester = requesterFactory.getInstance();

            try {
                while (scheduler.isAlive()) {
                    Task task = scheduler.poll();
                    try {
                        if (task == null) {
                            Thread.sleep(100);
                            continue;
                        }
                        Page page = requester.getPage(task);
                        TaskList taskList = new TaskList();
                        visit(page, taskList);
                        afterVisit(page, taskList);
                        taskList.forEach(t -> t.setLife(taskLife));
                        scheduler.addNextTasks(taskList);
                        if (restTime > 0) {
                            Thread.sleep(restTime);
                        }
                    } catch (InterruptedException e) {
                        // put back the polled task
                        if (task != null) {
                            scheduler.addTask(task);
                        }
                        logger.info(Thread.currentThread().getName() + " stopped");
                        return;
                    } catch (IOException e) {
                        logger.error(e);
                        // assert task != null;
                        if (task.decreaseLifeAndReturn()) {
                            scheduler.retry(task);
                        } else {
                            scheduler.finish(task);
                            logger.info("task-" + task.getUrl() + " was given up");
                        }
                    } catch (Exception e) {
                        scheduler.finish(task);
                        e.printStackTrace();
                    }
                }
            } finally {
                latch.countDown();
            }
        }
    }

    private class DaemonSafeStopThead extends Thread{

        private CrawlerThread[] crawlerThreads;

        private DaemonSafeStopThead(CrawlerThread[] crawlerThreads) {
            super("crawler-daemon");
            this.crawlerThreads = crawlerThreads;
            setDaemon(true);
        }

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            String line;
            while (true) {
                line = scanner.nextLine();
                if (!line.equalsIgnoreCase("q")) continue;
                for (CrawlerThread crawlerThread : crawlerThreads) {
                    crawlerThread.interrupt();
                }
                logger.info("stop the crawler");
                break;
            }
        }
    }
}
