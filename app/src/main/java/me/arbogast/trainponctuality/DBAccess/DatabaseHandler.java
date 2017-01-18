package me.arbogast.trainponctuality.DBAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by excelsior on 08/01/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TRAVEL_CREATE_TABLE = "CREATE TABLE travel (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "departureDate LONG NOT NULL, arrivalDate LONG, line TEXT, mission TEXT, departureStation TEXT NOT NULL, arrivalStation TEXT);";
    private static final String ROUTES_CREATE_TABLE = "CREATE TABLE routes (route_id CHAR(12) PRIMARY KEY NOT NULL, " +
            "agency_id CHAR(6) NOT NULL, route_short_name VARCHAR(20), route_long_name TEXT, route_type INTEGER, " +
            "route_color CHAR(6), route_text_color CHAR(6));";
    private static final String TRIPS_CREATE_TABLE = "CREATE TABLE trips (id VARCHAR(26) PRIMARY KEY NOT NULL, route_id CHAR(12) NOT NULL, " +
            "service_id CHAR(4) NOT NULL, trip_headsign VARCHAR(8) NOT NULL, direction_id INTEGER NOT NULL, block_id INTEGER)";
    private static final String STOPS_CREATE_TABLE = "CREATE TABLE stops (id VARCHAR(20) PRIMARY KEY NOT NULL, name TEXT NOT NULL, " +
            "latitude DOUBLE, longitude DOUBLE, location_type INTEGER, parent_station VARCHAR(20))";
    private static final String STOP_TIMES_CREATE_TABLE = "CREATE TABLE stop_times (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "trip_id VARCHAR(30) NOT NULL, arrival_time LONG NOT NULL, departure_time LONG NOT NULL, stop_id VARCHAR(20) NOT NULL, " +
            "sequence INTEGER NOT NULL, pickup_type INTEGER NOT NULL, dropoff_type INTEGER NOT NULL)";
    private static final String CALENDAR_CREATE_TABLE = "CREATE TABLE calendar (id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, service_id, " +
            "monday INTEGER NOT NULL, tuesday INTEGER NOT NULL, wednesday INTEGER NOT NULL, thursday INTEGER NOT NULL, friday INTEGER NOT NULL, " +
            "saturday INTEGER NOT NULL, sunday INTEGER NOT NULL, start_date LONG NOT NULL, end_date LONG NOT NULL)";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
        // db.execSQL(TRAVEL_DROP_TABLE);
        // onCreate(db);
    }
}
