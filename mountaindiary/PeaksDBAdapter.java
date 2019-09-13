package com.example.mountaindiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class PeaksDBAdapter {
    static final String DATABASE_NAME = "DBMountainDiary.db";
    String ok="OK";
    static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase db;
    private final Context context;
    private static DatabaseHelper dbHelper;

    public  PeaksDBAdapter(Context _context) {
        context = _context;
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method to openthe Database
    public  PeaksDBAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Method to close the Database
    public void close() {
        db.close();
    }

    // Method returns an Instance of the Database
    public  SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    // Method to insert a record in Table
    public String insertEntry(int RangeId, String Title, String Range, String Chain, int Height, String Description, float CoordX, float CoordY, int GainId) {
        try {
            ContentValues newValues = new ContentValues();

            newValues.put("RangeId", RangeId);
            newValues.put("Title", Title);
            newValues.put("Range", Range);
            newValues.put("Chain", Chain);
            newValues.put("Height", Height);
            newValues.put("Description", Description);
            newValues.put("CoordX", CoordX);
            newValues.put("CoordY", CoordY);
            newValues.put("GainId", GainId);

            // Insert the row into your table
            db = dbHelper.getWritableDatabase();
            long result=db.insert("Peaks", null, newValues);
            //System.out.print(result);
            Toast.makeText(context, "Peak Saved", Toast.LENGTH_LONG).show();
        }catch(Exception ex) {
            //System.out.println("Exceptions " +ex);
            Log.e("Note", "One row entered");
        }
        return ok;
    }

    // Method to delete a Record of UserName
    public int deleteEntry(String id) {
        String where="Id=?";
        int numberOFEntriesDeleted= (int) db.delete("Peaks", where, new String[]{id});
        Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }

    // Method to get the entry  of id
    public Cursor getSinlgeEntry(String id) {
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query("Peaks", null, "Id=?", new String[]{id}, null, null, null);
        if(cursor.getCount()<1) // Peaks Not Exist
            return null;
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getEntry() {
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query("Peaks", null, null, null, null, null, null);
        if(cursor.getCount()<1) // Peaks Not Exist
            return null;
        return cursor;
    }

    // Method to Update an Existing
    public void  updateEntry(String id, int RangeId,String Title, String Range,String Chain, int Height, String Description, float CoordX, float CoordY, int GainId) {
        //  create object of ContentValues
        ContentValues updatedValues = new ContentValues();
        // Assign values for each Column.
        ContentValues newValues = new ContentValues();
        updatedValues.put("RangeId", RangeId);
        updatedValues.put("Title", Title);
        updatedValues.put("Range", Range);
        updatedValues.put("Chain", Chain);
        updatedValues.put("Height", Height);
        updatedValues.put("Description", Description);
        updatedValues.put("CoordX", CoordX);
        updatedValues.put("CoordY", CoordY);
        updatedValues.put("GainId", GainId);
        String where="Id = ?";
        db.update("Peaks",updatedValues, where, new String[]{id});
    }
}
