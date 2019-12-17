package com.github.ryoii.core.requester;

import com.github.ryoii.core.config.Configurable;
import com.github.ryoii.core.config.Configuration;
import com.github.ryoii.core.model.Page;
import com.github.ryoii.core.model.Task;
import com.github.ryoii.core.proxy.RandomProxySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClientRequester implements Requester, Configurable {

    private final Logger logger = LoggerFactory.getLogger("requester");
    private final Configuration configuration;
    private HttpClient client;

    HttpClientRequester(Configuration configuration) {
        this.configuration = configuration;
        client = HttpClient.newBuilder()
                .proxy(new RandomProxySelector(configuration.getProxies()))
                .followRedirects(configuration.getRedirectPolicy())
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
        int statusCode = response.statusCode();
        String contentType = response.headers().firstValue("content-type").orElse("");

        logger.info("request url [" + statusCode + "]" + task.getUrl());

        return new Page(task, response.body(), statusCode, contentType);
    }

    @Override
    public Configuration conf() {
        return configuration;
    }
}
