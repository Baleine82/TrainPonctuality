package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import me.arbogast.trainponctuality.Model.Travel;

/**
 * Created by excelsior on 08/01/17.
 */

public class TravelDAO extends DAOBase {
    private static final String TAG = "TravelDAO";

    public static final String TABLE_NAME = "travel";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DEPARTURE_DATE = "departureDate";
    public static final String COLUMN_DEPARTURE_STATION = "departureStation";
    public static final String COLUMN_ARRIVAL_DATE = "arrivalDate";
    public static final String COLUMN_ARRIVAL_STATION = "arrivalStation";
    public static final String COLUMN_LINE = "line";
    public static final String COLUMN_MISSION = "mission";

    private static final String SELECT_ALL = "SELECT " + COLUMN_ID + ", " + COLUMN_DEPARTURE_DATE + ", " + COLUMN_DEPARTURE_STATION + ", " +
            COLUMN_ARRIVAL_DATE + ", " + COLUMN_ARRIVAL_STATION + ", " + COLUMN_LINE + ", " + COLUMN_MISSION;

    public TravelDAO(Context pContext) {
        super(pContext);
    }

    public void insertDeparture(Travel t) {
        openWrite();
        mDb.insert(TABLE_NAME, null, createValues(t));
    }

    public void update(Travel t) {
        if (t.getId() <= 0) {
            Log.w(TAG, "update : Cannot update object with id = 0");
            return;
        }

        openWrite();
        mDb.update(TABLE_NAME, createValues(t), COLUMN_ID + " = ?", new String[]{String.valueOf(t.getId())});
    }

    private ContentValues createValues(Travel t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_DEPARTURE_DATE, wrapDateDB(t.getDepartureDate()));
        value.put(COLUMN_DEPARTURE_STATION, t.getDepartureStation());
        if (t.getArrivalStation() != null) {
            value.put(COLUMN_ARRIVAL_DATE, wrapDateDB(t.getArrivalDate()));
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
                return getTravel(c);
            else
                return null;
        } finally {
            c.close();
        }
    }

    private Travel getTravel(Cursor c) {

        return new Travel(c.getLong(0), getDateFromDB(c.getString(1)), c.getString(2), getDateFromDB(c.getString(3)), c.getString(4), c.getString(5), c.getString(6));
    }

    public List<Travel> selectAllTravels() {
        openRead();
        List<Travel> listT = new ArrayList<>();
        Cursor c = mDb.rawQuery(SELECT_ALL + " from " + TABLE_NAME, null);

        while (c.moveToNext())
            listT.add(getTravel(c));

        c.close();

        return listT;
    }
}
