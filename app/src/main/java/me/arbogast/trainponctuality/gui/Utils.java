package me.arbogast.trainponctuality.gui;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.EditText;

import java.text.ParseException;
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
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static String getDate() {
        return dateFormat.format(GregorianCalendar.getInstance().getTime());
    }

    static String getTime() {
        return timeFormat.format(GregorianCalendar.getInstance().getTime());
    }

    static Date parseDate(String date, String time) {
        try {
            return dateTimeFormat.parse(date + " " + time);
        } catch (ParseException e) {
            Log.e(TAG, "parseDate: " + date, e);
            e.printStackTrace();
            return GregorianCalendar.getInstance().getTime();
        }
    }

    static String getText(EditText e)
    {
        return e.getText().toString();
    }

    public static String dateToString(Date d)
    {
        return dateFormat.format(d);
    }

    public static String timeToString(Date d) {
        return timeFormat.format(d);
    }
}
