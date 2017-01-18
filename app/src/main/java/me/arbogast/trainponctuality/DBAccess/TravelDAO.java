package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import me.arbogast.trainponctuality.Model.Travel;

/**
 * Created by excelsior on 08/01/17.
 */

public class TravelDAO extends DAOBase<Travel> {
    private static final String TAG = "TravelDAO";

    private static final String TABLE_NAME = "travel";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DEPARTURE_DATE = "departureDate";
    private static final String COLUMN_DEPARTURE_STATION = "departureStation";
    private static final String COLUMN_ARRIVAL_DATE = "arrivalDate";
    private static final String COLUMN_ARRIVAL_STATION = "arrivalStation";
    private static final String COLUMN_LINE = "line";
    private static final String COLUMN_MISSION = "mission";

    private static final String SELECT_ALL = "SELECT " + COLUMN_ID + ", " + COLUMN_DEPARTURE_DATE + ", " + COLUMN_DEPARTURE_STATION + ", " +
            COLUMN_ARRIVAL_DATE + ", " + COLUMN_ARRIVAL_STATION + ", " + COLUMN_LINE + ", " + COLUMN_MISSION;

    public TravelDAO(Context pContext) {
        super(pContext);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getColumnId() {
        return COLUMN_ID;
    }

    protected String getSelectAllCols() {
        return SELECT_ALL;
    }

    protected ContentValues createValues(Travel t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_DEPARTURE_DATE, t.getDepartureDate().getTime());
        value.put(COLUMN_DEPARTURE_STATION, t.getDepartureStation());
        if (t.getArrivalStation() != null) {
            value.put(COLUMN_ARRIVAL_DATE, t.getArrivalDate().getTime());
            value.put(COLUMN_ARRIVAL_STATION, t.getArrivalStation());
        }
        value.put(COLUMN_LINE, t.getLine());
        value.put(COLUMN_MISSION, t.getMissionCode());

        return value;
    }

    public void delete(long id) {
        openWrite();
        mDb.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void delete(Travel t) {
        openWrite();
        mDb.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(t.getId())});
    }

    public Travel selectCurrentTravel() {
        openRead();

        Cursor c = mDb.rawQuery(SELECT_ALL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ARRIVAL_DATE + " IS NULL", null);

        try {
            if (c.moveToFirst())
                return getItem(c);
            else
                return null;
        } finally {
            c.close();
        }
    }

    protected Travel getItem(Cursor c) {
        return new Travel(c.getLong(0), new Date(c.getLong(1)), c.getString(2), new Date(c.getLong(3)), c.getString(4), c.getString(5), c.getString(6));
    }
}
