package com.example.jhan.searchboxtry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by huimin on 3/20/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "data.sqlite";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    //create a table name location with two column(_id, latlng)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table location (_id integer primary key autoincrement, latlng String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //insert data into location table
    public long insertLatlng(String latlng) {
        ContentValues cv = new ContentValues();
        cv.put("latlng", latlng);
        return getWritableDatabase().insert("location", null, cv);
    }

    //query the table
    public LocationCursor queryLocation() {
        //select * from location
        Cursor wrapped = getReadableDatabase().query("location",null,null,null,null,null,null);
        return new LocationCursor(wrapped);
    }

    //use CursorWrapper to return rows
    public static class LocationCursor extends CursorWrapper {

        public LocationCursor(Cursor c) {
            super(c);
        }

        public String getLocation() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            long locationId = getLong(getColumnIndex("_id"));
            sb.append("ID = ").append(locationId).append(" ---- ");
            String locationDetail = getString(getColumnIndex("latlng"));
            sb.append("location = ").append(locationDetail);
            return sb.toString();
        }
    }
}
