package core.proxy;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Proxies {

    private List<Proxy> proxies = new ArrayList<>();

    public Proxies add(String host, String port) {
        return add(host, Integer.parseInt(port));
    }

    public Proxies add(String host, int port) {
        return add(new java.net.Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port)));
    }

    public Proxies add(Proxy proxy) {
        proxies.add(proxy);
        return this;
    }

    public Proxies addAll(Proxy... proxies) {
        Collections.addAll(this.proxies, proxies);
        return this;
    }

    public Proxies addAll(Collection<Proxy> proxies) {
        this.proxies.addAll(proxies);
        return this;
    }

    public int size() {
        return proxies.size();
    }

    Proxy get(int index) {
        return proxies.get(index);
    }

}
