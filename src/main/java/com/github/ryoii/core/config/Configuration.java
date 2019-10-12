package com.github.ryoii.core.config;

import com.github.ryoii.core.filter.FilterFactory;
import com.github.ryoii.core.proxy.Proxies;
import com.github.ryoii.core.requester.RequesterFactory;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    /*
    /* Crawler
    */
    private int threadNum;
    private boolean autoDetect;
    private boolean autoDetectImg;
    private long restTime;

    public int getThreadNum() {
        return threadNum;
    }

    public Configuration setThreadNum(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public boolean isAutoDetect() {
        return autoDetect;
    }

    public Configuration setAutoDetect(boolean autoDetect) {
        this.autoDetect = autoDetect;
        return this;
    }

    public boolean isAutoDetectImg() {
        return autoDetectImg;
    }

    public Configuration setAutoDetectImg(boolean autoDetectImg) {
        this.autoDetectImg = autoDetectImg;
        return this;
    }

    public long getRestTime() {
        return restTime;
    }

    public Configuration setRestTime(long restTime) {
        this.restTime = restTime;
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
    private Proxies proxies;

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

    public Proxies getProxies() {
        return proxies;
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
    private int retryTime;

    public int getRetryTime() {
        return retryTime;
    }

    public Configuration setRetryTime(int retryTime) {
        this.retryTime = retryTime;
        return this;
    }

    public static Configuration defaultConfiguration() {
        Configuration configuration = new Configuration();
        configuration.threadNum = 50;
        configuration.requesterType = RequesterFactory.RequesterFactoryType.HTTPCLIENT_REQUESTER;
        configuration.addHeader("powerBy", "hook");
        configuration.timeout = 30000;
        configuration.connectTimeout = 30000;
        configuration.proxies = new Proxies();
        configuration.filterName = FilterFactory.Filters.HASH_FILTER;
        configuration.retryTime = 5;
        configuration.autoDetect = false;
        configuration.autoDetectImg = false;
        configuration.restTime = 0;
        return configuration;
    }
}
