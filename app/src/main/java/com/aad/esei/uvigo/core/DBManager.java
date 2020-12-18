package com.aad.esei.uvigo.core;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {
    public static final String DB_NOMBRE = "CAR_MANAGER";
    public static final int DB_VERSION = 2;

    public static final String COCHE_TABLE = "COCHE";
    public static final String COCHE_ID = "_id";
    public static final String COCHE_NOMBRE = "nombre";
    public static final String COCHE_MARCA = "marca";
    public static final String COCHE_MODELO = "modelo";
    public static final String COCHE_TIPO_COMBUSTIBLE = "tipo_combustible";
    public static final String COCHE_COMBUSTIBLE_MAX = "combustible_max";
    public static final String COCHE_KM_INICIALES = "km_iniciales";

    public static final String GASTO_TABLE = "GASTO";
    public static final String GASTO_ID = "_id";
    public static final String GASTO_ID_COCHE = "id_coche";
    public static final String GASTO_CATEGORIA = "categoria";
    public static final String GASTO_PRECIO = "precio";
    public static final String GASTO_FECHA = "fecha";
    public static final String GASTO_TITULO = "titulo";

    private static DBManager instancia;

    public DBManager(Context c) {
        super(c, DB_NOMBRE, null, DB_VERSION);
    }

    public static DBManager getManager(Context c) {
        return (instancia == null) ? new DBManager(c) : instancia;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DBManager", "Creando base de datos..." + DB_NOMBRE + " v" + DB_VERSION);

        try {
            db.beginTransaction();
            db.execSQL("CREATE TABLE IF NOT EXISTS " + COCHE_TABLE + "("
                    + COCHE_ID + " TEXT PRIMARY KEY,"
                    + COCHE_NOMBRE + " TEXT NOT NULL,"
                    + COCHE_MARCA + " TEXT, "
                    + COCHE_MODELO + " TEXT, "
                    + COCHE_TIPO_COMBUSTIBLE + " TEXT,"
                    + COCHE_COMBUSTIBLE_MAX + " INTEGER,"
                    + COCHE_KM_INICIALES + " INTEGER"
                    + ")"
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + GASTO_TABLE + "("
                    + GASTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + GASTO_ID_COCHE + " TEXT NOT NULL,"
                    + GASTO_CATEGORIA + " TEXT NOT NULL,"
                    + GASTO_PRECIO + " REAL NOT NULL,"
                    + GASTO_FECHA + " TEXT NOT NULL,"
                    + GASTO_TITULO + " TEXT,"
                    + "FOREIGN KEY (" + GASTO_ID_COCHE + ")"
                    + "REFERENCES " + COCHE_TABLE + " (" + COCHE_ID + ")"
                    + "ON DELETE CASCADE ON UPDATE NO ACTION"
                    + ")"
            );

            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("DBManger.onCreate", "Creando tablas: " + exc.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DBManager", "Actualizando base de datos..."
                + DB_NOMBRE + " v" + oldVersion + " -> v" + newVersion);
        try {
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS " + COCHE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + GASTO_TABLE);
            db.setTransactionSuccessful();

        } catch (SQLException exc) {
            Log.e("DBManger.onUpgrade", "Actualizando tablas: " + exc.getMessage());
        } finally {
            db.endTransaction();
        }

        this.onCreate(db);
    }
}
