package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import me.arbogast.trainponctuality.Model.Line;
import me.arbogast.trainponctuality.Model.Routes;

/**
 * Created by excelsior on 15/01/17.
 */

public class RoutesDAO extends DAOImportBase<Routes> {
    private static final String TAG = "TravelDAO";

    static final String TABLE_NAME = "routes";
    static final String COLUMN_ID = "rte_id";
    static final String COLUMN_AGENCY = "rte_agency_id";
    static final String COLUMN_SHORT_NAME = "rte_route_short_name";
    static final String COLUMN_LONG_NAME = "rte_route_long_name";
    static final String COLUMN_TYPE = "rte_route_type";
    static final String COLUMN_COLOR = "rte_route_color";
    static final String COLUMN_TEXT_COLOR = "rte_route_text_color";

    private static final String SELECT_ALL = COLUMN_ID + ", " + COLUMN_AGENCY + ", " + COLUMN_SHORT_NAME + ", " +
            COLUMN_LONG_NAME + ", " + COLUMN_TYPE + ", " + COLUMN_COLOR + ", " + COLUMN_TEXT_COLOR;

    public RoutesDAO(Context pContext) {
        super(pContext);
    }

    @Override
    protected ContentValues createValues(Routes t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_ID, t.getId());
        value.put(COLUMN_AGENCY, t.getAgencyId());
        value.put(COLUMN_SHORT_NAME, t.getShortName());
        value.put(COLUMN_LONG_NAME, t.getLongName());
        value.put(COLUMN_TYPE, t.getType());
        value.put(COLUMN_COLOR, t.getColor());
        value.put(COLUMN_TEXT_COLOR, t.getTextColor());

        return value;
    }

    @Override
    protected ContentValues createValues(String[] t) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_ID, t[0]);
        value.put(COLUMN_AGENCY, t[1]);
        value.put(COLUMN_SHORT_NAME, t[2]);
        value.put(COLUMN_LONG_NAME, t[3]);
        value.put(COLUMN_TYPE, Integer.parseInt(t[5]));
        value.put(COLUMN_COLOR, t[7]);
        value.put(COLUMN_TEXT_COLOR, t[8]);

        return value;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getColumnId() {
        return COLUMN_ID;
    }

    @Override
    protected String getSelectAllCols() {
        return SELECT_ALL;
    }

    @Override
    protected Routes getItem(Cursor c) {
        return new Routes(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5), c.getString(6));
    }

    public List<Line> getDistinctLines() {
        openRead();
        List<Line> listT = new ArrayList<>();
        Cursor c = mDb.rawQuery("SELECT DISTINCT " + COLUMN_SHORT_NAME + " FROM " + TABLE_NAME + " WHERE LENGTH(" + COLUMN_SHORT_NAME + ") = 1 ORDER BY " + COLUMN_SHORT_NAME + ";", null);

        while (c.moveToNext())
            listT.add(new Line(c.getString(0)));

        c.close();

        return listT;
    }
}
