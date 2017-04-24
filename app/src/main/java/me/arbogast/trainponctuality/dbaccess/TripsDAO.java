package me.arbogast.trainponctuality.dbaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.arbogast.trainponctuality.gui.Utils;
import me.arbogast.trainponctuality.model.History;
import me.arbogast.trainponctuality.model.Stops;
import me.arbogast.trainponctuality.model.Travel;
import me.arbogast.trainponctuality.model.Trips;

/**
 * Created by excelsior on 15/01/17.
 * This is a trip done on the railway
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

    public List<String> getTripsForLine(String line) {
        openRead();
        List<String> listT = new ArrayList<>();
        String query = "SELECT " + COLUMN_TRIP_HEADSIGN + " FROM " + RoutesDAO.TABLE_NAME +
                " INNER JOIN " + TABLE_NAME + " ON (" + RoutesDAO.TABLE_NAME + "." + RoutesDAO.COLUMN_ID + " = " + TABLE_NAME + "." + COLUMN_ROUTE_ID + ")" +
                " WHERE " + RoutesDAO.TABLE_NAME + "." + RoutesDAO.COLUMN_SHORT_NAME + " = ? " +
                " AND " + TABLE_NAME + "." + COLUMN_TRIP_HEADSIGN + " REGEXP '[A-Z]{4}'" +
                " GROUP BY " + COLUMN_TRIP_HEADSIGN +
                " ORDER BY " + COLUMN_TRIP_HEADSIGN + ";";
        try (Cursor c = mDb.rawQuery(query, new String[]{line})) {

            while (c.moveToNext())
                listT.add(c.getString(0));
        }

        return listT;
    }

    @SuppressWarnings("deprecation")
    public List<History> findMatchingTrips(String line, String missionCode, Stops departurePoint, Date departureDate) {

        String dayColumn = CalendarDAO.getDayColumnForDate(departureDate);
        openRead();
        List<History> listT = new ArrayList<>();
        String query = "SELECT " + StopTimesDAO.TABLE_NAME + "." + StopTimesDAO.COLUMN_ID + ", strftime('%s'," + StopTimesDAO.TABLE_NAME + "." + StopTimesDAO.COLUMN_DEPARTURE_TIME + ", 'utc'), " +
                TABLE_NAME + "." + COLUMN_ID +
                " FROM " + RoutesDAO.TABLE_NAME +
                " INNER JOIN " + TABLE_NAME + " ON (" + RoutesDAO.TABLE_NAME + "." + RoutesDAO.COLUMN_ID + " = " + TABLE_NAME + "." + COLUMN_ROUTE_ID + ") " +
                " INNER JOIN " + CalendarDAO.TABLE_NAME + " ON (" + TABLE_NAME + "." + COLUMN_SERVICE_ID + " = " + CalendarDAO.TABLE_NAME + "." + CalendarDAO.COLUMN_ID + ") " +
                " INNER JOIN " + StopTimesDAO.TABLE_NAME + " ON (" + TABLE_NAME + "." + COLUMN_ID + " = " + StopTimesDAO.TABLE_NAME + "." + StopTimesDAO.COLUMN_TRIP_ID + ") " +
                " INNER JOIN " + StopsDAO.TABLE_NAME + " ON (" + StopTimesDAO.TABLE_NAME + "." + StopTimesDAO.COLUMN_STOP_ID + " = " + StopsDAO.TABLE_NAME + "." + StopsDAO.COLUMN_ID + ") " +
                " WHERE " + RoutesDAO.TABLE_NAME + "." + RoutesDAO.COLUMN_SHORT_NAME + " = ? AND " + TABLE_NAME + "." + COLUMN_TRIP_HEADSIGN + " = ? " +
                " AND " + CalendarDAO.TABLE_NAME + "." + dayColumn + " = 1 AND ? BETWEEN " + CalendarDAO.TABLE_NAME + "." + CalendarDAO.COLUMN_START_DATE + " and " + CalendarDAO.TABLE_NAME + "." + CalendarDAO.COLUMN_END_DATE +
                " AND " + StopsDAO.TABLE_NAME + "." + StopsDAO.COLUMN_ID + " = ?" +
                " ORDER BY ABS(CAST((JULIANDAY(" + StopTimesDAO.TABLE_NAME + "." + StopTimesDAO.COLUMN_DEPARTURE_TIME + ") - JULIANDAY(?)) * 24 * 60 AS INTEGER)) ASC;";
        // select * from routes
        // inner join trips on routes.rte_id = trips.tri_route_id
        // inner join calendar on trips.tri_service_id = calendar.cal_service_id
        // inner join stop_times on trips.tri_id = stop_times.stt_trip_id inner
        // join stops on stop_times.stt_stop_id = stops.sto_id
        // where routes.rte_route_short_name = 'A' AND trips.tri_trip_headsign = 'TPUR' and stops.stop_id = 'StopPoint:DUA8738641';

        try (Cursor c = mDb.rawQuery(query, new String[]{line, missionCode, Utils.dbDateToString(departureDate), departurePoint.getId(), Utils.dbTimeToString(departureDate)})) {

            Calendar depTheory = GregorianCalendar.getInstance();
            depTheory.setTimeInMillis(departureDate.getTime());

            while (c.moveToNext()) {
                Date travel = new Date(Utils.getEpochFromDb(c.getLong(1)));
                depTheory.set(Calendar.HOUR_OF_DAY, travel.getHours());
                depTheory.set(Calendar.MINUTE, travel.getMinutes());

                listT.add(new History(new Travel(depTheory.getTime(), line, missionCode, departurePoint.getId(), c.getString(0)), departurePoint.getName(), null));
            }
        }

        return listT;
    }
}
