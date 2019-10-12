package com.github.ryoii.core.filter;

import com.github.ryoii.core.config.Configurable;
import com.github.ryoii.core.config.Configuration;
import com.github.ryoii.core.model.Persistence;

import java.io.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HashFilter implements Filter, Configurable {

    private final Configuration configuration;

    public HashFilter(Configuration configuration) {
        this.configuration = configuration;
    }

    private Set<String> set = ConcurrentHashMap.newKeySet();

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
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(conf().getName() + ".FUS"))) {
            oos.writeObject(set);
            oos.flush();
        } catch (FileNotFoundException e) {
            //TODO log can not create file
        } catch (IOException e) {
            //TODO log can not write file
        }
    }

    @Override
    public void antiPersistence() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(conf().getName() + ".FUS"))) {
            Set set = (Set) ois.readObject();
            for (Object o : set) {
                this.set.add((String) o);
            }
        } catch (FileNotFoundException e) {
            //TODO log can not find file
            return;
        } catch (IOException e) {
            //TODO log can not read file
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Configuration conf() {
        return this.configuration;
    }
}
