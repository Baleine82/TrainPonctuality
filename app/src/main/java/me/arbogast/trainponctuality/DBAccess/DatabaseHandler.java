package me.arbogast.trainponctuality.DBAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by excelsior on 08/01/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String TRAVEL_CREATE_TABLE = "CREATE TABLE travel (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "departureDate DATE NOT NULL, arrivalDate DATE, line TEXT, mission TEXT, departureStation TEXT NOT NULL, arrivalStation TEXT);";
    public static final String ROUTES_CREATE_TABLE = "CREATE TABLE routes (route_id CHAR(12) PRIMARY KEY NOT NULL, " +
            "agency_id CHAR(6) NOT NULL, route_short_name VARCHAR(5), route_long_name TEXT, route_desc TEXT, route_type INTEGER, route_url TEXT, " +
            "route_color CHAR(6), route_text_color CHAR(6));";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRAVEL_CREATE_TABLE);
        db.execSQL(ROUTES_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL(TRAVEL_DROP_TABLE);
        // onCreate(db);
    }
}
