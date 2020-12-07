package com.aad.esei.uvigo.core;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GastoDAO {
    private SQLiteDatabase db;

    public GastoDAO(DBManager manager) {
        this.db = manager.getWritableDatabase();
    }

    public String insert(String id, ContentValues valores) {
        String primaryKey = null;
        valores.put(DBManager.GASTO_ID, id);

        try {
            db.beginTransaction();
            db.insert(DBManager.GASTO_TABLE, null, valores);
            db.setTransactionSuccessful();
            primaryKey = id;

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return primaryKey;
    }

    public String update(String id, ContentValues valores) {
        String primaryKey = null;

        try {
            db.beginTransaction();
            db.update(DBManager.GASTO_TABLE, valores,
                    DBManager.GASTO_ID + "= ?", new String[]{ id });
            db.setTransactionSuccessful();
            primaryKey = id;

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return primaryKey;
    }

    public void delete(String id) {
        try {
            db.beginTransaction();
            db.delete(DBManager.GASTO_TABLE,
                    DBManager.GASTO_ID + "= ?", new String[]{ id });
            db.setTransactionSuccessful();

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getAllGastosCoche(String pk, Date fecha){
        SimpleDateFormat isoDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.ROOT );
        isoDateFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
        String fechaSQL = isoDateFormat.format( fecha );
        return db.rawQuery("SELECT * FROM " + DBManager.GASTO_TABLE
                + " WHERE " + DBManager.GASTO_ID_COCHE + " = ?" +
                " AND " + DBManager.GASTO_FECHA + " = ?"
                , new String[]{pk,fechaSQL});
    }


}