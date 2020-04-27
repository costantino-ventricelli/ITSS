package it.uniba.ventricellisardone.itss.log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {

    private static PrintWriter logFile;

    public static void e(String TAG, String message, Exception ex) {
        try {
            logFile = new PrintWriter(new FileOutputStream("log.txt", true));
            Calendar calendar = Calendar.getInstance();
            logFile.println(TAG + " [EXCEPTION LOG] " + SimpleDateFormat.getInstance().format(calendar.getTime()));
            logFile.println("\t" + message);
            logFile.println("\t" + ex.getMessage());
            logFile.println();
            logFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void i(String TAG, String message) {
        try {
            logFile = new PrintWriter(new FileOutputStream("log.txt", true));
            Calendar calendar = Calendar.getInstance();
            logFile.println(TAG + " [INFO LOG] " + SimpleDateFormat.getInstance().format(calendar.getTime()));
            logFile.println("\t" + message);
            logFile.println();
            logFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
