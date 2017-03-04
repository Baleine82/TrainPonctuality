package me.arbogast.trainponctuality.gui;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import me.arbogast.trainponctuality.dbaccess.StopsDAO;
import me.arbogast.trainponctuality.model.Stops;

/**
 * Created by excelsior on 03/03/17
 * This is the asyncTask for finding all stations for a given line, ordered by proximity with the user (or ascending name if no location is provided)
 */

class GetStationForLineAsync extends AsyncTask<GetStationForLineParams, Integer, ArrayList<Stops>> {
    private static final String TAG = "InputArrivalActivity";

    @Override
    final protected ArrayList<Stops> doInBackground(GetStationForLineParams... params) {
        ArrayList<Stops> stops;
        long started = System.nanoTime();
        Log.d(TAG, "doInBackground: Started Task");
        try (StopsDAO dbStops = new StopsDAO(params[0].context)) {
            Log.d(TAG, "doInBackground: Database is ready : " + String.valueOf(System.nanoTime() - started));
            stops = dbStops.getStopsForLine(params[0].line);
            Log.d(TAG, "doInBackground: List has been retrieved : " + String.valueOf(System.nanoTime() - started));
        }

        if (params[0].loc == null) {
            Collections.sort(stops, Stops.ASCENDING_NAME_COMPARATOR);
        } else {
            for (Stops stop : stops)
                stop.setDistanceFromUser(params[0].loc.distanceTo(stop.getLocation()));
            Log.d(TAG, "doInBackground: Calculated distances : " + String.valueOf(System.nanoTime() - started));
            Collections.sort(stops, Stops.LOCATION_COMPARATOR);
        }
        Log.d(TAG, "doInBackground: Data sorted (finish) : " + String.valueOf(System.nanoTime() - started));
        return stops;
    }
}

class GetStationForLineParams {
    Context context;
    String line;
    Location loc;

    GetStationForLineParams(Context ctx, String code, Location foundLocation) {
        context = ctx;
        line = code;
        loc = foundLocation;
    }
}
