package me.arbogast.trainponctuality.model;

import java.util.Date;

import me.arbogast.trainponctuality.gui.Utils;

/**
 * Created by excelsior on 06/02/17.
 * This is an object who represents the list of trips the user made
 */

public class History implements IGetId {
    private String id;
    private String line;
    private String missionCode;
    private String dayTravel;
    private Date departureDate;
    private String departureStation;
    private Date arrivalDate;
    private String arrivalStation;
    private boolean isSection;

    public History(String day) {
        isSection = true;
        dayTravel = day;
    }

    public History(String travelId, String line, String missionCode, Date day, Date departureDate, String departureStation, Date arrivalDate, String arrivalStation) {
        this.id = travelId;
        this.line = line;
        this.missionCode = missionCode;
        this.dayTravel = Utils.dateToString(day);
        this.departureDate = departureDate;
        this.departureStation = departureStation;
        this.arrivalDate = arrivalDate;
        this.arrivalStation = arrivalStation;
    }

    public String getLine() {
        return line;
    }

    String getMissionCode() {
        return missionCode;
    }

    public String getDayTravel() {
        return dayTravel;
    }

    Date getDepartureDate() {
        return departureDate;
    }

    String getDepartureStation() {
        return departureStation;
    }

    Date getArrivalDate() {
        return arrivalDate;
    }

    String getArrivalStation() {
        return arrivalStation;
    }

    public boolean isSection() {
        return isSection;
    }

    @Override
    public String getId() {
        return id;
    }
}
