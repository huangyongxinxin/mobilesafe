package com.txs.mobilesafe.receiver;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;


public class LocationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        //获取手机的经纬度坐标
        //1.获取位置管理者对象ssssssssssssss
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //以最优的方式拿到经纬度坐标
        //critetia 交叉来回移动 一种查询方式
        Criteria criteria = new Criteria();
        //允许 话费 流量
        criteria.setCostAllowed(true);
        //制定获取经纬度的精确度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        if (lm != null) {
            String bestProvider = lm.getBestProvider(criteria, true);
            //在一定时间间隔 或者  移动一定距离后获得经纬度坐标
            MyLocationListener myLocationListener = new MyLocationListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                //请求 权限 然后 重写 请求回调方法

                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ///解决 用户 grants 权限

                //1问题 服务中可以申请权限么

                return;
            }
            //发送短信
//            Toast.makeText(, "", Toast.LENGTH_SHORT).show();
            System.out.println("发送短信");
            //发送请求 当 时间 距离变化的时候

            lm.requestLocationUpdates(bestProvider, 60000, 0, myLocationListener);
        }

    }

    class MyLocationListener implements LocationListener{

        //当地址改变的时候 发送短信
        @Override
        public void onLocationChanged(Location location) {
            System.out.println("地址改变");
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            System.out.println(longitude+"地址"+latitude);

            //发送短信(添加权限 发送短信 地理位置)
            SmsManager aDefault = SmsManager.getDefault();
            //destination 目的地 地址
            //sc 来源地址
            //delivery 传递 （发送 但是没有收到的intent）
            aDefault.sendTextMessage("5556",null,"long="+longitude+"laititude="+latitude,null,null);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
//            //发送短信(添加权限 发送短信 地理位置)
//            SmsManager aDefault = SmsManager.getDefault();
//            aDefault.sendTextMessage("5556",null,"long="+longitude+"laititude="+latitude,null,null);

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
