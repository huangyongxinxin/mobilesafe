package com.txs.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

//上下文 拿到sharedpreferences

public class SpUtil {

    public static SharedPreferences sp;

    public static void putBollean(Context context, String key, Boolean b){
        if (sp == null){
            sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,b).apply();
    }

    public static boolean getBollean(Context context, String key, Boolean b){
        if (sp == null){
            sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
//        sp.edit().putBoolean(key,b).apply();
        return sp.getBoolean(key,b);
    }
    public static void putString(Context context, String key, String s){
        if (sp == null){
            sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,s).apply();
    }

    public static String getString(Context context, String key, String s){
        if (sp == null){
            sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
//        sp.edit().putBoolean(key,b).apply();
        return sp.getString(key,s);
    }


    public static void putint(Context context, String key, int s){
        if (sp == null){
            sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key,s).apply();
    }

    public static int getint(Context context, String key, int s){
        if (sp == null){
            sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
//        sp.edit().putBoolean(key,b).apply();
        return sp.getInt(key,s);
    }


    //移除制定节点
    public static void remove(Context context, String key) {
        if (sp == null){
            sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
//        sp.edit().putBoolean(key,b).apply();

        sp.edit().remove(key).apply();
    }
}
