package com.txs.mobilesafe.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceUtil {
    public static boolean isRunning(Context context,String serviceName){
        System.out.println("1231123");
        //获取activity manager 对象 ，可以获取当前手机 正在运行的所有服务
        ActivityManager serviceManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = null;
        if (serviceManager != null) {
            runningServices = serviceManager.getRunningServices(100);
        }
        //循环遍历 所有 正在运行的服务 对其名字进行判断
        if (runningServices != null) {
            for (ActivityManager.RunningServiceInfo info:
                 runningServices) {

                System.out.println("1231123");

                String className = info.service.getClassName();

                System.out.println(className);
                if (className.equals(serviceName)){
                    return true;
                }
            }
        }
        return false;
    }
}
