package core.requester;

import core.config.Configurable;
import core.config.Configuration;

public class RequesterFactory {

    public static Requester of(RequesterType requesterType, Configuration configuration) {
        Requester requester;
        switch (requesterType) {
            case HTTPCLIENT_REQUESTER:
                requester = new HttpClientRequester(configuration);
                break;
            default:
                requester = new HttpClientRequester(configuration);
                break;
        }
        return requester;
    }

    public enum RequesterType {
        HTTPCLIENT_REQUESTER
    }
}
