package me.arbogast.trainponctuality.gui;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by excelsior on 22/12/16.
 * Utility class
 */

public class Utils {
    private static final String TAG = "Utils";
    @SuppressLint("SimpleDateFormat")
    private static DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
    @SuppressLint("SimpleDateFormat")
    private static DateFormat timeFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);

    public static String dateToString(Date d)
    {
        return dateFormat.format(d);
    }

    public static String timeToString(Date d) {
        return timeFormat.format(d);
    }
}
