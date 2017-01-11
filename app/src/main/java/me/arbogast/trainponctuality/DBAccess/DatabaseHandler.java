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
    // public static final String TRAVEL_DROP_TABLE = "DROP TABLE IF EXISTS travel;";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRAVEL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL(TRAVEL_DROP_TABLE);
        // onCreate(db);
    }
}
