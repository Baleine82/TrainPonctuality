package me.arbogast.trainponctuality.model;

/**
 * Created by excelsior on 15/01/17.
 * This is a route
 */

public class Routes implements IGetId {
    private String id;
    private String agencyId;
    private String shortName;
    private String LongName;
    private int type;
    private String color;
    private String textColor;

    public Routes(String id, String agencyId, String shortName, String longName, int type, String color, String textColor) {
        this.id = id;
        this.agencyId = agencyId;
        this.shortName = shortName;
        this.LongName = longName;
        this.type = type;
        this.color = color;
        this.textColor = textColor;
    }

    public Routes(String id, String agencyId, String shortName, String longName, String type, String color, String textColor) {
        this(id, agencyId, shortName, longName, Integer.parseInt(type), color, textColor);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return LongName;
    }

    public void setLongName(String longName) {
        LongName = longName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}
