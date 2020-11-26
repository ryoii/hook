package com.github.ryoii.core.crawler;

import com.github.ryoii.core.config.Configuration;
import com.github.ryoii.core.model.*;
import com.github.ryoii.core.regex.RegexRules;

import java.net.Proxy;
import java.net.http.HttpClient;
import java.util.*;
import java.util.function.BiConsumer;

public class Hook implements MetaSetter<Hook> {

    public static Hook newHook() {
        return new Hook();
    }

    public static Hook newHook(String name) {
        Hook hook = new Hook();
        hook.configuration.setName(name);
        return hook;
    }

    /* Task */
    private TaskList seeds = new TaskList();

    /**
     * Add a seed for the crawler.
     *
     * @param url the seed you wanna add
     * @return Hook
     */
    public Hook addSeed(String url) {
        return addSeed(url, false);
    }

    /**
     * Add a seed that will not be filtered for the crawler.
     *
     * @param url   the seed you wanna add
     * @param force true, if you wanna the seed will not be filtered
     * @return Hook
     */
    public Hook addSeed(String url, boolean force) {
        seeds.add(new Task(url, force));
        return this;
    }

    /**
     * Add a seed with type for the crawler
     *
     * @param url  the seed you wanna add
     * @param type type of the seed
     * @return Hook
     */
    public Hook addSeed(String url, String type) {
        return addSeed(url, type, false);
    }

    /**
     * Add a seed with type that will not be filtered for the crawler.
     *
     * @param url   the seed you wanna add
     * @param type  type of the seed
     * @param force true, if you wanna the seed will not be filtered
     * @return Hook
     */
    public Hook addSeed(String url, String type, boolean force) {
        seeds.add(new Task(url, force).type(type));
        return this;
    }

    /**
     * Add a meta for the seed you add lastly.
     *
     * @param key   meta key
     * @param value meta value
     * @return Hook
     */
    public Hook meta(String key, String value) {
        seeds.get(seeds.size() - 1).meta(key, value);
        return this;
    }

    /**
     * Add meta for the seed you add lastly.
     *
     * @param meta meta
     * @return Hook
     */
    @Override
    public Hook meta(Map<String, String> meta) {
        seeds.get(seeds.size() - 1).meta(meta);
        return this;
    }

    /* Configuration */
    private Configuration configuration = Configuration.defaultConfiguration();


    /**
     * Set configuration.
     *
     * @param conf configuration
     * @return Hook
     */
    private Hook conf(Configuration conf) {
        configuration = conf;
        return this;
    }

    /**
     * Auto detect urls from hyperlinks in html document.
     *
     * @param autoDetect true, if enable auto detect hyperlinks
     * @return Hook
     */
    public Hook autoDetect(boolean autoDetect) {
        configuration.setAutoDetect(autoDetect);
        return this;
    }

    /**
     * Auto detect urls from img in html document only enable while {@link Hook#autoDetect(boolean)} is true.
     *
     * @param autoDetectImg true, if enable auto detect img
     * @return Hook
     */
    public Hook autoDetectImg(boolean autoDetectImg) {
        configuration.setAutoDetectImg(autoDetectImg);
        return this;
    }

    /**
     * Allow the crawler to continue from the last task by saving the data
     *
     * @param isPersistence true if allow the crawler save its data
     * @return Hook
     */
    public Hook persistence(boolean isPersistence) {
        configuration.setPersistence(isPersistence);
        return this;
    }

    /**
     * Set each thread's sleep time after finish a task.
     *
     * @param time rest time (millisecond)
     * @return Hook
     */
    public Hook restTime(long time) {
        configuration.setRestTime(time);
        return this;
    }

    /**
     * Set User Agent
     *
     * @param userAgent user agent
     * @return Hook
     */
    public Hook userAgent(String userAgent) {
        configuration.setUserAgent(userAgent);
        return this;
    }

    /**
     * Set cookie
     *
     * @param cookie cookie
     * @return Hook
     */
    public Hook cookie(String cookie) {
        configuration.setCookie(cookie);
        return this;
    }

    /**
     * Add cookie.
     * Cookies are split with semicolons. Concat new cookie after the current cookie string.
     *
     * @param cookie cookie
     * @return Hook
     */
    public Hook addCookie(String cookie) {
        String origin = configuration.getCookie();
        if (origin.endsWith(";")) {
            origin += cookie;
        } else {
            origin += (";" + cookie);
        }
        configuration.setCookie(origin);
        return this;
    }

    /**
     * Add header for the crawler.
     *
     * @param key   header key
     * @param value header value
     * @return Hook
     */
    public Hook header(String key, String value) {
        configuration.addHeader(key, value);
        return this;
    }

    /**
     * Set request timeout for the crawler.
     * When the crawler get no response within designated time, it will throw an exception.
     *
     * @param timeout request timeout
     * @return Hook
     */
    public Hook timeout(long timeout) {
        configuration.setTimeout(timeout);
        return this;
    }

    /**
     * Set connect timeout for the crawler.
     * When the crawler can not connect to the server within designated time, it will throw an exception.
     *
     * @param connectTimeout connect timeout
     * @return Hook
     */
    public Hook connectTimeout(long connectTimeout) {
        configuration.setConnectTimeout(connectTimeout);
        return this;
    }

