package com.qilu.core.util.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class ImageHistoryHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "ImageHistory";
    public static final String TIME = "time";
    public static final String IMAGE = "image";

    //数据库建表语句
    private static final String sql_create = "create table "+TABLE_NAME+" ("+TIME+" text primary key, "+IMAGE+" blob)";

    public ImageHistoryHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TABLE_NAME, "onCreate: " );
        db.execSQL(sql_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
