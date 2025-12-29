package smartattendance.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Utility class for logging application errors into a local file.
 * Keeps all runtime exceptions recorded for debugging.
 */
public class ErrorLogUtil {

    private static final String LOG_FILE = "error_log.txt";
    public static void logError(String source, Exception e) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println("-----------------------------------------------------");
            pw.println("Time: " + LocalDateTime.now());
            pw.println("Source: " + source);
            pw.println("Error Message: " + e.getMessage());
            pw.println("Stack Trace:");
            e.printStackTrace(pw);
            pw.println("-----------------------------------------------------\n");

        } catch (IOException ioException) {
            System.err.println("Failed to write error log: " + ioException.getMessage());
        }
    }
}
