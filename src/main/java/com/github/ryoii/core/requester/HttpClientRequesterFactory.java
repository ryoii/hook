package com.github.ryoii.core.requester;

import com.github.ryoii.core.config.Configuration;

import java.net.http.HttpClient;

public class HttpClientRequesterFactory implements RequesterFactory {

    private volatile Requester requester;

    @Override
    public Requester getRequester(Configuration configuration) {
        if (requester == null) {
            synchronized (HttpClient.class) {
                if (requester == null) {
                    requester = new HttpClientRequester(configuration);
                }
            }
        }
        return requester;
    }
}
