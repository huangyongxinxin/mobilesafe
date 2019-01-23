package com.txs.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlacknumOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_TALE = "create table blacknumber" +
            "(_id integer primary key autoincrement ," +
            "phone varchar(20)," +
            "mode varchar(5))";
    private static String dbname = "blackNumber.db";
    private static int dbversion = 1;
    //cursorfactory
    public BlacknumOpenHelper(Context context) {
        super(context, dbname, null, dbversion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表
        db.execSQL(CREATE_TALE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
