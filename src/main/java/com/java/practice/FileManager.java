package com.java.practice;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager {

    public Params getParams() {
        try {
            return new ObjectMapper().readValue(new FileReader("params.json"), Params.class);
        } catch (IOException e) {
            System.out.println("params.json config file not found.");
            throw new RuntimeException("params.json config file not found.");
        }
    }

    public boolean generateConfigFile() {
        try {
            PrintWriter pw = new PrintWriter(new File("params.json"));
            pw.println(new ObjectMapper().writeValueAsString(new Params()));
            pw.flush();
            pw.close();
            return true;
        } catch (Exception e) {
            System.out.println("Fail to create config file.");
            throw new RuntimeException("Fail to create config file.");
        }
    }

    public void generateLogsFile(String sucesses, String fails) {
        try {
            PrintWriter pw = new PrintWriter(new File("sucesses.txt"));
            pw.println(sucesses);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            System.out.println("Fail to create sucesses log file.");
        }
        try {
            PrintWriter pw = new PrintWriter(new File("fails.txt"));
            pw.println(fails);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            System.out.println("Fail to create fails log file.");
        }
    }
}
