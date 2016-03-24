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
        db.execSQL("create table if not exists business2 (_id integer primary key autoincrement, name String, " +
                "rate Sting, imageurl String, ratingimageurl String, reviewcount integer, mapurl String, address String, " +
                "phone String, snippetimageurl String, snippettext String, latitude String, longitude String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //insert data into location table
    public long insertLatlng(String name, String rate, String imageurl, String ratingimageurl, int reviewcount, String mapurl, String address, String phone, String snippetimageurl, String snippettext, String latitude, String longitude) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("rate", rate);
        cv.put("imageurl", imageurl);
        cv.put("ratingimageurl", ratingimageurl);
        cv.put("reviewcount", reviewcount);
        cv.put("mapurl", mapurl);
        cv.put("address", address);
        cv.put("phone", phone);
        cv.put("snippetimageurl", snippetimageurl);
        cv.put("snippettext", snippettext);
        cv.put("latitude", latitude);
        cv.put("longitude", longitude);
        return getWritableDatabase().insert("business2", null, cv);
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
