package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;

import me.arbogast.trainponctuality.Model.CalendarTrip;
import me.arbogast.trainponctuality.Model.Trips;

/**
 * Created by excelsior on 15/01/17.
 */

public class CalendarDAO extends DAOImportBase<CalendarTrip> {
    private static final String TAG = "TripsDAO";

    private static final String TABLE_NAME = "calendar";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SERVICE_ID = "service_id";
    private static final String COLUMN_MONDAY = "monday";
    private static final String COLUMN_TUESDAY = "tuesday";
    private static final String COLUMN_WEDNESDAY = "wednesday";
    private static final String COLUMN_THURSDAY = "thursday";
    private static final String COLUMN_FRIDAY = "friday";
    private static final String COLUMN_SATURDAY = "saturday";
    private static final String COLUMN_SUNDAY = "sunday";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";

    public CalendarDAO(Context pContext) {
        super(pContext);
    }

    @Override
    protected ContentValues createValues(CalendarTrip t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_ID, t.getId());
        value.put(COLUMN_SERVICE_ID, t.getServiceId());
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
        value.put(COLUMN_SERVICE_ID, t[0]);
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
}
