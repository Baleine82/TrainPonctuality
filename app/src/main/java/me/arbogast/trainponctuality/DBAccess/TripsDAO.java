package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;

import me.arbogast.trainponctuality.Model.Routes;
import me.arbogast.trainponctuality.Model.Trips;

/**
 * Created by excelsior on 15/01/17.
 */

public class TripsDAO extends DAOImportBase<Trips> {
    private static final String TAG = "TripsDAO";

    private static final String TABLE_NAME = "trips";
    private static final String COLUMN_ROUTE_ID = "route_id";
    private static final String COLUMN_SERVICE_ID = "service_id";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TRIP_HEADSIGN = "trip_headsign";
    private static final String COLUMN_DIRECTION_ID = "direction_id";

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
}
