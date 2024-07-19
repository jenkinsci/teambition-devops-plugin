package io.jenkins.plugins.teambition.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class RestApiClient {
    private HttpClient httpClient;
    
    public RestApiClient() {
        this.httpClient = HttpClients.createDefault();
    }
    
    private final static int connectTimeout = 60000;
    private final static int socketTimeout = 60000;
    private final static int connReqTimeout = 5000;
    
    public HttpResponse doGetRequest(String url) throws Exception {
        HttpGet request = new HttpGet(url);
        return executeRequest(request);
    }
    
    public HttpResponse doPostRequest(String url, String jsonStr) throws Exception {
        HttpPost request = new HttpPost(url);
        request.setEntity(new StringEntity(jsonStr, ContentType.APPLICATION_JSON));
        return executeRequest(request);
    }
    
    private HttpResponse executeRequest(HttpRequestBase request) throws IOException {
        RequestConfig reqConfig = RequestConfig.custom().setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout).setConnectionRequestTimeout(connReqTimeout).build();
        request.setConfig(reqConfig);
        return httpClient.execute(request);
    }
}
