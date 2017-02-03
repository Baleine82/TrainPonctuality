package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import me.arbogast.trainponctuality.Model.CalendarTrip;

/**
 * Created by excelsior on 15/01/17.
 */

public class CalendarDAO extends DAOImportBase<CalendarTrip> {
    private static final String TAG = "TripsDAO";

    static final String TABLE_NAME = "calendar";
    static final String COLUMN_ID = "cal_service_id";
    static final String COLUMN_MONDAY = "cal_monday";
    static final String COLUMN_TUESDAY = "cal_tuesday";
    static final String COLUMN_WEDNESDAY = "cal_wednesday";
    static final String COLUMN_THURSDAY = "cal_thursday";
    static final String COLUMN_FRIDAY = "cal_friday";
    static final String COLUMN_SATURDAY = "cal_saturday";
    static final String COLUMN_SUNDAY = "cal_sunday";
    static final String COLUMN_START_DATE = "cal_start_date";
    static final String COLUMN_END_DATE = "cal_end_date";

    public CalendarDAO(Context pContext) {
        super(pContext);
    }

    @Override
    protected ContentValues createValues(CalendarTrip t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_ID, t.getId());
        value.put(COLUMN_MONDAY, t.getMonday());
        value.put(COLUMN_TUESDAY, t.getTuesday());
        value.put(COLUMN_WEDNESDAY, t.getWednesday());
        value.put(COLUMN_THURSDAY, t.getThursday());
        value.put(COLUMN_FRIDAY, t.getFriday());
        value.put(COLUMN_SATURDAY, t.getSaturday());
        value.put(COLUMN_SUNDAY, t.getSunday());
        value.put(COLUMN_START_DATE, t.getStartDate().getTime());
        value.put(COLUMN_END_DATE, t.getEndDate().getTime());

        return value;
    }

    @Override
    protected ContentValues createValues(String[] t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_ID, t[0]);
        value.put(COLUMN_MONDAY, t[1]);
        value.put(COLUMN_TUESDAY, t[2]);
        value.put(COLUMN_WEDNESDAY, t[3]);
        value.put(COLUMN_THURSDAY, t[4]);
        value.put(COLUMN_FRIDAY, t[5]);
        value.put(COLUMN_SATURDAY, t[6]);
        value.put(COLUMN_SUNDAY, t[7]);
        value.put(COLUMN_START_DATE, t[8]);
        value.put(COLUMN_END_DATE, t[9]);

        return value;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getColumnId() {
        return COLUMN_ID;
    }

    @Override
    protected String getSelectAllCols() {
        return null;
    }

    @Override
    protected CalendarTrip getItem(Cursor c) {
        return null;
    }
}