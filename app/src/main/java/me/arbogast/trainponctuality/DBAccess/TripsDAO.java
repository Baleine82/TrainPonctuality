package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import me.arbogast.trainponctuality.Model.Trips;

/**
 * Created by excelsior on 15/01/17.
 */

public class TripsDAO extends DAOImportBase<Trips> {
    private static final String TAG = "TripsDAO";

    static final String COLUMN_ID = "tri_id";
    static final String TABLE_NAME = "trips";
    static final String COLUMN_ROUTE_ID = "tri_route_id";
    static final String COLUMN_SERVICE_ID = "tri_service_id";
    static final String COLUMN_TRIP_HEADSIGN = "tri_trip_headsign";
    static final String COLUMN_DIRECTION_ID = "tri_direction_id";

    private static final String SELECT_ALL = COLUMN_ID + ", " + COLUMN_ROUTE_ID + ", " + COLUMN_SERVICE_ID + ", " +
            COLUMN_TRIP_HEADSIGN + ", " + COLUMN_DIRECTION_ID;

    public TripsDAO(Context pContext) {
        super(pContext);
    }

    @Override
    protected ContentValues createValues(Trips t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_ID, t.getId());
        value.put(COLUMN_ROUTE_ID, t.getRouteId());
        value.put(COLUMN_SERVICE_ID, t.getServiceId());
        value.put(COLUMN_TRIP_HEADSIGN, t.getTripHeadsign());
        value.put(COLUMN_DIRECTION_ID, t.getDirectionId());

        return value;
    }

    @Override
    protected ContentValues createValues(String[] t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_ROUTE_ID, t[0]);
        value.put(COLUMN_SERVICE_ID, t[1]);
        value.put(COLUMN_ID, t[2]);
        value.put(COLUMN_TRIP_HEADSIGN, t[3]);
        value.put(COLUMN_DIRECTION_ID, t[4]);

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
    protected Trips getItem(Cursor c) {
        return null;
    }

    public List<String> getTripsForLine(String line){
        openRead();
        List<String> listT = new ArrayList<>();
        String query = "SELECT " + COLUMN_TRIP_HEADSIGN + " FROM " + RoutesDAO.TABLE_NAME +
                " INNER JOIN " + TABLE_NAME + " ON (" + RoutesDAO.TABLE_NAME + "." + RoutesDAO.COLUMN_ID + " = " + TABLE_NAME + "." + COLUMN_ROUTE_ID + ")" +
                " WHERE " + RoutesDAO.TABLE_NAME + "." + RoutesDAO.COLUMN_SHORT_NAME + " = ? " +
                " GROUP BY " + COLUMN_TRIP_HEADSIGN +
                " ORDER BY " + COLUMN_TRIP_HEADSIGN + ";";
        try(Cursor c = mDb.rawQuery(query, new String[]{line})) {

            while (c.moveToNext())
                listT.add(c.getString(0));
        }

        return listT;
    }
}
