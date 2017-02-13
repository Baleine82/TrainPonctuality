package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.arbogast.trainponctuality.Model.History;
import me.arbogast.trainponctuality.Model.Travel;

/**
 * Created by excelsior on 08/01/17.
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

    private static final String SELECT_ALL = COLUMN_ID + ", " + COLUMN_DEPARTURE_DATE + ", " + COLUMN_DEPARTURE_STATION + ", " +
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

        try (Cursor c = mDb.rawQuery("SELECT " + SELECT_ALL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ARRIVAL_DATE + " IS NULL", null)) {
            if (c.moveToFirst())
                return getItem(c);
            else
                return null;
        }
    }

    protected Travel getItem(Cursor c) {
        return new Travel(c.getLong(0), new Date(c.getLong(1)), c.getString(2), new Date(c.getLong(3)), c.getString(4), c.getString(5), c.getString(6));
    }

    public ArrayList selectHistory() {
        openRead();
        ArrayList listT = new ArrayList();
        try(Cursor c = mDb.rawQuery("SELECT " + COLUMN_LINE + ", " + COLUMN_MISSION + ", " + COLUMN_DEPARTURE_DATE + " as DateTravel, " +
                COLUMN_DEPARTURE_DATE + ", depStop." + StopsDAO.COLUMN_NAME + " AS departureStation, " + COLUMN_ARRIVAL_DATE + ", " +
                " arrStop." + StopsDAO.COLUMN_NAME + " AS arrivalStation FROM " + TABLE_NAME +
                " INNER JOIN " + StopsDAO.TABLE_NAME + " AS depStop ON (" + TABLE_NAME + "." + COLUMN_DEPARTURE_STATION + " = depStop." + StopsDAO.COLUMN_ID + ") " +
                " LEFT JOIN " + StopsDAO.TABLE_NAME + " AS arrStop ON (" + TABLE_NAME + "." + COLUMN_ARRIVAL_STATION + " = arrStop." + StopsDAO.COLUMN_ID + ") " +
                " ORDER BY " + COLUMN_DEPARTURE_DATE + ";", null)) {

            while (c.moveToNext())
                listT.add(new History(c.getString(0), c.getString(1), new Date(c.getLong(2)), new Date(c.getLong(3)), c.getString(4), new Date(c.getLong(5)), c.getString(6)));
        }

        return listT;
    }
}
