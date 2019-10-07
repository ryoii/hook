package core.requester;

import core.config.Configuration;

import java.net.http.HttpClient;

public class HttpClientRequesterFactory implements RequesterFactory{

    private volatile Requester instance;
    private Configuration configuration;

    HttpClientRequesterFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Requester getInstance() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClientRequester(configuration);
                }
            }
        }
        return instance;
    }
}
