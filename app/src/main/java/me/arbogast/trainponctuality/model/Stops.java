package me.arbogast.trainponctuality.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by excelsior on 15/01/17.
 * This is a Station on the line
 */

public class Stops implements Parcelable, IGetId {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private int locationType;
    private String parentStation;
    private double distanceFromUser;
    private Location location;

    public Stops(String id, String name, double latitude, double longitude, int locationType, String parentStation) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationType = locationType;
        this.parentStation = parentStation;

        calculateLocation();
    }

    private void calculateLocation() {
        location = new Location("");
        location.setLatitude(this.latitude);
        location.setLongitude(this.longitude);
    }

    public Location getLocation() {
        return location;
    }

    public double getDistanceFromUser() {
        return distanceFromUser;
    }

    public void setDistanceFromUser(double distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        calculateLocation();
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        calculateLocation();
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public String getParentStation() {
        return parentStation;
    }

    public void setParentStation(String parentStation) {
        this.parentStation = parentStation;
    }

    //region Created with Parcelabler
    private Stops(Parcel in) {
        id = in.readString();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        locationType = in.readInt();
        parentStation = in.readString();
        calculateLocation();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(locationType);
        dest.writeString(parentStation);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Stops> CREATOR = new Parcelable.Creator<Stops>() {
        @Override
        public Stops createFromParcel(Parcel in) {
            return new Stops(in);
        }

        @Override
        public Stops[] newArray(int size) {
            return new Stops[size];
        }
    };

    //endregion

    public static final Comparator<Stops> LOCATION_COMPARATOR = new Comparator<Stops>() {
        // Overriding the compare method to sort the age
        public int compare(Stops d, Stops d1) {
            return Double.compare(d.distanceFromUser, d1.distanceFromUser);
        }
    };
}
