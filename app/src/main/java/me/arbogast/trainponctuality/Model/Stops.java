package me.arbogast.trainponctuality.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by excelsior on 15/01/17.
 */

public class Stops implements Parcelable, IGetId {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private int locationType;
    private String parentStation;

    public Stops(String id, String name, double latitude, double longitude, int locationType, String parentStation) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationType = locationType;
        this.parentStation = parentStation;
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
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
    protected Stops(Parcel in) {
        id = in.readString();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        locationType = in.readInt();
        parentStation = in.readString();
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
}
