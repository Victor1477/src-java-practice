package com.java.practice;

import java.util.ArrayList;
import java.util.List;

public class Params {

    private String url;
    private String requestMethod;
    private List<Header> headers;
    private Integer threads;
    private Integer requestsPerThread;
    private String requestBody;

    public Params() {
        this.url = "http://example.com.br";
        this.requestMethod = "GET";
        this.headers = new ArrayList<>();
        this.headers.add(new Header("Content-Type", "application/json"));
        this.threads = 1;
        this.requestsPerThread = 10;
        this.requestBody = "";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getRequestsPerThread() {
        return requestsPerThread;
    }

    public void setRequestsPerThread(Integer requestsPerThread) {
        this.requestsPerThread = requestsPerThread;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }
}
