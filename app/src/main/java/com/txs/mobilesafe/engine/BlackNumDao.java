package com.txs.mobilesafe.engine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.txs.mobilesafe.db.BlacknumOpenHelper;
import com.txs.mobilesafe.domain.BlacknumInfo;

import java.util.ArrayList;

public class BlackNumDao {
    //对表 的怎删改查
    //单例模式1.私有化构造方法 2.声明 当前类的对象 3.提供一个方法 如果 当前类的对象 为空 new

    private BlacknumOpenHelper blacknumOpenHelper;
    public BlackNumDao(Context context) {
        //创建数据库  创建表
        blacknumOpenHelper = new BlacknumOpenHelper(context);
    }

    private static BlackNumDao blackNumDao = null;

    public static BlackNumDao getInstance(Context context){
        if (blackNumDao==null){
            blackNumDao = new BlackNumDao(context);
        }
        return blackNumDao;
    }

    //增加一个条目
    public void insert(String phone,String mode){
        SQLiteDatabase db = blacknumOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone",phone);
        contentValues.put("mode",mode);
        db.insert("blacknumber",null,contentValues);
        db.close();
    }

    //删除一个条目
    public void delete(String phone){
        SQLiteDatabase db = blacknumOpenHelper.getWritableDatabase();
//        String[] strs = {phone};
        db.delete("blacknumber","phone = ?",new String[]{phone});
    }
    public void update(String phone , String mode){
        SQLiteDatabase writableDatabase = blacknumOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode",mode);
        writableDatabase.update("blacknumber",contentValues,"phone = ?",new String[]{phone});
    }
    public ArrayList<BlacknumInfo> findAll(){
        SQLiteDatabase writableDatabase = blacknumOpenHelper.getWritableDatabase();
        Cursor cursor = writableDatabase.query("blacknumber", new String[]{"phone", "mode"}, null, null, null, null, "_id desc");
        ArrayList<BlacknumInfo> blacknumInfos = new ArrayList<BlacknumInfo>();
        while (cursor.moveToNext()){
            String phone = cursor.getString(0);
            String mode = cursor.getString(1);
            BlacknumInfo blacknumInfo = new BlacknumInfo();
            blacknumInfo.setPhone(phone);
            blacknumInfo.setMode(mode);
            blacknumInfos.add(blacknumInfo);
        }
        cursor.close();
        writableDatabase.close();
        return blacknumInfos;
    }
    //倒叙 加载 20条数据
    public ArrayList<BlacknumInfo> findlimit(int index){
        SQLiteDatabase writableDatabase = blacknumOpenHelper.getWritableDatabase();

//        writableDatabase.query("blacknumber",new String[]{"phone","mode"},null,null,null,null,"_id desc","0 20",new String[]{});
        String sql = "select phone,mode from blacknumber order by _id limit ?,20 ";
        Cursor cursor = writableDatabase.rawQuery(sql, new String[]{index + ""});
        ArrayList<BlacknumInfo> blacknumInfos = new ArrayList<BlacknumInfo>();
        while (cursor.moveToNext()){
            String phone = cursor.getString(0);
            String mode = cursor.getString(1);
            BlacknumInfo blacknumInfo = new BlacknumInfo();
            blacknumInfo.setPhone(phone);
            blacknumInfo.setMode(mode);
            blacknumInfos.add(blacknumInfo);
        }
        cursor.close();
        writableDatabase.close();
        return blacknumInfos;
    }

    public int getCount(){
        SQLiteDatabase writableDatabase = blacknumOpenHelper.getWritableDatabase();
        String sql = "select count(*) from blacknumber  ";
        Cursor cursor = writableDatabase.rawQuery(sql, null);
        int count= 0;
        if (cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        cursor.close();
        writableDatabase.close();
        return count;
    }

    public int findMode(String originatingAddress) {
        SQLiteDatabase writableDatabase = blacknumOpenHelper.getWritableDatabase();
        String sql = "select mode  from blacknumber where phone = ?  ";
        Cursor cursor = writableDatabase.rawQuery(sql, new String[]{originatingAddress});
        int mode= 0;
        if (cursor.moveToNext()){
            mode = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        writableDatabase.close();
        return mode;
    }
}
