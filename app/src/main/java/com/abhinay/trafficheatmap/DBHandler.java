package com.abhinay.trafficheatmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "link_traffic_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "link";
    private static final String NAME_COL = "locname";
    private static final String ID_COL = "ID";
    private static final String START_LATITUDE_COL = "startlatitude";
    private static final String START_LONGITUDE_COL = "startlongitude";
    private static final String END_LATITUDE_COL = "endlatitude";
    private static final String END_LONGITUDE_COL = "endlongitude";
    private static final String CAPACITY_COL = "capacity";
    private static final String INDEX_COL = "trafficindex";
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COL + " TEXT, "
                + START_LATITUDE_COL + " REAL,"
                + START_LONGITUDE_COL + " REAL,"
                + END_LATITUDE_COL + " REAL,"
                + END_LONGITUDE_COL + " REAL,"
                + CAPACITY_COL +" REAL,"
                + INDEX_COL + " REAL)";
        db.execSQL(query);
    }

    public void addNewCourse(String name,double startlatitude,double startlongitude,double endlatitude,double endlongitude,double capacity,double index) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL,name);
        values.put(START_LATITUDE_COL,startlatitude);
        values.put(START_LONGITUDE_COL,startlongitude);
        values.put(END_LATITUDE_COL,endlatitude);
        values.put(END_LONGITUDE_COL,endlongitude);
        values.put(CAPACITY_COL,capacity);
        values.put(INDEX_COL,index);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public void update(int a,float b)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(INDEX_COL, b);

        db.update(TABLE_NAME, newValues, ID_COL+"="+Integer.toString(a), null);
    }
    public void reset()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+ TABLE_NAME;
        db.execSQL(query);
    }
    public ArrayList<location> readLocations()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<location> ArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                ArrayList.add(new location(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getDouble(5),
                        cursor.getDouble(6),
                        cursor.getDouble(7)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ArrayList;
    }
    public ArrayList<location> read(int a)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME+" WHERE "+ID_COL+" = "+Integer.toString(a), null);
        ArrayList<location> ArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                ArrayList.add(new location(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getDouble(5),
                        cursor.getDouble(6),
                        cursor.getDouble(7)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

