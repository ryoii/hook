package com.github.ryoii.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Task implements MetaGetter, MetaSetter<Task>, Serializable {

    private String url;
    private boolean force;
    private boolean ignore = false;
    private HashMap<String, String> metaMap = new HashMap<>();
    private int life = 1;

    public Task(String url) {
        this(url, false);
    }

    public Task(String url, boolean force) {
        this.url = url;
        this.force = force;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isForce() {
        return force;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isAlive() {
        return life > 0;
    }

    public boolean decreaseLifeAndReturn() {
        life--;
        return isAlive();
    }

    @Override
    public String meta(String key) {
        return metaMap.get(key);
    }

    @Override
    public Task meta(String key, String value) {
        metaMap.put(key, value);
        return this;
    }

    @Override
    public Task meta(Map<String, String> meta) {
        metaMap.putAll(meta);
        return this;
    }
}
