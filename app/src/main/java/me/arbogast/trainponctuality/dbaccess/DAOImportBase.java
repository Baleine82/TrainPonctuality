package me.arbogast.trainponctuality.dbaccess;

import android.content.ContentValues;
import android.content.Context;

import me.arbogast.trainponctuality.model.IGetId;

/**
 * Created by excelsior on 15/01/17.
 * This is a class helping with SNCF import
 */

public abstract class DAOImportBase<T extends IGetId> extends DAOBase<T> {
    DAOImportBase(Context pContext) {
        super(pContext);
    }

    public void insert(String[] t) {
        openWrite();
        mDb.insert(getTableName(), null, createValues(t));
    }

    protected abstract ContentValues createValues(String[] t);
}
