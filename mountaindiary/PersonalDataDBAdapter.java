package com.example.mountaindiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class PersonalDataDBAdapter {
    static final String DATABASE_NAME = "MountainDiaryDB.db";
    String ok="OK";
    static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase db;
    private final Context context;
    private static DatabaseHelper dbHelper;

    public PersonalDataDBAdapter(Context _context) {
        context = _context;
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PersonalDataDBAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public String insertEntry(String name, String surname, String number) {
        try {
            ContentValues newValues = new ContentValues();
            newValues.put("Name", name);
            newValues.put("Surname", surname);
            newValues.put("Number", number);

            db = dbHelper.getWritableDatabase();
            long result=db.insert("PersonalData", null, newValues);
            System.out.print(result);
            Toast.makeText(context, "Dane zapisane", Toast.LENGTH_LONG).show();
        }catch(Exception ex) {
            System.out.println("Exceptions " +ex);
            Log.e("Note", "One row entered");
        }
        return ok;
    }

    public int deleteEntry(String id) {
        String where="Id=?";
        int numberOFEntriesDeleted= (int) db.delete("PersonalData", where, new String[]{id});
        Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }

    public Cursor getLastEntry() {
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query("PersonalData", null, null, null, null, null, null);
        if(cursor.getCount()<1)
            return null;
        cursor.moveToLast();
        return cursor;
    }

    public Cursor getEntry() {
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query("PersonalData", null, null, null, null, null, null);
        if(cursor.getCount()<1) // Gain Not Exist
            return null;
        return cursor;
    }

    public void  updateEntry(String name, String surname, String number) {
        //  create object of ContentValues
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("Name", name);
        updatedValues.put("Surname", surname);
        updatedValues.put("Number", number);

        String where="Id = ?";
        db.update("PersonalData",updatedValues, where, new String[]{getLastEntry().getString(1)});
    }
}
