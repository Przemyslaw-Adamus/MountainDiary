package com.example.mountaindiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String PEAKS_CREATE = "create table Peaks( Id integer primary key autoincrement, RangeId integer, Title text, Range text, Chain text, Height integer, Description text, CoordX float, CoordY float, GainId);";
    static final String GAINS_CREATE = "create table Gains( Id integer primary key autoincrement, PeakId integer, StringId text, WinterDates text, SummerDates text, CoordX float, CoordY float, Foto0 text, Foto1 text, Foto2 text, Foto3 text, Foto4 text, Memory text, LocationOrFoto integer);";
    static final String PERSONAL_DATA_CREATE = "create table PersonalData( Id integer primary key autoincrement, Name text, Surname text, Number text);";
    static final String DATABASE_CREATE = new StringBuilder().append(PEAKS_CREATE).append("\n").append(GAINS_CREATE).toString();

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(PEAKS_CREATE);
            db.execSQL(GAINS_CREATE);
            db.execSQL(PERSONAL_DATA_CREATE);
        }catch(Exception er){
            Log.e("Error","exceptioin");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = new StringBuilder().append("DROP TABLE IF EXISTS ").append("Peaks").append("\n").append("DROP TABLE IF EXISTS ").append("Gains").append("\n").append("DROP TABLE IF EXISTS ").append("PersonalData").toString();
        db.execSQL(query);
        Log.w("TaskDBAdapter", "Upgrading from version " +oldVersion + " to " + newVersion + ", which will destroy all old data");
        onCreate(db);
        //dbVersion++;
    }
}
