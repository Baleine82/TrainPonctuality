package me.arbogast.trainponctuality.model;

/**
 * Created by excelsior on 23/01/17.
 * This is a line (Line B of Paris RER, eg)
 */

public class Line {
    private String code;

    public Line(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
