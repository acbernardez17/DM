package com.aad.esei.uvigo.core;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {
    public static final String DB_NOMBRE = "CAR_MANAGER";
    public static final int DB_VERSION = 1;

    public static final String COCHE_TABLE = "COCHE";
    public static final String COCHE_ID = "_id";
    public static final String COCHE_NOMBRE = "nombre";
    public static final String COCHE_MARCA = "marca";
    public static final String COCHE_MODELO = "modelo";
    public static final String COCHE_TIPO_COMBUSTIBLE = "tipo_combustible";
    public static final String COCHE_COMBUSTIBLE_MAX = "combustible_max";
    public static final String COCHE_KM_INICIALES = "km_iniciales";
    public static final String COCHE_LITROS_INICIALES = "litros_iniciales";

    public static final String GASTO_TABLE = "GASTO";
    public static final String GASTO_ID = "_id";
    public static final String GASTO_ID_COCHE = "id_coche";
    public static final String GASTO_CATEGORIA = "categoria";
    public static final String GASTO_PRECIO = "precio";
    public static final String GASTO_FECHA = "fecha";
    public static final String GASTO_TITULO = "titulo";

    public static final String REPOSTAJE_TABLE = "REPOSTAJE";
    public static final String REPOSTAJE_ID = "_id";
    public static final String REPOSTAJE_ID_COCHE = "id_coche";
    public static final String REPOSTAJE_PRECIO_LITRO = "precio_litro";
    public static final String REPOSTAJE_LITROS_ANTES = "litros_antes";
    public static final String REPOSTAJE_LITROS_REPOSTADOS = "litros_repostados";
    public static final String REPOSTAJE_KM_ACTUALES = "km_actuales";
    public static final String REPOSTAJE_FECHA = "fecha";
    public static final String REPOSTAJE_TITULO = "titulo";

    private static DBManager instancia;

    public DBManager(Context c) {
        super(c, DB_NOMBRE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DBManager", "Creando base de datos..." + DB_NOMBRE + " v" + DB_VERSION);

        try {
            db.beginTransaction();
            db.execSQL( "CREATE TABLE IF NOT EXISTS " + COCHE_TABLE + "("
                    + COCHE_ID + " TEXT PRIMARY KEY,"
                    + COCHE_NOMBRE + " TEXT NOT NULL CHECK (length(" + COCHE_NOMBRE + ") < 10),"
                    + COCHE_MARCA + " TEXT CHECK (length(" + COCHE_MARCA + ") < 15), "
                    + COCHE_MODELO + " TEXT CHECK (length(" + COCHE_MODELO + ") < 15), "
                    + COCHE_TIPO_COMBUSTIBLE + " TEXT NOT NULL,"
                    + COCHE_COMBUSTIBLE_MAX + " INTEGER NOT NULL,"
                    + COCHE_KM_INICIALES + " INTEGER,"
                    + COCHE_LITROS_INICIALES + " INTEGER"
                    + ")"
            );

            db.execSQL( "CREATE TABLE IF NOT EXISTS " + GASTO_TABLE + "("
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

            db.execSQL( "CREATE TABLE IF NOT EXISTS " + REPOSTAJE_TABLE + "("
                    + REPOSTAJE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + REPOSTAJE_ID_COCHE + " TEXT NOT NULL,"
                    + REPOSTAJE_PRECIO_LITRO + " REAL NOT NULL,"
                    + REPOSTAJE_LITROS_ANTES + " INTEGER NOT NULL,"
                    + REPOSTAJE_LITROS_REPOSTADOS + " INTEGER NOT NULL,"
                    + REPOSTAJE_KM_ACTUALES + " INTEGER NOT NULL,"
                    + REPOSTAJE_FECHA + " TEXT NOT NULL,"
                    + REPOSTAJE_TITULO + " TEXT,"
                    + "FOREIGN KEY (" + REPOSTAJE_ID_COCHE + ")"
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
            db.execSQL("DROP TABLE IF EXISTS " + REPOSTAJE_TABLE);
            db.setTransactionSuccessful();

        } catch (SQLException exc) {
            Log.e("DBManger.onUpgrade", "Actualizando tablas: " + exc.getMessage());
        } finally {
            db.endTransaction();
        }

        this.onCreate(db);
    }

    public static DBManager getManager(Context c) {
        return (instancia == null) ? new DBManager(c) : instancia;
    }
}
