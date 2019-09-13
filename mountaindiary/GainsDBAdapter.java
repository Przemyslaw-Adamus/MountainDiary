package com.example.mountaindiary;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.sql.Blob;

class GainsDBAdapter {

        static final String DATABASE_NAME = "MountainDiaryDB.db";
        String ok="OK";
        static final int DATABASE_VERSION = 1;
        public static SQLiteDatabase db;
        private final Context context;
        private static DatabaseHelper dbHelper;

        public  GainsDBAdapter(Context _context) {
            context = _context;
            dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public  GainsDBAdapter open() throws SQLException {
            db = dbHelper.getWritableDatabase();        return this;
        }

        public void close() {
            db.close();
        }

        public  SQLiteDatabase getDatabaseInstance() {
            return db;
        }

        public int deleteEntry(String id) {
            String where="Id=?";
            int numberOFEntriesDeleted= (int) db.delete("Gains", where, new String[]{id});
            Toast.makeText(context, "Pomyślnie usunieto element: "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
            return numberOFEntriesDeleted;
        }

        public Cursor getSinlgeEntry(String id) {
            db=dbHelper.getReadableDatabase();
            Cursor cursor=db.query("Gains", null, "Id=?", new String[]{id}, null, null, null);
            if(cursor.getCount()<1) // Gain Not Exist
                return null;
            cursor.moveToFirst();
            return cursor;
        }

        public Cursor getEntry() {
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query("Gains", null, null, null, null, null, null);
        if(cursor.getCount()<1) // Gain Not Exist
            return null;
        return cursor;
    }

        public String insertEntry(int PeakId, String StringId, String WinterDates,String SummerDates, float CoordX, float CoordY, String image0,  String image1, String image2, String image3, String image4, String memory, int locationOrFoto) {
            try {
                ContentValues newValues = new ContentValues();
                newValues.put("PeakId", PeakId);
                newValues.put("StringId", StringId);
                newValues.put("WinterDates", WinterDates);
                newValues.put("SummerDates", SummerDates);
                newValues.put("CoordX", CoordX);
                newValues.put("CoordY", CoordY);
                newValues.put("Foto0", image0);
                newValues.put("Foto1", image1);
                newValues.put("Foto2", image2);
                newValues.put("Foto3", image3);
                newValues.put("Foto4", image4);
                newValues.put("Memory", memory);
                newValues.put("LocationOrFoto", locationOrFoto);

                db = dbHelper.getWritableDatabase();
                long result=db.insert("Gains", null, newValues);
                System.out.print(result);
            }catch(Exception ex) {
                System.out.println("Exceptions " +ex);
                Log.e("Note", "One row entered");
            }
            return ok;
        }

        public void  updateEntry(String id, String image0,  String image1, String image2, String image3, String image4, String memory) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("Foto0", image0);
        updatedValues.put("Foto1", image1);
        updatedValues.put("Foto2", image2);
        updatedValues.put("Foto3", image3);
        updatedValues.put("Foto4", image4);
        updatedValues.put("Memory", memory);

        String where="Id = ?";
        db.update("Gains",updatedValues, where, new String[]{id});
    }

    }
