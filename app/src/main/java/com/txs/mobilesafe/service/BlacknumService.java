package com.txs.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;

import com.txs.mobilesafe.engine.BlackNumDao;

public class BlacknumService extends Service{

    public MySmsReceiver mySmsReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //在开启服务的时候 动态注册 广播接受者 对 sms进行监听

        System.out.println("开启短信拦截的服务");
        IntentFilter intentFilter = new IntentFilter();
        //接受到 短信的action
        intentFilter.addAction("android.provider.Telephony.SMS_REDEIVED");
        intentFilter.setPriority(1000);
        mySmsReceiver = new MySmsReceiver();
        registerReceiver(mySmsReceiver,intentFilter);
        System.out.println("注册广播");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mySmsReceiver);
    }

    private class MySmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //在收到短信后 对比 电话号码 是否在黑名单数据库中
            // 2如果在黑名单数据库中则 拦截 广播
            //3。短信内容 来电地址获取
            System.out.println("收到短信 广播");
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            //循环遍历短信 判断其中 是否有 关键字
//            System.out.println(pdus);
            for (Object o : pdus) {
                //通过 短信对象 创建 短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);
                //获取短信对象的基本信息
//                String originatingAddress = sms.getOriginatingAddress();
                //电话号码
                String messageBody = sms.getMessageBody();
                //判断是否包含 关键字
                String originatingAddress = sms.getOriginatingAddress();
                //对来电地址

                BlackNumDao bnDao = BlackNumDao.getInstance(context);
                int mode = bnDao.findMode(originatingAddress);
                if (mode==1||mode == 3){
                    abortBroadcast();//拦截 短信广播
                }


            }
        }
    }
}
