package me.arbogast.trainponctuality.Model;

/**
 * Created by excelsior on 23/01/17.
 */

public class Line {
    private String code;
    private int resId;

    public Line(String code, int resId) {
        this.code = code;
        this.resId = resId;
    }

    public String getCode() {
        return code;
    }

    public int getresId() {
        return resId;
    }
}
