package com.dania.one.DatabaseSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseMyData extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "Profile";
    private static final String TABLE_NAME = "MyData";
    private static final String COL1 = "UID";
    private static final String COL2 = "Name";
    private static final String COL3 = "DisplayPicture";
    private static final String COL4 = "FirstName";
    private static final String COL5 = "FamilyName";
    private static final String COL6 = "EmailId";

    public DatabaseMyData(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public boolean addData(String uid, String name, String dp, String first_name, String family_name, String email_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+COL1+" TEXT, "+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT, "+COL6+" TEXT)");
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, uid);
        contentValues.put(COL2, name);
        contentValues.put(COL3, dp);
        contentValues.put(COL4, first_name);
        contentValues.put(COL5, family_name);
        contentValues.put(COL6, email_id);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+COL1+" TEXT, "+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT, "+COL5+" TEXT, "+COL6+" TEXT)");
        Cursor c = db.rawQuery("select * from "+TABLE_NAME,null);
        return c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
