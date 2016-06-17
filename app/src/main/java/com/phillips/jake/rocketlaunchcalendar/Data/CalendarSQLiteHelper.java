package com.phillips.jake.rocketlaunchcalendar.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jphil on 6/16/2016.
 */
public class CalendarSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_CALENDAR = "calendar";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MISSION = "mission";
    public static final String COLUMN_ROCKET = "rocket";
    public static final String COLUMN_NET = "net";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_WINDOW_START = "windowStart";
    public static final String COLUMN_WINDOW_END = "windowEnd";
    public static final String COLUMN_VID_URL = "vidURL";
    public static final String COLUMN_INFO_URL = "infoURL";
    public static final String COLUMN_MAP_URL = "mapURL";

    public static final String DATABASE_NAME = "calendar.db";
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_CREATE = "create table "
            + TABLE_CALENDAR + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MISSION + " text not null, "
            + COLUMN_ROCKET + " text not null, "
            + COLUMN_NET + " text not null, "
            + COLUMN_STATUS + " integer, "
            + COLUMN_WINDOW_START + " integer, "
            + COLUMN_WINDOW_END + " integer, "
            + COLUMN_VID_URL + " text not null, "
            + COLUMN_INFO_URL + " text not null, "
            + COLUMN_MAP_URL + " text not null);";

    public CalendarSQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) { database.execSQL(DATABASE_CREATE);}

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS" + TABLE_CALENDAR);
        onCreate(database);
    }

}
