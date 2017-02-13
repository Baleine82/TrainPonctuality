package me.arbogast.trainponctuality.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by excelsior on 13/02/17
 * This class is the access point for location
 */

public class LocationProxy extends Observable {
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int GOODENOUGHACCURACY = 100;
    private static final String TAG = "LocationProxy";

    private LocationManager locMan;
    private LocationListener locLis;
    private Location lastBest;
    private boolean isInit = false;
    private static LocationProxy INSTANCE = new LocationProxy();
    private boolean hasChanged;

    public static LocationProxy getInstance() {
        return INSTANCE;
    }

    public Location getLastBest() {
        return lastBest;
    }

    private LocationProxy() {
    }

    private void notifyIfAccuracyIsMet() {
        Log.i(TAG, "notifyIfAccuracyIsMet: Accuracy = " + lastBest.getAccuracy());
        if (lastBest.getAccuracy() < GOODENOUGHACCURACY) {
            Log.i(TAG, "notifyIfAccuracyIsMet: ");
            setChanged();
            hasChanged = true;
            notifyObservers();
        }
    }

    private void Initialize(Context c) {
        locMan = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        locLis = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "onLocationChanged: " + location);
                if (isBetterLocation(location, lastBest))
                    lastBest = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
            }


            private boolean isBetterLocation(Location location, Location currentBestLocation) {
                if (currentBestLocation == null) {
                    notifyIfAccuracyIsMet();
                    return true;
                }

                // Check whether the new location fix is newer or older
                long timeDelta = location.getTime() - currentBestLocation.getTime();
                boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
                boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
                boolean isNewer = timeDelta > 0;

                // If it's been more than two minutes since the current location, use the new location
                // because the user has likely moved
                if (isSignificantlyNewer) {
                    notifyIfAccuracyIsMet();
                    return true;
                    // If the new location is more than two minutes older, it must be worse
                } else if (isSignificantlyOlder) {
                    return false;
                }

                // Check whether the new location fix is more or less accurate
                int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
                boolean isLessAccurate = accuracyDelta > 0;
                boolean isMoreAccurate = accuracyDelta < 0;
                boolean isSignificantlyLessAccurate = accuracyDelta > 200;

                // Check if the old and new location are from the same provider
                boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

                // Determine location quality using a combination of timeliness and accuracy
                if (isMoreAccurate) {
                    notifyIfAccuracyIsMet();
                    return true;
                } else if (isNewer && !isLessAccurate) {
                    return true;
                } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                    notifyIfAccuracyIsMet();
                    return true;
                }
                return false;
            }

            /** Checks whether two providers are the same */
            private boolean isSameProvider(String provider1, String provider2) {
                if (provider1 == null) {
                    return provider2 == null;
                }
                return provider1.equals(provider2);
            }
        };

        isInit = true;
    }

    public void startRequest(Context c) {
        hasChanged = false;
        Log.i(TAG, "startRequest: ");
        if (!isInit) {
            Log.i(TAG, "startRequest: Initializing");
            Initialize(c);
        }

        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location lastGps = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastNetwork = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        setLastBest(lastGps, lastNetwork);
        if (hasChanged)
            return;

        Log.i(TAG, "startRequest: requestLocationUpdates");
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locLis);
    }

    public void stopRequest(Context c) {
        Log.i(TAG, "stopRequest: ");
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locMan.removeUpdates(locLis);
    }

    private void setLastBest(Location lastGps, Location lastNetwork) {
        if (lastGps == null && lastNetwork == null)
            lastBest = null;

        if (lastGps == null)
            lastBest = lastNetwork;

        if (lastNetwork == null)
            lastBest = lastGps;

        long GPSLocationTime = 0;
        if (null != lastGps) {
            GPSLocationTime = lastGps.getTime();
        }

        long NetLocationTime = 0;

        if (null != lastNetwork)
            NetLocationTime = lastNetwork.getTime();

        if (0 < GPSLocationTime - NetLocationTime)
            lastBest = lastGps;
        else
            lastBest = lastNetwork;

        notifyIfAccuracyIsMet();
    }
}
