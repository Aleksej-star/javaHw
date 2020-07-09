package org.example.classes;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class MyLogger {
    private static final Calendar CALENDAR;
    private static final Path THREADS_LOG;

    static {
        THREADS_LOG = Path.of("log.txt");
        if (Files.exists(THREADS_LOG)) {
            try {
                Files.delete(THREADS_LOG);
            } catch (IOException e) {
                logMessage(Arrays.toString(e.getStackTrace()));
            }
        }
        CALENDAR = new GregorianCalendar();
    }

    public static void logMessage(final String message) {
        try (FileWriter writer = new FileWriter(THREADS_LOG.toString(), true)) {
            writer.write(message + " : " + CALENDAR.getTime());
            writer.append('\n');
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
