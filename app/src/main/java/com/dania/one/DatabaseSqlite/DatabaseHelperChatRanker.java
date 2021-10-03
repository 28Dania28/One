package com.dania.one.DatabaseSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dania.one.Model.FriendModel;

import java.util.ArrayList;

public class DatabaseHelperChatRanker extends SQLiteOpenHelper {


    private static String DATABASE_NAME = "ChatRanks";
    private static final String INDEX = "Id";
    private static final String UID = "Uid";
    private static final String NAME = "Name";
    private static final String DP = "DP";
    private static final String TOKEN = "Token";

    public DatabaseHelperChatRanker(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addFriendsData(String table_name, String uid, String name, String dp, String token){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ table_name + " ("+INDEX+" INTEGER PRIMARY KEY AUTOINCREMENT, "+UID+" TEXT, "+NAME+" TEXT,"+DP+" TEXT, "+TOKEN+" TEXT )");
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID, uid);
        contentValues.put(NAME, name);
        contentValues.put(DP, dp);
        contentValues.put(TOKEN, token);
        long result = db.insert(table_name, null, contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }


    public Cursor getFriendsData(String table1){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ table1 + " ("+INDEX+" INTEGER PRIMARY KEY AUTOINCREMENT, "+UID+" TEXT, "+NAME+" TEXT,"+DP+" TEXT, "+TOKEN+" TEXT )");
        Cursor c = db.rawQuery("select * from "+table1,null);
        return c;
    }


    public ArrayList<String> getAllFriendsName(String my_id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ "Buddies"+my_id + " ("+INDEX+" INTEGER PRIMARY KEY AUTOINCREMENT, "+UID+" TEXT, "+NAME+" TEXT,"+DP+" TEXT, "+TOKEN+" TEXT )");
        Cursor c = db.rawQuery("select NAME from "+"Buddies"+my_id,null);
        ArrayList<String> name_list = new ArrayList<>();
        if (c.getCount()>0){
            while (c.moveToNext()){
                String full_name = c.getString(c.getColumnIndex(NAME)).toLowerCase();
                full_name = full_name.trim();
                String[] words = full_name.split(" ");
                for (int i = 0;i<words.length;i++){
                    name_list.add(words[i]);
                }
            }
        }
        c.close();
        return name_list;
    }

    public FriendModel getDataFromName(String name, String my_id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ "Buddies"+my_id + " ("+INDEX+" INTEGER PRIMARY KEY AUTOINCREMENT, "+UID+" TEXT, "+NAME+" TEXT,"+DP+" TEXT, "+TOKEN+" TEXT )");
        Cursor c = db.query("Buddies"+my_id, new String[]{UID, NAME, DP, TOKEN}, NAME + " LIKE ?", new String[]{ "%"+name+"%" }, null, null, null, null);
        FriendModel fm = null;
        if (c.moveToFirst()){
            fm = new FriendModel(c.getString(0),c.getString(1),c.getString(2),c.getString(3));
        }
        c.close();
        return fm;
    }

    public void deleteAllFriends(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+table_name);
        db.close();
    }

}