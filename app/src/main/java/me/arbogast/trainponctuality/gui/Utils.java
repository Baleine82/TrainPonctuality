package me.arbogast.trainponctuality.gui;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by excelsior on 22/12/16.
 * Utility class
 */

public class Utils {
    static final int RESULT_GET_DEPARTURE_STATION = 0;
    static final int RESULT_GET_DEPARTURE_DATE = 1;
    static final int RESULT_GET_DEPARTURE_TIME = 2;
    static final int RESULT_GET_ARRIVAL_STATION = 3;
    static final int RESULT_GET_ARRIVAL_DATE = 4;
    static final int RESULT_GET_ARRIVAL_TIME = 5;
    static final int RESULT_EDIT_TRAVEL = 6;
    static final int RESULT_FIND_THEORIC_TRAVEL = 7;


    private static final String TAG = "Utils";
    @SuppressLint("SimpleDateFormat")
    private static DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
    @SuppressLint("SimpleDateFormat")
    private static DateFormat timeFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
    @SuppressLint("SimpleDateFormat")
    private static DateFormat dbTimeFormat = new SimpleDateFormat("HH:mm:ss");

    public static String dateToString(Date d)
    {
        return dateFormat.format(d);
    }

    public static String timeToString(Date d) {
        return timeFormat.format(d);
    }

    public static String dbTimeToString(Date d){
        return dbTimeFormat.format(d);
    }

    static Date millisToDate(Long millis){
        Calendar ret = GregorianCalendar.getInstance();
        ret.setTimeInMillis(millis);
        return ret.getTime();
    }

    public static long getEpochFromDb(long aLong) {
        return aLong * 1000 - 946684800000L;
    }
}