    public Hook redirectPolicy(HttpClient.Redirect policy) {
        configuration.setRedirectPolicy(policy);
        return this;
    }

    /**
     * Add a proxy for the crawler.
     * If more than one proxy is set, crawler will select one proxy randomly for each request.
     *
     * @param host proxy host or ip
     * @param port proxy port
     * @return Hook
     */
    public Hook proxy(String host, String port) {
        configuration.getProxies().add(host, port);
        return this;
    }

    /**
     * @see #proxy(String, String)
     */
    public Hook proxy(String host, int port) {
        configuration.getProxies().add(host, port);
        return this;
    }

    /**
     * @see #proxy(String, String)
     */
    public Hook proxy(Proxy proxy) {
        configuration.getProxies().add(proxy);
        return this;
    }

    public Hook proxy(String host) {
        String[] s = host.split(":");
        return proxy(s[0], s[1]);
    }

    /**
     * @see #proxy(String, String)
     */
    public Hook proxies(Proxy... proxies) {
        this.configuration.getProxies().addAll(proxies);
        return this;
    }

    /**
     * @see #proxy(String, String)
     */
    public Hook proxies(Collection<Proxy> proxies) {
        this.configuration.getProxies().addAll(proxies);
        return this;
    }

    public Hook clearProxies() {
        this.configuration.getProxies().clear();
        return this;
    }

    /* Task */

    /**
     * Set how many times a task will retry.
     * When an exception occur during a task running, the task will attempt to retry.
     *
     * @param time max retry time
     * @return Hook
     */
    public Hook retryTime(int time) {
        configuration.setRetryTime(time);
        return this;
    }


    /* Regex */
    private RegexRules regexRules = new RegexRules();

    /**
     * Add a regex rule for the auto detect crawler.
     *
     * @param regex regex rule
     * @return Hook
     */
    public Hook regex(String regex) {
        regexRules.addRule(regex);
        return this;
    }

    /**
     * Add a regex rule for the auto detect crawler and meta a type for the url.
     *
     * @param regex regex rule
     * @param type  type of the url
     * @return Hook
     */
    public Hook regex(String regex, String type) {
        regexRules.addRule(regex, type);
        return this;
    }

    private HashMap<String, List<SelectRule>> selectRuleMap = new HashMap<>();
    private BiConsumer<Page, AddOnlyTaskList> visitHandler = (page, tasks) ->
            selectRuleMap.getOrDefault(page.type(), List.of())
                    .forEach(rule -> tasks.add(rule.select(page).toTaskList().meta(rule.meta)));

    /**
     * Add a callback after a task is requested,
     * handler will invoke in order
     *
     * @param visitHandler visitHandler
     * @return Hook
     */
    public Hook visitHandler(BiConsumer<Page, AddOnlyTaskList> visitHandler) {
        this.visitHandler = this.visitHandler.andThen(visitHandler);
        return this;
    }

    /**
     * Auto detect links from page according to html selector
     * while type of page is matched.
     *
     * @param type type of page
     * @param selector html selector
     * @return SelectRule
     */
    public SelectRule select(String type, String selector) {
        SelectRule selectRule = new SelectRule(this, selector, false);
        if (selectRuleMap.containsKey(type)) {
            selectRuleMap.get(type).add(selectRule);
        } else {
            ArrayList<SelectRule> list = new ArrayList<>();
            list.add(selectRule);
            selectRuleMap.put(type, list);
        }
        return selectRule;
    }

    /**
     * @see #start(int)
     */
    public void start() {
        start(configuration.getThreadNum());
    }

    private AutoDetectCrawler crawler = null;
    /**
     * Start the crawler.
     *
     * @param threadNum the thread num of the crawler
     */
    public void start(int threadNum) {
        crawler = new AutoDetectCrawler(configuration) {
            @Override
            public void visit(Page page, AddOnlyTaskList tasks) {
                visitHandler.accept(page, tasks);
            }
        };
        crawler.setRegexRules(regexRules).setSeeds(seeds).conf().setThreadNum(threadNum);
        crawler.start();
    }

    public void close() {
        if (crawler != null) {
            crawler.close();
        }
    }

    public static class SelectRule implements MetaSetter<SelectRule> {
        private Hook hook;
        private HashMap<String, String> meta;
        private String selector;
        private boolean containSrc;

        private SelectRule(Hook hook, String selector, boolean containSrc) {
            this.hook = hook;
            this.meta = new HashMap<>();
            this.selector = selector;
            this.containSrc = containSrc;
        }

        public SelectRule select(String type, String selector) {
            return hook.select(type, selector);
        }

        private Links select(Page page) {
            return page.links(selector, containSrc);
        }

        public Hook visitHandler(BiConsumer<Page, AddOnlyTaskList> visitHandler) {
            return hook.visitHandler(visitHandler);
        }

        public Hook hook() {
            return hook;
        }

        @Override
        public SelectRule meta(String key, String value) {
            meta.put(key, value);
            return this;
        }

        @Override
        public SelectRule meta(Map<String, String> meta) {
            this.meta.putAll(meta);
            return this;
        }
    }
}
