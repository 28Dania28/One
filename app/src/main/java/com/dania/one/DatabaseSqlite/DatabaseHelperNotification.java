package com.dania.one.DatabaseSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperNotification extends SQLiteOpenHelper {


    private static String DATABASE_NAME = "Notifications";
    private static final String INDEX = "Id";
    private static final String UID = "Uid";
    private static final String NAME = "Name";
    private static final String DP = "DP";
    private static final String CONTENT = "Content";
    private static final String INTENT = "Intent";
    private static final String STATUS = "Status";

    public DatabaseHelperNotification(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addNotification(String table_name, String uid, String name, String dp, String content, String intent, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ table_name + " ("+INDEX+" INTEGER PRIMARY KEY AUTOINCREMENT, "+UID+" TEXT, "+NAME+" TEXT,"+DP+" TEXT, "+CONTENT+" TEXT,"+INTENT+" TEXT,"+STATUS+" TEXT )");
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID, uid);
        contentValues.put(NAME, name);
        contentValues.put(DP, dp);
        contentValues.put(CONTENT, content);
        contentValues.put(INTENT, intent);
        contentValues.put(STATUS, status);
        long result = db.insert(table_name, null, contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }


    public Cursor getNotifications(String table_name){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ table_name + " ("+INDEX+" INTEGER PRIMARY KEY AUTOINCREMENT, "+UID+" TEXT, "+CONTENT+" TEXT,"+INTENT+" TEXT,"+STATUS+" TEXT )");
        Cursor c = db.rawQuery("select * from "+table_name,null);
        return c;
    }



}
