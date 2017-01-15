package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;

import me.arbogast.trainponctuality.Model.Routes;

/**
 * Created by excelsior on 15/01/17.
 */

public class RoutesDAO extends DAOImportBase<Routes> {
    private static final String TAG = "TravelDAO";

    private static final String TABLE_NAME = "routes";
    private static final String COLUMN_ID = "route_id";
    private static final String COLUMN_AGENCY = "agency_id";
    private static final String COLUMN_SHORT_NAME = "route_short_name";
    private static final String COLUMN_LONG_NAME = "route_long_name";
    private static final String COLUMN_TYPE = "route_type";
    private static final String COLUMN_COLOR = "route_color";
    private static final String COLUMN_TEXT_COLOR = "route_text_color";

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
        value.put(COLUMN_TYPE, t[5]);
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
}
