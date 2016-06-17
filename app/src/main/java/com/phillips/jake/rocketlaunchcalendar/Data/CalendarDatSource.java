package com.phillips.jake.rocketlaunchcalendar.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.phillips.jake.rocketlaunchcalendar.LaunchDetails;

import java.util.ArrayList;

/**
 * Created by jphil on 6/16/2016.
 */
public class CalendarDatSource {

    private SQLiteDatabase database;
    private CalendarSQLiteHelper dbHelper;
    private String[] allColumns = {CalendarSQLiteHelper.COLUMN_ID, CalendarSQLiteHelper.COLUMN_MISSION, CalendarSQLiteHelper.COLUMN_ROCKET,
            CalendarSQLiteHelper.COLUMN_NET, CalendarSQLiteHelper.COLUMN_STATUS, CalendarSQLiteHelper.COLUMN_WINDOW_START,
            CalendarSQLiteHelper.COLUMN_WINDOW_END, CalendarSQLiteHelper.COLUMN_VID_URL, CalendarSQLiteHelper.COLUMN_INFO_URL,
            CalendarSQLiteHelper.COLUMN_MAP_URL};

    public CalendarDatSource(Context context){
        dbHelper = new CalendarSQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public LaunchDetails createDatabaseItem(String mission, String rocket, String net, String vidURL, String infoURL,
                                            String mapURL, int status, int windowStart, int windowClose){
        ContentValues values = new ContentValues();

        values.put(CalendarSQLiteHelper.COLUMN_MISSION, mission);
        values.put(CalendarSQLiteHelper.COLUMN_ROCKET, rocket);
        values.put(CalendarSQLiteHelper.COLUMN_NET, net);
        values.put(CalendarSQLiteHelper.COLUMN_STATUS, status);
        values.put(CalendarSQLiteHelper.COLUMN_WINDOW_START, windowStart);
        values.put(CalendarSQLiteHelper.COLUMN_WINDOW_END, windowClose);
        values.put(CalendarSQLiteHelper.COLUMN_VID_URL, vidURL);
        values.put(CalendarSQLiteHelper.COLUMN_INFO_URL, infoURL);
        values.put(CalendarSQLiteHelper.COLUMN_MAP_URL, mapURL);

        long insertId = database.insert(CalendarSQLiteHelper.TABLE_CALENDAR, null, values);
        Cursor cursor = database.query(CalendarSQLiteHelper.TABLE_CALENDAR, allColumns,
                CalendarSQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        LaunchDetails details = cursorToDetails(cursor);

        cursor.close();

        return details;
    }

    public ArrayList<LaunchDetails> getAllLaunches(){
        ArrayList<LaunchDetails> calendar = new ArrayList<>();

        Cursor cursor = database.query(CalendarSQLiteHelper.TABLE_CALENDAR, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            LaunchDetails details = cursorToDetails(cursor);
            calendar.add(details);
            cursor.moveToNext();
        }

        cursor.close();
        return calendar;
    }


    /*
    * WARNING THIS DELETES THE ENTIRE TABLE!!!!!!!!
    * */
    public void clearDatabase(){
        database.delete(CalendarSQLiteHelper.TABLE_CALENDAR, null, null);
    }

    private LaunchDetails cursorToDetails(Cursor cursor){
        return new LaunchDetails(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getInt(4),
                cursor.getInt(5), cursor.getInt(6), cursor.getInt(0));
    }

}
