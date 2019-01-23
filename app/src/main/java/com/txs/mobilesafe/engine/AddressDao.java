package com.txs.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressDao {
    //1.指定访问数据 的 路径
    static String path = "data/data/com.txs.mobilesafe/files/address.db";
    private static String tag="地址查询";
    public static SQLiteDatabase db;

    //2.传递一个电话号码 ，开启数据库连接 ，访问，返回一个归属地
    //静态方法
    public static String getAddress(String phone){
        //正则表达式 匹配 电话号码
        boolean telBoolean = isTelPhoneNumber(phone);
        String address = "电话来了";
        if (telBoolean){
            phone = phone.substring(0,7);
            //开启数据库连接
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            //数据库查询
            Cursor cursor = db.query("data1", new String[]{"outkey"}, "id=?", new String[]{phone}, null, null, null);
            //查到即可
            if (cursor.moveToNext()){
                String string = cursor.getString(0);
                Log.i(tag,string);

                Cursor cursor2 = db.query("data2", new String[]{"location"}, "id = ?", new String[]{string}, null, null, null);
                if (cursor2.moveToNext()){
                    address = cursor2.getString(0);
                    Log.i(tag, address);
                }
            }
        }else{
            int phoneL = phone.length();
            switch (phoneL){
                case 3:
                    address= "报警电话";
                    break;
                case 4:
                    address = "模拟器电话";
                    break;
                case 5:
                    address = "服务电话";
                    break;
                case 7:
                    address = "本地电话";
                    break;
                case 8:
                    address = "本地电话";
                    break;
                case 11:
                    String area = phone.substring(1, 3);
                    Cursor cursor = db.query("data2", new String[]{"location"}, "area=?", new String[]{area}, null, null, null);
                    if (cursor.moveToNext()){
                        address=cursor.getString(0);
                    }else{
                        address = "未知号码";
                    }
                    break;
                case 12:
                    String area2 = phone.substring(1, 4);
                    Cursor cursor2 = db.query("data2", new String[]{"location"}, "area=?", new String[]{area2}, null, null, null);
                    if (cursor2.moveToNext()){
                        address=cursor2.getString(0);
                    }else{
                        address = "未知号码";
                    }
                    address = "模拟器电话";
                    break;
            }
        }
        return address;
    }

    /**
     * 手机号号段校验，
     第1位：1；
     第2位：{3、4、5、6、7、8}任意数字；
     第3—11位：0—9任意数字
     * @param value
     * @return
     */
    public static boolean isTelPhoneNumber(String value) {
        if (value != null && value.length() == 11) {
            Pattern pattern = Pattern.compile("^1[3|4|5|6|7|8][0-9]\\d{8}$");
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }
        return false;
    }
}
