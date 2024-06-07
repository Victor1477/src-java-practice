package com.java.practice;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Integer serverPort = 8080;

    public static void main(String[] args) throws Exception {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
            server.createContext("/", context -> {
                handleRequests(context);
            });
            server.start();
            Logger.log("Server Listening on port: " + serverPort);
            Logger.log("Call http://localhost:" + serverPort + "/generateConfigFile to generate params config file.");
            Logger.log("Call http://localhost:" + serverPort + "/generateLogsFiles to generate log files.");
            Logger.log("Call http://localhost:" + serverPort + "/start to start the requests.");
        } catch (IOException e) {
            throw new RuntimeException("Server Failed on Initialization");
        }
    }

    public static void handleRequests(HttpExchange context) throws IOException {
        switch (context.getRequestURI().toString()) {
            case "/generateConfigFile": {
                try {
                    new FileManager().generateConfigFile();
                } catch (Exception e) {
                    String msg = e.getMessage();
                    context.sendResponseHeaders(500, msg.getBytes().length);
                    context.getResponseBody().write(msg.getBytes());
                    context.close();
                }
                break;
            }
            case "/start": {
                Logger.restartLogger();
                try {
                    Params params = new FileManager().getParams();
                    ExecutorService executorService = Executors.newFixedThreadPool(params.getThreads());

                    for (int i = 1; i <= params.getThreads(); i++) {
                        executorService.submit(new RequestThread("Thread " + i + ": ", params));
                    }
                } catch (Exception e) {
                    String msg = e.getMessage();
                    context.sendResponseHeaders(500, msg.getBytes().length);
                    context.getResponseBody().write(msg.getBytes());
                    context.close();
                    return;
                }
                break;
            }
            case "/generateLogsFiles": {
                Logger.generateLogs();
            }
        }
        context.sendResponseHeaders(200, 0);
        context.close();
    }
}