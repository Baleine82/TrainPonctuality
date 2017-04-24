package me.arbogast.trainponctuality.dbaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.arbogast.trainponctuality.model.IGetId;

/**
 * Created by excelsior on 08/01/17
 */

public abstract class DAOBase<T extends IGetId> implements AutoCloseable {
    private static final String TAG = "DAOBase";

    private final static int VERSION = 2;
    private final static String NOM = "database.db";

    SQLiteDatabase mDb = null;
    private DatabaseHandler mHandler = null;

    DAOBase(Context pContext) {
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    final void openWrite() {
        if (inTransaction() && !mDb.isReadOnly())
            return;

        closeDb();
        mDb = mHandler.getWritableDatabase();
    }

    final void openRead() {
        if (mDb != null)
            return;

        closeDb();
        mDb = mHandler.getReadableDatabase();
    }

    public void close() {
        if (mDb != null)
            mDb.close();
    }

    public void insert(T t) {
        openWrite();
        mDb.insert(getTableName(), null, createValues(t));
    }

    public void update(T t) {
        if (t.getId().isEmpty() || t.getId().equals("0")) {
            Log.w(TAG, "update : Cannot update object with id = 0");
            return;
        }

        openWrite();
        mDb.update(getTableName(), createValues(t), getColumnId() + " = ?", new String[]{t.getId()});
    }

    public final void beginTransaction() {
        // If we are already in transaction, we don't need to openWrite if write isn't required or we already have write permissions
        if (inTransaction())
            return;

        openWrite();
        mDb.beginTransaction();
    }

    public final void endTransaction(boolean success) {
        if (success)
            mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    protected abstract ContentValues createValues(T t);

    protected abstract String getTableName();

    protected abstract String getColumnId();

    protected abstract String getSelectAllCols();

    protected abstract T getItem(Cursor c);

    public void delete(long id) {
        openWrite();
        mDb.delete(getTableName(), getColumnId() + " = ?", new String[]{String.valueOf(id)});
    }

    public void delete(T t) {
        openWrite();
        mDb.delete(getTableName(), getColumnId() + " = ?", new String[]{t.getId()});
    }

    public void truncate() {
        openWrite();
        mDb.execSQL("DELETE FROM " + getTableName());

        if (!mDb.inTransaction())
            mDb.execSQL("VACUUM");
    }

    public List<T> selectAll() {
        openRead();
        List<T> listT = new ArrayList<>();
        try (Cursor c = mDb.rawQuery("SELECT " + getSelectAllCols() + " from " + getTableName(), null)) {

            while (c.moveToNext())
                listT.add(getItem(c));
        }

        return listT;
    }

    private void closeDb() {
        if (mDb != null && mDb.isOpen()) {
            if (mDb.inTransaction())
                mDb.endTransaction();

            mDb.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        closeDb();
        super.finalize();
    }

    public boolean inTransaction() {
        return mDb != null && mDb.inTransaction();
    }
}
