package core.config;

import core.filter.FilterFactory;
import core.requester.RequesterFactory;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    /*
    /* Crawler
    */
    private int threadNum;
    private boolean autoDetectImg;

    public int getThreadNum() {
        return threadNum;
    }

    public Configuration setThreadNum(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public boolean isAutoDetectImg() {
        return autoDetectImg;
    }

    public Configuration setAutoDetectImg(boolean autoDetectImg) {
        this.autoDetectImg = autoDetectImg;
        return this;
    }

    /*
    /* Requester
    */
    private RequesterFactory.RequesterFactoryType requesterType;
    private String userAgent = "";
    private String cookie = "";
    private List<String> headers = new ArrayList<>();
    private long timeout;
    private long connectTimeout;

    public RequesterFactory.RequesterFactoryType getRequesterType() {
        return requesterType;
    }

    public Configuration setRequesterType(RequesterFactory.RequesterFactoryType requesterType) {
        this.requesterType = requesterType;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Configuration setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getCookie() {
        return cookie;
    }

    public Configuration setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public Configuration addHeader(String key, String value) {
        headers.add(key);
        headers.add(value);
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public Configuration setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public Configuration setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /*
    /* Filter
    */
    private FilterFactory.Filters filterName;

    public FilterFactory.Filters getFilterName() {
        return filterName;
    }

    public Configuration setFilterName(FilterFactory.Filters filterName) {
        this.filterName = filterName;
        return this;
    }

    /*
    /* Task
    */
    private int taskDefaultLife;

    public int getTaskDefaultLife() {
        return taskDefaultLife;
    }

    public Configuration setTaskDefaultLife(int taskDefaultLife) {
        this.taskDefaultLife = taskDefaultLife;
        return this;
    }

    public static Configuration defaultConfiguration() {
        Configuration configuration = new Configuration();
        configuration.threadNum = 50;
        configuration.requesterType = RequesterFactory.RequesterFactoryType.HTTPCLIENT_REQUESTER;
        configuration.addHeader("powerBy", "hook");
        configuration.timeout = 30000;
        configuration.connectTimeout = 30000;
        configuration.filterName = FilterFactory.Filters.HASH_FILTER;
        configuration.taskDefaultLife = 5;
        configuration.autoDetectImg = false;
        return configuration;
    }
}
