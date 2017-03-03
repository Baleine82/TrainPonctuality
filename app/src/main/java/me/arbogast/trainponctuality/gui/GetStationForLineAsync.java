package me.arbogast.trainponctuality.gui;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import me.arbogast.trainponctuality.dbaccess.StopsDAO;
import me.arbogast.trainponctuality.model.Stops;

/**
 * Created by excelsior on 03/03/17
 * This is the asyncTask for finding all stations for a given line, ordered by proximity with the user
 */

class GetStationForLineAsync extends AsyncTask<GetStationForLineParams, Integer, List<Stops>> {
    Context ctx;
    private static final String TAG = "InputArrivalActivity";

    @Override
    final protected List<Stops> doInBackground(GetStationForLineParams... params) {
        List<Stops> stops;
        long started = System.nanoTime();
        Log.d(TAG, "doInBackground: Started Task");
        ctx = params[0].context;
        try (StopsDAO dbStops = new StopsDAO(params[0].context)) {
            Log.d(TAG, "doInBackground: Database is ready : " + String.valueOf(System.nanoTime() - started));
            stops = dbStops.getStopsForLine(params[0].line);
            Log.d(TAG, "doInBackground: List has been retrieved : " + String.valueOf(System.nanoTime() - started));
        }

        for (Stops stop : stops)
            stop.setDistanceFromUser(params[0].loc.distanceTo(stop.getLocation()));
        Log.d(TAG, "doInBackground: Calculated distances : " + String.valueOf(System.nanoTime() - started));
        Collections.sort(stops, Stops.LOCATION_COMPARATOR);
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
