package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import me.arbogast.trainponctuality.Model.StopTimes;

/**
 * Created by excelsior on 15/01/17.
 */

public class StopTimesDAO extends DAOImportBase<StopTimes> {
    private static final String TAG = "TripsDAO";

    static final String TABLE_NAME = "stop_times";
    static final String COLUMN_ID = "stt_id";
    static final String COLUMN_TRIP_ID = "stt_trip_id";
    static final String COLUMN_ARRIVAL_TIME = "stt_arrival_time";
    static final String COLUMN_DEPARTURE_TIME = "stt_departure_time";
    static final String COLUMN_STOP_ID = "stt_stop_id";
    static final String COLUMN_SEQUENCE = "stt_sequence";
    static final String COLUMN_PICKUP_TYPE = "stt_pickup_type";
    static final String COLUMN_DROPOFF_TYPE = "stt_dropoff_type";

    public StopTimesDAO(Context pContext) {
        super(pContext);
    }

    @Override
    protected ContentValues createValues(StopTimes t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_ID, t.getId());
        value.put(COLUMN_TRIP_ID, t.getTripId());
        value.put(COLUMN_ARRIVAL_TIME, t.getArrivalTime().getTime());
        value.put(COLUMN_DEPARTURE_TIME, t.getDepartureTime().getTime());
        value.put(COLUMN_STOP_ID, t.getStopId());
        value.put(COLUMN_SEQUENCE, t.getSequence());
        value.put(COLUMN_PICKUP_TYPE, t.getPickupType());
        value.put(COLUMN_DROPOFF_TYPE, t.getDropOffType());

        return value;
    }

    @Override
    protected ContentValues createValues(String[] t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_TRIP_ID, t[0]);
        value.put(COLUMN_ARRIVAL_TIME, t[1]);
        value.put(COLUMN_DEPARTURE_TIME, t[2]);
        value.put(COLUMN_STOP_ID, t[3]);
        value.put(COLUMN_SEQUENCE, t[4]);
        value.put(COLUMN_PICKUP_TYPE, t[6]);
        value.put(COLUMN_DROPOFF_TYPE, t[7]);

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
    protected StopTimes getItem(Cursor c) {
        return null;
    }
}
