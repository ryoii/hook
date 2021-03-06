package com.github.ryoii.core.config;

import com.github.ryoii.core.proxy.Proxies;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

    /*
    /* Crawler
    */
    private String name;
    private int threadNum;
    private boolean autoDetect;
    private boolean autoDetectImg;
    private boolean persistence;
    private long restTime;

    public String getName() {
        return name;
    }

    public Configuration setName(String name) {
        this.name = name;
        return this;
    }

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

    public boolean isPersistence() {
        return persistence;
    }

    public Configuration setPersistence(boolean persistence) {
        this.persistence = persistence;
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
    private String userAgent = "";
    private String cookie = "";
    private List<String> headers = new ArrayList<>();
    private long timeout;
    private long connectTimeout;
    private HttpClient.Redirect redirectPolicy = HttpClient.Redirect.NEVER;
    private Proxies proxies;

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

    public HttpClient.Redirect getRedirectPolicy() {
        return redirectPolicy;
    }

    public Configuration setRedirectPolicy(HttpClient.Redirect policy) {
        this.redirectPolicy = policy;
        return this;
    }

    public Proxies getProxies() {
        return proxies;
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
        configuration.addHeader("powerBy", "hook");
        configuration.timeout = 30000;
        configuration.connectTimeout = 30000;
        configuration.proxies = new Proxies();
        configuration.retryTime = 5;
        configuration.autoDetect = false;
        configuration.autoDetectImg = false;
        configuration.restTime = 0;
        return configuration;
    }
}
