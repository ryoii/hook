package com.github.ryoii.core.proxy;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Random;

public class RandomProxySelector extends ProxySelector {

    private Proxies proxies;
    private Random random = new Random();

    public RandomProxySelector(Proxies proxies) {
        this.proxies = proxies;
    }

    @Override
    public List<Proxy> select(URI uri) {
        if (proxies.size() > 0)
            return List.of(proxies.get(random.nextInt(proxies.size())));
        else
            return List.of(Proxy.NO_PROXY);
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        //ignore
        System.out.println("Failed: " + sa.toString());
        ioe.printStackTrace();
    }
}
