package com.cec.sbs.client.support;

import javax.annotation.PostConstruct;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClientManager {

    @Value("${http-pool.maxConnections}")
    private int maxPoolConnections;

    @Value("${http-pool.routeMaxConnections}")
    private int maxConnectionsPerRoute;

    @Value("${http-timeouts.socketReadTimeout}")
    private int socketReadTimeout;

    @Value("${http-timeouts.socketConnectTimeout}")
    private int socketConnectTimeout;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(socketReadTimeout)
                .setConnectTimeout(socketConnectTimeout)
                .build();

        HttpClient defaultHttpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setConnectionManager(connectionManager).build();

        connectionManager.setMaxTotal(maxPoolConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);

        ClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(defaultHttpClient);

        restTemplate = new RestTemplate(clientHttpRequestFactory);

    }

    public int getMaxPoolConnections() {
        return maxPoolConnections;
    }

    public void setMaxPoolConnections(int maxPoolConnections) {
        this.maxPoolConnections = maxPoolConnections;
    }

    public int getMaxConnectionsPerRoute() {
        return maxConnectionsPerRoute;
    }

    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public int getSocketReadTimeout() {
        return socketReadTimeout;
    }

    public void setSocketReadTimeout(int socketReadTimeout) {
        this.socketReadTimeout = socketReadTimeout;
    }

    public int getSocketConnectTimeout() {
        return socketConnectTimeout;
    }

    public void setSocketConnectTimeout(int socketConnectTimeout) {
        this.socketConnectTimeout = socketConnectTimeout;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
