package me.arbogast.trainponctuality.model;

import me.arbogast.trainponctuality.gui.Utils;

/**
 * Created by excelsior on 06/02/17.
 * This is an viewModel object to represent a travel
 */

public class History {
    private Travel iTravel;
    private String dayTravel;
    private boolean isSection;
    private boolean isSelected;
    private String departureStation;
    private String arrivalStation;

    public History(String day) {
        isSection = true;
        dayTravel = day;
    }

    public History(Travel travel, String stationDeparture, String stationArrival) {
        this.iTravel = travel;
        this.departureStation = stationDeparture;
        this.arrivalStation = stationArrival;
        this.dayTravel = Utils.dateToString(travel.getDepartureDate());
    }

    public String getDayTravel() {
        return dayTravel;
    }

    public Travel getTravel() {
        return iTravel;
    }

    public boolean isSection() {
        return isSection;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    boolean getIsSelected() {
        return isSelected;
    }

    String getDepartureStation() {
        return departureStation;
    }

    String getArrivalStation() {
        return arrivalStation;
    }
}
