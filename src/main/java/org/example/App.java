package org.example;

import org.example.classes.MyLogger;
import org.example.classes.ThreadReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class App {
    private static final Path FILE_PATH;
    private static final Path OUT_PATH;

    static {
        FILE_PATH = Path.of("alice.txt");
        OUT_PATH = Path.of("text.txt");
        if (Files.exists(OUT_PATH)) {
            try {
                Files.delete(OUT_PATH);
            } catch (IOException e) {
                MyLogger.logMessage(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public static void main(String[] args) {
        final int begging = 0;
        final int countThreads = 10;
        final ExecutorService executorService = Executors.newCachedThreadPool();
        final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        System.out.println("System is working...");
        try {
            inputStream = new FileInputStream(FILE_PATH.toString());
            outputStream = new FileOutputStream(OUT_PATH.toString());
            final List<ThreadReader> readers = new ArrayList<>();
            for (int i = begging; i < countThreads; i++) {
                readers.add(new ThreadReader(inputStream, outputStream, readWriteLock));
            }
            try {
                executorService.invokeAll(readers);
            } catch (InterruptedException e) {
                MyLogger.logMessage(Arrays.toString(e.getStackTrace()));
            } finally {
                executorService.shutdown();
                MyLogger.logMessage("ExecutorService shutdown");
            }
            System.out.println("Program finished!");
            System.out.println("Good bye :)");
        } catch (FileNotFoundException e) {
            MyLogger.logMessage(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                Objects.requireNonNull(inputStream).close();
                Objects.requireNonNull(outputStream).close();
            } catch (IOException e) {
                MyLogger.logMessage(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
