package com.github.ryoii.core.filter;

import com.github.ryoii.core.config.Configurable;
import com.github.ryoii.core.config.Configuration;
import com.github.ryoii.core.model.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HashFilter implements Filter, Configurable {

    private final Logger logger = LogManager.getLogger("filter");
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
        String fileName = conf().getName() + ".FUS";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(set);
            oos.flush();
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
        } catch (FileNotFoundException e) {
            logger.info("Start a new filter. Can not find the file: " + fileName);
        } catch (IOException e) {
            logger.error("Start a new filter. Can not read the file:" + fileName);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
    }

    @Override
    public Configuration conf() {
        return this.configuration;
    }
}
