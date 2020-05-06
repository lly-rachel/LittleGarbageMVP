package com.example.littlegarbage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    public DBhelper(Context context){
        super(context,"LittleGarbage.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建表的操作
        String sql="create table info(_id integer primary key autoincrement,garbage varchar(20) unique not null,content text not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
