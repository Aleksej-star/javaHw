package org.example.classes;


import java.io.*;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadReader implements Callable<Void> {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final ReentrantReadWriteLock lock;

    public ThreadReader(final InputStream inputStream, final OutputStream outputStream, final ReentrantReadWriteLock readWriteLock) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.lock = readWriteLock;
    }

    @Override
    public Void call() {
        MyLogger.logMessage("Thread " + Thread.currentThread().getName() + " status is STARTED");
        int count;
        byte[] buffer = new byte[1024];
        try {
            do {
                this.lock.writeLock().lock();
                try {
                    count = this.inputStream.read(buffer);
                    MyLogger.logMessage("Thread " + Thread.currentThread().getName() + " status is WORKING");
                } finally {
                    this.lock.writeLock().unlock();
                }
                if (count > 0) {
                    this.outputStream.write(buffer, 0, count);
                }
                try {
                    MyLogger.logMessage("Thread " + Thread.currentThread().getName() + " status is SLEEP");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    MyLogger.logMessage(Arrays.toString(e.getStackTrace()));
                }
            } while (count > 0);
        } catch (IOException e) {
            MyLogger.logMessage(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
