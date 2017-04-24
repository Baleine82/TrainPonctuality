package me.arbogast.trainponctuality.dbaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by excelsior on 08/01/17.
 * Default database handler
 */

class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TRAVEL_CREATE_TABLE = "CREATE TABLE " + TravelDAO.TABLE_NAME +
            " (" + TravelDAO.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + TravelDAO.COLUMN_DEPARTURE_DATE + " LONG NOT NULL, " +
            TravelDAO.COLUMN_ARRIVAL_DATE + " LONG, " + TravelDAO.COLUMN_LINE + " TEXT, " + TravelDAO.COLUMN_MISSION + " TEXT, " +
            TravelDAO.COLUMN_DEPARTURE_STATION + " TEXT NOT NULL, " + TravelDAO.COLUMN_ARRIVAL_STATION + " TEXT);";

    private static final String ROUTES_CREATE_TABLE = "CREATE TABLE " + RoutesDAO.TABLE_NAME +
            " (" + RoutesDAO.COLUMN_ID + " CHAR(12) PRIMARY KEY NOT NULL, " + RoutesDAO.COLUMN_AGENCY + " CHAR(6) NOT NULL, " +
            RoutesDAO.COLUMN_SHORT_NAME + " VARCHAR(20), " + RoutesDAO.COLUMN_LONG_NAME + " TEXT, " + RoutesDAO.COLUMN_TYPE + " INTEGER, " +
            RoutesDAO.COLUMN_COLOR + " CHAR(6), " + RoutesDAO.COLUMN_TEXT_COLOR + " CHAR(6));";

    private static final String TRIPS_CREATE_TABLE = "CREATE TABLE " + TripsDAO.TABLE_NAME +
            " (" + TripsDAO.COLUMN_ID + " VARCHAR(26) PRIMARY KEY NOT NULL, " + TripsDAO.COLUMN_ROUTE_ID + " CHAR(12) NOT NULL, " +
            TripsDAO.COLUMN_SERVICE_ID + " CHAR(4) NOT NULL, " + TripsDAO.COLUMN_TRIP_HEADSIGN + " VARCHAR(8) NOT NULL, " +
            TripsDAO.COLUMN_DIRECTION_ID + " INTEGER NOT NULL);";

    private static final String STOPS_CREATE_TABLE = "CREATE TABLE " + StopsDAO.TABLE_NAME + " (" + StopsDAO.COLUMN_ID + " VARCHAR(20) PRIMARY KEY NOT NULL, " +
            StopsDAO.COLUMN_NAME + " TEXT NOT NULL, " + StopsDAO.COLUMN_LATITUDE + " DOUBLE, " + StopsDAO.COLUMN_LONGITUDE + " DOUBLE, " +
            StopsDAO.COLUMN_LOCATION_TYPE + " INTEGER, " + StopsDAO.COLUMN_PARENT_STATION + " VARCHAR(20))";

    private static final String STOP_TIMES_CREATE_TABLE = "CREATE TABLE " + StopTimesDAO.TABLE_NAME +
            " (" + StopTimesDAO.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + StopTimesDAO.COLUMN_TRIP_ID + " VARCHAR(30) NOT NULL, " +
            StopTimesDAO.COLUMN_ARRIVAL_TIME + " LONG NOT NULL, " + StopTimesDAO.COLUMN_DEPARTURE_TIME + " LONG NOT NULL, " +
            StopTimesDAO.COLUMN_STOP_ID + " VARCHAR(20) NOT NULL, " + StopTimesDAO.COLUMN_SEQUENCE + " INTEGER NOT NULL, " +
            StopTimesDAO.COLUMN_PICKUP_TYPE + " INTEGER NOT NULL, " + StopTimesDAO.COLUMN_DROPOFF_TYPE + " INTEGER NOT NULL)";

    private static final String CALENDAR_CREATE_TABLE = "CREATE TABLE " + CalendarDAO.TABLE_NAME +
            " (" + CalendarDAO.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + CalendarDAO.COLUMN_MONDAY + " INTEGER NOT NULL, " +
            CalendarDAO.COLUMN_TUESDAY + " INTEGER NOT NULL, " + CalendarDAO.COLUMN_WEDNESDAY + " INTEGER NOT NULL, " +
            CalendarDAO.COLUMN_THURSDAY + " INTEGER NOT NULL, " + CalendarDAO.COLUMN_FRIDAY + " INTEGER NOT NULL, " +
            CalendarDAO.COLUMN_SATURDAY + " INTEGER NOT NULL, " + CalendarDAO.COLUMN_SUNDAY + " INTEGER NOT NULL, " +
            CalendarDAO.COLUMN_START_DATE + " LONG NOT NULL, " + CalendarDAO.COLUMN_END_DATE + " LONG NOT NULL)";

    DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRAVEL_CREATE_TABLE);
        db.execSQL(ROUTES_CREATE_TABLE);
        db.execSQL(TRIPS_CREATE_TABLE);
        db.execSQL(STOPS_CREATE_TABLE);
        db.execSQL(STOP_TIMES_CREATE_TABLE);
        db.execSQL(CALENDAR_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table " + TravelDAO.TABLE_NAME + " add column " + TravelDAO.COLUMN_TRIP_ID + " VARCHAR(26);");
    }

    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
