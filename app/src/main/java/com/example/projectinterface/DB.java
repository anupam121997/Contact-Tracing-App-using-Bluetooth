package com.example.projectinterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    public DB(Context context) {
        super(context, "device.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table DeviceInfo(deviceId TEXT primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists DeviceInfo");
    }

    public boolean insertid(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deviceId",id);
        long result = db.insert("DeviceInfo", null, contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from DeviceInfo", null);
        return cursor;
    }
}
