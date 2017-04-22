package me.arbogast.trainponctuality.model;

import android.os.Parcel;
import android.os.Parcelable;

import me.arbogast.trainponctuality.gui.Utils;

/**
 * Created by excelsior on 06/02/17.
 * This is an viewModel object to represent a travel
 */

public class History implements Parcelable {
    private Travel iTravel;
    private String dayTravel;
    private boolean isSelected;
    private String departureStation;
    private String arrivalStation;

    public History(Travel travel, String stationDeparture, String stationArrival) {
        this.iTravel = travel;
        this.departureStation = stationDeparture;
        this.arrivalStation = stationArrival;
        this.dayTravel = Utils.dateToString(travel.getDepartureDate());
    }

    public String getDayTravel() {
        return dayTravel;
    }

    public void setDayTravel(String dayTravel) {
        this.dayTravel = dayTravel;
    }

    public Travel getTravel() {
        return iTravel;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    boolean getIsSelected() {
        return isSelected;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    private History(Parcel in) {
        iTravel = in.readParcelable(Travel.class.getClassLoader());
        dayTravel = in.readString();
        departureStation = in.readString();
        arrivalStation = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(iTravel, 0);
        dest.writeString(dayTravel);
        dest.writeString(departureStation);
        dest.writeString(arrivalStation);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<History> CREATOR = new Parcelable.Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

}
