package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;

import me.arbogast.trainponctuality.Model.IGetId;

/**
 * Created by excelsior on 15/01/17.
 */

public abstract class DAOImportBase<T extends IGetId> extends DAOBase<T> {
    public DAOImportBase(Context pContext) {
        super(pContext);
    }

    public void insert(String[] t) {
        openWrite();
        mDb.insert(getTableName(), null, createValues(t));
    }

    protected abstract ContentValues createValues(String[] t);
}
