package com.app.pfh.zhihudaily.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "cache.db"; //数据库名称
    private static final int DB_VERSION = 1; //数据库版本

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists CacheList (id INTEGER primary key autoincrement,date INTEGER unique,json text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
