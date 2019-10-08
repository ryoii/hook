package core.requester;

import core.config.Configurable;
import core.config.Configuration;
import core.model.Page;
import core.model.Task;
import core.proxy.RandomProxySelector;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClientRequester implements Requester, Configurable {

    private final Configuration configuration;
    private HttpClient client;

    HttpClientRequester(Configuration configuration) {
        this.configuration = configuration;
        client = HttpClient.newBuilder()
                .proxy(new RandomProxySelector(configuration.getProxies()))
                .connectTimeout(Duration.ofMillis(configuration.getConnectTimeout()))
                .build();
    }

    @Override
    public Page getPage(Task task) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(task.getUrl()))
                .timeout(Duration.ofMillis(configuration.getTimeout()))
                .header("cookie", configuration.getCookie())
                .header("user-agent", configuration.getUserAgent())
                .headers(configuration.getHeaders().toArray(new String[]{}))
                .GET().build();
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        String contentType = response.headers().firstValue("content-type").orElse("");
        return new Page(task, response.body(), contentType);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
