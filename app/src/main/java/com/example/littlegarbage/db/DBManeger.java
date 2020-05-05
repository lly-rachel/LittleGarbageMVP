package com.example.littlegarbage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManeger {

    public static SQLiteDatabase database;
    /*初始化数据库信息*/
    public static void initDB(Context context){
        DBhelper dbHelper = new DBhelper(context);
        database = dbHelper.getWritableDatabase();
    }

    /*查找数据库当中垃圾列表*/
    public static List<String> queryAllGarbageName(){
        Cursor cursor = database.query("info",null,null,null,null,null,null);
        List<String> garbagelist = new ArrayList<>();
        while(cursor.moveToNext()){
            String garbage = cursor.getString(cursor.getColumnIndex("garbage"));
            garbagelist.add(garbage);
        }
        return garbagelist;
    }


    /*新增一条垃圾信息*/
    public static long addGarbageInfo(String garbage,String content){
        ContentValues values = new ContentValues();
        values.put("garbage",garbage);
        values.put("content",content);
        return database.insert("info",null,values);
    }

    /*根据垃圾名查询数据库当中的内容*/
    public static String queryInroByGarbage(String garbage){
        Cursor cursor = database.query("info",null,"garbage=?",new String[]{garbage},null,null,null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            String content=cursor.getString(cursor.getColumnIndex("content"));
            return content;
        }
        return null;
    }

    /*返回当前表格垃圾具体个数*/
    public static int getGarbageCount(){
        Cursor cursor = database.query("info",null,null,null,null,null,null);
        return cursor.getCount();

    }

    /*查询数据库所有信息*/
    public static List<DataBaseBean> queryAllInfo(){
        Cursor cursor = database.query("info",null,null,null,null,null,null);
        List<DataBaseBean> list = new ArrayList<>();
       while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String garbage= cursor.getString(cursor.getColumnIndex("garbage"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            DataBaseBean bean = new DataBaseBean(id,garbage,content);
            list.add(bean);
        }
        return list;
    }

    /*根据城市名称，删除数据库的数据*/
    public static int deleteInfoByGarbage(String garbage){
        return database.delete("info","garbage=?",new String[]{garbage});
    }
}
