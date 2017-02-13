package me.arbogast.trainponctuality.Model;

import java.util.Date;

/**
 * Created by excelsior on 15/01/17.
 */

public class StopTimes implements IGetId {
    private String id;
    private Date arrivalTime;
    private Date departureTime;
    private String tripId;
    private String stopId;
    private String sequence;
    private int pickupType;
    private int dropOffType;

    public StopTimes(String id, Date arrivalTime, Date departureTime, String tripId, String stopId, String sequence, int pickupType, int dropOffType) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.tripId = tripId;
        this.stopId = stopId;
        this.sequence = sequence;
        this.pickupType = pickupType;
        this.dropOffType = dropOffType;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getPickupType() {
        return pickupType;
    }

    public void setPickupType(int pickupType) {
        this.pickupType = pickupType;
    }

    public int getDropOffType() {
        return dropOffType;
    }

    public void setDropOffType(int dropOffType) {
        this.dropOffType = dropOffType;
    }
}
