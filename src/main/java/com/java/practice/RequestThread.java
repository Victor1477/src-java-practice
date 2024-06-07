package com.java.practice;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestThread implements Runnable {

    public String name;
    public Params params;

    public RequestThread(String name, Params params) {
        this.name = name;
        this.params = params;
    }

    @Override
    public void run() {
        HttpResponse response;
        for (int i = 1; i <= params.getRequestsPerThread(); i++) {
            Logger.requests.set(Logger.requests.get() + 1);
            try {
                HttpRequest.Builder builder = HttpRequest.newBuilder().uri(new URI(this.params.getUrl()));
                params.getHeaders().forEach(header -> {
                    builder.header(header.getName(), header.getValue());
                });
                HttpRequest request = builder.method(this.params.getRequestMethod(), HttpRequest.BodyPublishers.ofString(this.params.getRequestBody())).build();
                HttpClient client = HttpClient.newBuilder().build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() >= 400) {
                    throw new RuntimeException((String) response.body());
                }

                synchronized (Logger.sucesses) {
                    Logger.addSucess((String) response.body());
                }
                Logger.log(this.name + "Request " + Logger.requests.get() + " Sucess.");
            } catch (Exception e) {
                Logger.log(this.name + "Request " + Logger.requests.get() + " Fail.");
                synchronized (Logger.fails) {
                    Logger.addFail(e.getMessage());
                }
            }
        }
        Logger.log(this.name + "Finished.");
    }
}