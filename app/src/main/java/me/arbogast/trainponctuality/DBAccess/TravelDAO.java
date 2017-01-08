package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;

import me.arbogast.trainponctuality.Model.Travel;

/**
 * Created by excelsior on 08/01/17.
 */

public class TravelDAO extends DAOBase {
    public static final String TABLE_NAME = "travel";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DEPARTURE_DATE = "departureDate";
    public static final String COLUMN_DEPARTURE_STATION = "departureStation";
    public static final String COLUMN_ARRIVAL_DATE = "arrivalDate";
    public static final String COLUMN_ARRIVAL_STATION = "arrivalStation";
    public static final String COLUMN_LINE = "line";
    public static final String COLUMN_MISSION = "mission";

    public TravelDAO(Context pContext) {
        super(pContext);
    }

    public void insert (Travel t)
    {
        ContentValues value = new ContentValues();
        value.put(COLUMN_DEPARTURE_DATE, wrapDateDB(t.getDepartureDate()));
        value.put(COLUMN_DEPARTURE_STATION, t.getDepartureStation());
        value.put(COLUMN_ARRIVAL_DATE, wrapDateDB(t.getArrivalDate()));
        value.put(COLUMN_ARRIVAL_STATION, t.getArrivalStation());
        value.put(COLUMN_LINE, t.getLine());
        value.put(COLUMN_MISSION, t.getMissionCode());
        mDb.insert(TABLE_NAME, null, value);
    }

    public void update (Travel t)
    {
        ContentValues value = new ContentValues();
        value.put(COLUMN_DEPARTURE_DATE, wrapDateDB(t.getDepartureDate()));
        value.put(COLUMN_DEPARTURE_STATION, t.getDepartureStation());
        value.put(COLUMN_ARRIVAL_DATE, wrapDateDB(t.getArrivalDate()));
        value.put(COLUMN_ARRIVAL_STATION, t.getArrivalStation());
        value.put(COLUMN_LINE, t.getLine());
        value.put(COLUMN_MISSION, t.getMissionCode());
        mDb.update(TABLE_NAME, value, COLUMN_ID  + " = ?", new String[] {String.valueOf(t.getId())});
    }

    public void delete(long id) {
        mDb.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] {String.valueOf(id)});
    }
    public void delete(Travel t)
    {
        mDb.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] {String.valueOf(t.getId())});
    }

}
