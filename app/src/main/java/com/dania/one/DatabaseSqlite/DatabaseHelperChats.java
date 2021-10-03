package com.dania.one.DatabaseSqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperChats  extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "Chats";
    private static final String COL1 = "Key";
    private static final String COL2 = "Msg";
    private static final String COL3 = "Time";
    private static final String COL4 = "Date";
    private static final String COL5 = "Sender";
    private static final String COL6 = "Timestamp";
    private static final String COL7 = "Type";
    private static final String COL8 = "Direction";
    private static final String COL9 = "Uri";
    private static final String COL10 = "Extra";


    public DatabaseHelperChats(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addData(String table_name, String msg, String time, String date, String sender, String key, int timestamp, String type, String direction, String uri, String extra){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ table_name + " ("+COL1+" TEXT PRIMARY KEY,"+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT, "+COL6+" INTEGER, "+COL7+" TEXT, "+COL8+" TEXT, "+COL9+" TEXT, "+COL10+" TEXT)");
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, key);
        contentValues.put(COL2, msg);
        contentValues.put(COL3, time);
        contentValues.put(COL4, date);
        contentValues.put(COL5, sender);
        contentValues.put(COL6, timestamp);
        contentValues.put(COL7, type);
        contentValues.put(COL8, direction);
        contentValues.put(COL9, uri);
        contentValues.put(COL10, extra);
        long result = db.insert(table_name, null, contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }


    public Cursor getData(String table1){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ table1 + " ("+COL1+" TEXT PRIMARY KEY,"+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT, "+COL6+" INTEGER, "+COL7+" TEXT, "+COL8+" TEXT, "+COL9+" TEXT, "+COL10+" TEXT)");
        Cursor c = db.rawQuery("select * from "+table1,null);
        return c;
    }

    public void deleteData(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+table_name);
        db.close();
    }

}


