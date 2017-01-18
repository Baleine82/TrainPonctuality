package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.arbogast.trainponctuality.Model.IGetId;

/**
 * Created by excelsior on 08/01/17.
 */

public abstract class DAOBase<T extends IGetId> {
    private static final String TAG = "DAOBase";

    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 1;
    // Le nom du fichier qui représente ma base
    protected final static String NOM = "database.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase openWrite() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public SQLiteDatabase openRead() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getReadableDatabase();
        return mDb;
    }

    public void close() {
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

    public void beginTransaction(boolean write) {
        if (write)
            openWrite();
        else
            openRead();

        mDb.beginTransaction();
    }

    public void endTransaction(boolean success) {
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

    public void truncate()
    {
        openWrite();
        mDb.execSQL("DELETE FROM " + getTableName());

        if (!mDb.inTransaction())
            mDb.execSQL("VACUUM");
    }

    public List<T> selectAll() {
        openRead();
        List<T> listT = new ArrayList<>();
        Cursor c = mDb.rawQuery(getSelectAllCols() + " from " + getTableName(), null);

        while (c.moveToNext())
            listT.add(getItem(c));

        c.close();

        return listT;
    }

    public boolean inTransaction() {
        return mDb!=null && mDb.inTransaction();
    }
}
