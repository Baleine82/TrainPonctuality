package me.arbogast.trainponctuality.Model;

import java.util.Date;

/**
 * Created by excelsior on 22/12/16.
 */

public class Travel {
    private Date departureDate;
    private Date arrivalDate;
    private String line;
    private String missionCode;
    private String departureStation;
    private String arrivalStation;
    private long id;

    public Travel(Date date, String line, String missionCode, String station) {
        this.departureDate=date;
        this.line=line;
        this.missionCode=missionCode;
        this.departureStation=station;
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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
