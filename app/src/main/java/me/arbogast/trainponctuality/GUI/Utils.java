package me.arbogast.trainponctuality.GUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by excelsior on 22/12/16.
 */

public class Utils {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static String getDate()
    {
        return dateFormat.format(GregorianCalendar.getInstance().getTime());
    }

    public static String getTime()
    {
        return timeFormat.format(GregorianCalendar.getInstance().getTime());
    }

    public static Date parseDate(String date, String time) {
        try {
            return dateTimeFormat.parse(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
            return GregorianCalendar.getInstance().getTime();
        }
    }
}
