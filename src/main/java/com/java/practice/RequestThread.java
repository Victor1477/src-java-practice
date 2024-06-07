package com.java.practice;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.util.Optional;

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

                String body = this.params.getRequestBody();

                Optional<Header> contentType = this.params.getHeaders().stream().filter(header -> header.name.equals("Content-Type")).findFirst();

                if (contentType.isPresent() && contentType.get().value.equals("application/json")) {
                    body = body.replaceAll("'", "\"");
                }

                HttpRequest request = builder.method(this.params.getRequestMethod(), HttpRequest.BodyPublishers.ofString(body)).build();
                HttpClient client = HttpClient.newBuilder().sslContext(this.getSSLContext()).build();
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

    public SSLContext getSSLContext() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}