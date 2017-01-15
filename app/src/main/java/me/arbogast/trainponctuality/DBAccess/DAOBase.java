package me.arbogast.trainponctuality.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.arbogast.trainponctuality.GUI.Utils;
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

    public SQLiteDatabase getDb() {
        return mDb;
    }


    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected String wrapDateDB(Date date) {
        return dateTimeFormat.format(date);
    }

    protected Date getDateFromDB(String dateStr) {
        try {
            return dateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            Log.e(TAG, "getDateFromDB: Error while parsing date " + dateStr, e);
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            Log.e(TAG, "getDateFromDB: Error while parsing date " + dateStr, e);
            e.printStackTrace();
            return null;
        }
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

    public void delete(long id) {
        openWrite();
        mDb.delete(getTableName(), getColumnId() + " = ?", new String[]{String.valueOf(id)});
    }

    public void delete(T t) {
        openWrite();
        mDb.delete(getTableName(), getColumnId() + " = ?", new String[]{t.getId()});
    }

    public boolean inTransaction() {
        return mDb!=null && mDb.inTransaction();
    }
}
