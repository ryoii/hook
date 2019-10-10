package core.crawler;

import core.config.Configurable;
import core.config.Configuration;
import core.model.AddOnlyTaskList;
import core.model.Page;
import core.model.Task;
import core.model.TaskList;
import core.pipeline.Pipeline;
import core.requester.Requester;
import core.requester.RequesterFactory;
import core.scheduler.DefaultScheduler;
import core.scheduler.Scheduler;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public abstract class BaseCrawler implements Configurable {

    final private Configuration configuration;
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
    }

    private void init() {
        requesterFactory = RequesterFactory.of(configuration.getRequesterType(), configuration);
        scheduler = new DefaultScheduler(configuration);
        scheduler.addTasks(seed);
    }

    public void start() {
        init();

        CrawlerThread[] hookTasks = new CrawlerThread[configuration.getThreadNum()];
        latch = new CountDownLatch(configuration.getThreadNum());

        for (int i = 0; i < hookTasks.length; i++) {
            hookTasks[i] = new CrawlerThread();
            hookTasks[i].start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public abstract void visit(Page page, AddOnlyTaskList tasks);

    protected void afterVisit(Page page, AddOnlyTaskList taskList) {
    }

    @Override
    public Configuration conf() {
        return this.configuration;
    }


    /**
     * CrawlerThread
     */
    private class CrawlerThread extends Thread {

        @Override
        public void run() {
            int taskLife = configuration.getRetryTime();
            long restTime = configuration.getRestTime();
            Requester requester = requesterFactory.getInstance();

            try {
                while (scheduler.isAlive()) {
                    Task task = scheduler.getNextTask();
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
                        scheduler.addTasks(taskList);
                        scheduler.countDown();
                    } catch (InterruptedException | IOException e) {
                        // TODO: logger: exception
                        if (task == null) continue;
                        if (task.decreaseLifeAndReturn()) {
                            scheduler.retry(task);
                        } else {
                            scheduler.countDown();
                            // TODO: logger: give up
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        scheduler.countDown();
                    }

                    if (restTime > 0) {
                        try {
                            Thread.sleep(restTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } finally {
                latch.countDown();
            }

        }
    }
}
