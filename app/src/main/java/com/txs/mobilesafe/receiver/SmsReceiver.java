package com.txs.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.SpUtil;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "收到信息", Toast.LENGTH_SHORT).show();
        System.out.println("onReceive");
        //1.判断是否开启 防盗保护
        boolean open_security = SpUtil.getBollean(context ,ConstantValue.OPEN_SECURITY, false);
        //2.获取短信内容
        System.out.println(open_security);

        if (open_security){
            //获取短信 中 的关键字
            //通过intent 拿到 对象数组
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            //循环遍历短信 判断其中 是否有 关键字
            System.out.println(pdus);
            for (Object o :
                    pdus) {
                //通过 短信对象 创建 短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);
                //获取短信对象的基本信息
//                String originatingAddress = sms.getOriginatingAddress();
                //电话号码
                String messageBody = sms.getMessageBody();
                //判断是否包含 关键字
                boolean alarm = messageBody.contains("/*alarm*/");
                if (alarm){
                    //播放音乐（准备音乐
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);//一直循环播放
                    mediaPlayer.start();
                }
                boolean location = messageBody.contains("/*location*/");
                if (location){
                    //拿到 手机 的地址 1.定位方式有三种
                    //1基站2.网络3.卫星定位
                    //网络：ip地址对应物理地址  不精确
                    //偏振坐标

                    //开启服务 不和activity绑定，为了关闭应用之后 收到短信后
                    Intent intent1 = new Intent(context, LocationService.class);
                    context.startService(intent1);
                }
                boolean lock = messageBody.contains("/*lock*/");
                if (lock){
                //锁屏
                    System.out.println("todo");
                }
                boolean wipe = messageBody.contains("/*wipe*/");
                if (wipe){
                    //清楚数据
                    //                    System.out.println("todo");
                }
            }
        }
    }
}
