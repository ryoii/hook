package com.github.ryoii.core.requester;

import com.github.ryoii.core.config.Configuration;

public interface RequesterFactory {

    Requester getInstance();

    static RequesterFactory of(RequesterFactoryType requesterType, Configuration configuration) {
        RequesterFactory factory;
        switch (requesterType) {
            case HTTPCLIENT_REQUESTER:
                factory = new HttpClientRequesterFactory(configuration);
                break;
            default:
                factory = new HttpClientRequesterFactory(configuration);
                break;
        }
        return factory;
    }

    enum RequesterFactoryType {
        HTTPCLIENT_REQUESTER
    }
}
