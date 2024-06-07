package com.java.practice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger {

    static List<String> sucesses = Collections.synchronizedList(new ArrayList<>());
    static List<String> fails = Collections.synchronizedList(new ArrayList<>());

    static AtomicInteger requests = new AtomicInteger(0);

    static void restartLogger() {
        Logger.requests.set(0);
        sucesses = Collections.synchronizedList(new ArrayList<>());
        fails = Collections.synchronizedList(new ArrayList<>());
    }

    static void generateLogs() {
        StringBuilder sucessesStr = new StringBuilder();
        StringBuilder failsStr = new StringBuilder();

        sucesses.forEach((sucess) -> {
            sucessesStr.append(sucess + "\n\n\n\n\n");
        });

        fails.forEach((fail) -> {
            failsStr.append(fail + "\n\n\n\n\n");
        });

        new FileManager().generateLogsFile(sucessesStr.toString(), failsStr.toString());
    }

    static void log(String msg) {
        System.out.println(msg);
    }

    static void addSucess(String sucess) {
        Logger.sucesses.add(sucess);
    }

    static void addFail(String fail) {
        Logger.fails.add(fail);
    }
}