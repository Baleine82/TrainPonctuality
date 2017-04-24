package me.arbogast.trainponctuality.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by excelsior on 22/12/16.
 * This is a travel (usually the travel in progress)
 */

public class Travel implements Parcelable, IGetId {
    private long id;
    private Date departureDate;
    private Date arrivalDate;
    private String line;
    private String missionCode;
    private String departureStation;
    private String arrivalStation;
    private String tripId;

    public Travel(Date date, String line, String missionCode, String station, String trip) {
        this.departureDate = date;
        this.line = line;
        this.missionCode = missionCode;
        this.departureStation = station;
        this.tripId = trip;
    }

    public Travel(long id, Date departureDate, String departureStation, Date arrivalDate, String arrivalStation, String line, String missionCode, String trip) {
        this.id = id;
        this.departureDate = departureDate;
        this.departureStation = departureStation;
        this.arrivalDate = arrivalDate;
        this.arrivalStation = arrivalStation;
        this.line = line;
        this.missionCode = missionCode;
        this.tripId = trip;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getMissionCode() {
        return missionCode;
    }

    public void setMissionCode(String missionCode) {
        this.missionCode = missionCode;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getTripId() {
        return tripId;
    }

    public String getId() {
        return Long.toString(id);
    }

    public void setId(long id) {
        this.id = id;
    }


    //region Created with Parcelabler
    private Travel(Parcel in) {
        id = in.readLong();
        long tmpDepartureDate = in.readLong();
        departureDate = tmpDepartureDate != -1 ? new Date(tmpDepartureDate) : null;
        long tmpArrivalDate = in.readLong();
        arrivalDate = tmpArrivalDate != -1 ? new Date(tmpArrivalDate) : null;
        line = in.readString();
        missionCode = in.readString();
        departureStation = in.readString();
        arrivalStation = in.readString();
        tripId  = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(departureDate != null ? departureDate.getTime() : -1L);
        dest.writeLong(arrivalDate != null ? arrivalDate.getTime() : -1L);
        dest.writeString(line);
        dest.writeString(missionCode);
        dest.writeString(departureStation);
        dest.writeString(arrivalStation);
        dest.writeString(tripId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Travel> CREATOR = new Parcelable.Creator<Travel>() {
        @Override
        public Travel createFromParcel(Parcel in) {
            return new Travel(in);
        }

        @Override
        public Travel[] newArray(int size) {
            return new Travel[size];
        }
    };

    private boolean isValidDeparture(){
        return line != null && !line.equals("") &&
                missionCode != null && !missionCode.equals("") &&
                departureStation != null && !departureStation.equals("");
    }

    public boolean isValidTravel() {
        return isValidDeparture() &&
                arrivalStation != null && !arrivalStation.equals("");
    }

    //endregion
}
