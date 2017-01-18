package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import me.arbogast.trainponctuality.Model.Stops;

/**
 * Created by excelsior on 15/01/17.
 */

public class StopsDAO extends DAOImportBase<Stops> {
    private static final String TAG = "TripsDAO";

    private static final String TABLE_NAME = "stops";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_LOCATION_TYPE = "location_type";
    private static final String COLUMN_PARENT_STATION = "parent_station";

    private static final String SELECT_ALL = "SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_LATITUDE + ", " +
            COLUMN_LONGITUDE + ", " + COLUMN_LOCATION_TYPE + ", " + COLUMN_PARENT_STATION;

    public StopsDAO(Context pContext) {
        super(pContext);
    }

    @Override
    protected ContentValues createValues(Stops t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_ID, t.getId());
        value.put(COLUMN_NAME, t.getName());
        value.put(COLUMN_LATITUDE, t.getLatitude());
        value.put(COLUMN_LONGITUDE, t.getLongitude());
        value.put(COLUMN_LOCATION_TYPE, t.getLocationType());
        value.put(COLUMN_PARENT_STATION, t.getParentStation());

        return value;
    }

    @Override
    protected ContentValues createValues(String[] t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_ID, t[0]);
        value.put(COLUMN_NAME, t[1]);
        value.put(COLUMN_LATITUDE, Double.parseDouble(t[3]));
        value.put(COLUMN_LONGITUDE, Double.parseDouble(t[4]));
        value.put(COLUMN_LOCATION_TYPE, t[7]);
        value.put(COLUMN_PARENT_STATION, t[8]);

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
        return SELECT_ALL;
    }

    protected Stops getItem(Cursor c) {
        return new Stops(c.getString(0), c.getString(1), c.getDouble(2), c.getDouble(3), c.getInt(4), c.getString(5));
    }

}
