package com.example.lokalnedoserwisu.tracksaver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTracksHelper extends SQLiteOpenHelper {

    String username;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TracksFromGPS.db";

    // Track table name
    private String TABLE_TRACK;

    // Tracks table name
    private String TABLE_TRACKS;

    // Track Table Columns names
    private static final String COLUMN_TIME = "time_sec";
    private static final String COLUMN_LON = "longitude";
    private static final String COLUMN_LAT = "lattitude";
    private static final String COLUMN_HEIGHT = "height";

    // Tracks Table Columns names
    private static final String COLUMN_TRACK_ID = "track_id";
    private static final String COLUMN_TRACK_TABLE_NAME="track_table_name";

    // create tracks table sql query
    private String CREATE_TRACKS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRACKS + "("
            + COLUMN_TRACK_TABLE_NAME + " TEXT NOT NULL )";

    // drop track table sql query
    private String DROP_TRACK_TABLE = "DROP TABLE IF EXISTS " + TABLE_TRACK;

    public DatabaseTracksHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        username = context.getApplicationContext().getSharedPreferences("dane", Context.MODE_PRIVATE).getString("username", null);
        TABLE_TRACKS="tracks_of_"+username;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRACKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_TRACK_TABLE);

        // Create tables again
        onCreate(db);
    }

    public void CreateTrackTable(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        TABLE_TRACK=username + "_" + name;
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_TRACKS + "("
                + COLUMN_TRACK_TABLE_NAME + " TEXT NOT NULL )";
        db.execSQL(query);
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRACK_TABLE_NAME, TABLE_TRACK);
        db.insert(TABLE_TRACKS, null, values);

        // create track table sql query
        String CREATE_TRACK_TABLE = "CREATE TABLE " + TABLE_TRACK + "("
                + COLUMN_TIME + " TEXT NOT NULL, "
                + COLUMN_LON + " REAL NOT NULL, "
                + COLUMN_LAT + " REAL NOT NULL, "
                + COLUMN_HEIGHT + " REAL NOT NULL " + ")";
        db.execSQL(CREATE_TRACK_TABLE);
        db.close();
    }

    public void AddPointToTrack(String name, String time, Double lon, Double lat, Double height){
        SQLiteDatabase db=this.getWritableDatabase();
        TABLE_TRACK=username + "_" + name;
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_LON, lon);
        values.put(COLUMN_LAT, lat);
        values.put(COLUMN_HEIGHT, height);
        db.insert(TABLE_TRACK, null, values);
        db.close();
    }

    public List<String> getTracks(){
        SQLiteDatabase db=this.getReadableDatabase();
        List<String> tracks = new ArrayList<String>();
        // Check if table tracks exist
        String query = "SELECT * FROM sqlite_master WHERE type='table' AND name='" + TABLE_TRACKS + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            // selection arguments
            query = "SELECT " + COLUMN_TRACK_TABLE_NAME + " FROM " + TABLE_TRACKS;
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    tracks.add(cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_TABLE_NAME)));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return tracks;
    }

    private ArrayList<String> getColumnStrings(SQLiteDatabase db, String tableName, String columnName) {
        ArrayList<String> column = new ArrayList<String>();
        String query = "SELECT " + columnName + " FROM " + tableName + " ORDER BY " + COLUMN_TIME + " ASC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                column.add(cursor.getString(cursor.getColumnIndex(columnName)));
            } while (cursor.moveToNext());
        }
        return column;
    }

    private ArrayList<Double> getColumnDoubles(SQLiteDatabase db, String tableName, String columnName) {
        ArrayList<Double> column = new ArrayList<Double>();
        String query = "SELECT " + columnName + " FROM " + tableName + " ORDER BY " + COLUMN_TIME + " ASC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                column.add(cursor.getDouble(cursor.getColumnIndex(columnName)));
            } while (cursor.moveToNext());
        }
        return column;
    }


    public Track getTrack(String name) {
        SQLiteDatabase db=this.getReadableDatabase();
        ArrayList<String> time = getColumnStrings(db, name, COLUMN_TIME);
        ArrayList<Double> lon = getColumnDoubles(db, name, COLUMN_LON);
        ArrayList<Double> lan = getColumnDoubles(db, name, COLUMN_LAT);
        ArrayList<Double> alt = getColumnDoubles(db, name, COLUMN_HEIGHT);
        Track track = new Track(name, time, lon, lan, alt);
        db.close();
        return track;
    }

    public void deleteTrack(Track track) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TRACKS;
        db.rawQuery(query, null);
        String deleteTableQuery = "DROP TABLE IF EXISTS " + track.getName();
        String deleteRowFromTableTracks = "DELETE FROM " + TABLE_TRACKS + " WHERE " + COLUMN_TRACK_TABLE_NAME + "='" + track.getName() + "'";
        db.execSQL(deleteTableQuery);
        db.execSQL(deleteRowFromTableTracks);
        db.close();
    }

    public int getTracksCount() {
        return getTracks().size();
    }
}
