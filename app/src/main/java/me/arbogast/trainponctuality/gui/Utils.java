package me.arbogast.trainponctuality.gui;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by excelsior on 22/12/16.
 * Utility class
 */

public class Utils {
    private static final String TAG = "Utils";
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    static Date now() {
        return GregorianCalendar.getInstance().getTime();
    }

    public static String dateToString(Date d)
    {
        return dateFormat.format(d);
    }

    public static String timeToString(Date d) {
        return timeFormat.format(d);
    }
}
