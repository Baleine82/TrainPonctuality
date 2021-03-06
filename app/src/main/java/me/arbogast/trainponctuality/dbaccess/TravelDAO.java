package me.arbogast.trainponctuality.dbaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

import me.arbogast.trainponctuality.model.History;
import me.arbogast.trainponctuality.model.Travel;

/**
 * Created by excelsior on 08/01/17.
 * DAO for Travel
 */

public class TravelDAO extends DAOBase<Travel> {
    private static final String TAG = "TravelDAO";

    static final String TABLE_NAME = "travel";
    static final String COLUMN_ID = "tra_id";
    static final String COLUMN_DEPARTURE_DATE = "tra_departureDate";
    static final String COLUMN_DEPARTURE_STATION = "tra_departureStation";
    static final String COLUMN_ARRIVAL_DATE = "tra_arrivalDate";
    static final String COLUMN_ARRIVAL_STATION = "tra_arrivalStation";
    static final String COLUMN_LINE = "tra_line";
    static final String COLUMN_MISSION = "tra_mission";
    static final String COLUMN_TRIP_ID = "tra_trip_id";

    private static final String SELECT_ALL = COLUMN_ID + ", " + COLUMN_DEPARTURE_DATE + ", " + COLUMN_DEPARTURE_STATION + ", " +
            COLUMN_ARRIVAL_DATE + ", " + COLUMN_ARRIVAL_STATION + ", " + COLUMN_LINE + ", " + COLUMN_MISSION + ", " + COLUMN_TRIP_ID;

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
        value.put(COLUMN_TRIP_ID, t.getTripId());

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

    public History selectCurrentTravel() {
        openRead();

        try (Cursor c = mDb.rawQuery("SELECT " + SELECT_ALL + ", depStop." + StopsDAO.COLUMN_NAME + " AS departureStation FROM " + TABLE_NAME +
                " INNER JOIN " + StopsDAO.TABLE_NAME + " AS depStop ON (" + TABLE_NAME + "." + COLUMN_DEPARTURE_STATION + " = depStop." + StopsDAO.COLUMN_ID + ") " +
                " WHERE " + COLUMN_ARRIVAL_DATE + " IS NULL LIMIT 1", null)) {
            if (c.moveToFirst())
                return new History(getItem(c), c.getString(8), null);
            else
                return null;
        }
    }

    protected Travel getItem(Cursor c) {
        return new Travel(c.getLong(0), new Date(c.getLong(1)), c.getString(2), new Date(c.getLong(3)), c.getString(4), c.getString(5), c.getString(6), c.getString(7));
    }

    public ArrayList<History> selectHistory() {
        openRead();
        ArrayList<History> listT = new ArrayList<>();
        try (Cursor c = mDb.rawQuery("SELECT " + SELECT_ALL + ", depStop." + StopsDAO.COLUMN_NAME + " AS departureStation, " +
                " arrStop." + StopsDAO.COLUMN_NAME + " AS arrivalStation FROM " + TABLE_NAME +
                " INNER JOIN " + StopsDAO.TABLE_NAME + " AS depStop ON (" + TABLE_NAME + "." + COLUMN_DEPARTURE_STATION + " = depStop." + StopsDAO.COLUMN_ID + ") " +
                " LEFT JOIN " + StopsDAO.TABLE_NAME + " AS arrStop ON (" + TABLE_NAME + "." + COLUMN_ARRIVAL_STATION + " = arrStop." + StopsDAO.COLUMN_ID + ") " +
                " ORDER BY " + COLUMN_DEPARTURE_DATE + ";", null)) {

            while (c.moveToNext())
                listT.add(new History(getItem(c), c.getString(8), c.getString(9)));
        }

        return listT;
    }
}
