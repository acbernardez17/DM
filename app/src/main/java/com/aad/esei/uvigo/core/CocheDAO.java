package com.aad.esei.uvigo.core;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CocheDAO {
    private SQLiteDatabase db;

    public CocheDAO(DBManager manager) {
        this.db = manager.getWritableDatabase();
    }

    public String insert(String matricula, ContentValues valores) {
        String primaryKey = null;
        valores.put(DBManager.COCHE_ID, matricula);

        try {
            db.beginTransaction();
            db.insert(DBManager.COCHE_TABLE, null, valores);
            db.setTransactionSuccessful();
            primaryKey = matricula;

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return primaryKey;
    }

    public String update(String matricula, ContentValues valores) {
        String primaryKey = null;

        try {
            db.beginTransaction();
            db.update(DBManager.COCHE_TABLE, valores,
                    DBManager.COCHE_ID + "= ?", new String[]{matricula});
            db.setTransactionSuccessful();
            primaryKey = matricula;

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return primaryKey;
    }

    public void delete(String matricula) {
        try {
            db.beginTransaction();
            db.delete(DBManager.COCHE_TABLE,
                    DBManager.COCHE_ID + "= ?", new String[]{matricula});
            db.setTransactionSuccessful();

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getAllCoches() {
        return db.rawQuery("SELECT * FROM " + DBManager.COCHE_TABLE, null);
    }

    public Cursor getCocheByMatricula(String pk) {
        return db.rawQuery("SELECT * FROM " + DBManager.COCHE_TABLE
                + " WHERE " + DBManager.COCHE_ID + " = ?", new String[]{pk});
    }

    public int getCochesCount() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DBManager.COCHE_TABLE, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
}
