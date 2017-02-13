package me.arbogast.trainponctuality.Model;

import java.util.Date;

import me.arbogast.trainponctuality.GUI.Utils;

/**
 * Created by excelsior on 06/02/17.
 */

public class History {
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

    public History(String line, String missionCode, Date day, Date departureDate, String departureStation, Date arrivalDate, String arrivalStation) {
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

    public String getMissionCode() {
        return missionCode;
    }

    public String getDayTravel() {
        return dayTravel;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public boolean isSection() {
        return isSection;
    }
}
