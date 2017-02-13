package me.arbogast.trainponctuality.dbaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import me.arbogast.trainponctuality.model.Stops;

/**
 * Created by excelsior on 15/01/17.
 * DAO for Stops
 */

public class StopsDAO extends DAOImportBase<Stops> {
    private static final String TAG = "TripsDAO";

    static final String TABLE_NAME = "stops";
    static final String COLUMN_ID = "sto_id";
    static final String COLUMN_NAME = "sto_name";
    static final String COLUMN_LATITUDE = "sto_latitude";
    static final String COLUMN_LONGITUDE = "sto_longitude";
    static final String COLUMN_LOCATION_TYPE = "sto_location_type";
    static final String COLUMN_PARENT_STATION = "sto_parent_station";

    private static final String SELECT_ALL = COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_LATITUDE + ", " +
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

    public List<Stops> getStopsForLine(String line) {
        openRead();
        List<Stops> listT = new ArrayList<>();
        String query = "SELECT " + SELECT_ALL + " FROM " + RoutesDAO.TABLE_NAME +
                " INNER JOIN " + TripsDAO.TABLE_NAME + " ON (" + RoutesDAO.TABLE_NAME + "." + RoutesDAO.COLUMN_ID + " = " + TripsDAO.TABLE_NAME + "." + TripsDAO.COLUMN_ROUTE_ID + ")" +
                " INNER JOIN " + StopTimesDAO.TABLE_NAME + " ON (" + TripsDAO.TABLE_NAME + "." + TripsDAO.COLUMN_ID + " = " + StopTimesDAO.TABLE_NAME + "." + StopTimesDAO.COLUMN_TRIP_ID + ")" +
                " LEFT JOIN " + StopsDAO.TABLE_NAME + " ON (" + StopTimesDAO.TABLE_NAME + "." + StopTimesDAO.COLUMN_STOP_ID + " = " + StopsDAO.TABLE_NAME + "." + StopsDAO.COLUMN_ID + ")" +
                " WHERE " + RoutesDAO.TABLE_NAME + "." + RoutesDAO.COLUMN_SHORT_NAME + " = ? " +
                " GROUP BY " + StopsDAO.COLUMN_ID +
                " ORDER BY " + StopsDAO.COLUMN_NAME + ";";
        try (Cursor c = mDb.rawQuery(query, new String[]{line})) {

            while (c.moveToNext())
                listT.add(getItem(c));
        }

        return listT;
    }
}
