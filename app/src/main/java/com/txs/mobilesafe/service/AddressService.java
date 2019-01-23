package com.txs.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.engine.AddressDao;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.SpUtil;


public class AddressService extends Service {

    public PhoneStateListener phoneStateListener;
    public TelephonyManager telephonyManager;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    public WindowManager windowManager;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//         mTv_address.setText(mAddress);
            Toast.makeText(getApplicationContext(),"播出电话"+mCallout,Toast.LENGTH_SHORT).show();
        }
    };
    public String mAddress;
    public TextView mTv_address;
    public View toastview;
    public int color;
    public int[] ints;

    public long time_first = 0;
    public long time_second= 0;
    private float startX;
    private float startY;
    private int width;
    private int height;
    public AddressService.calloutReceiver calloutReceiver;
    private String mCallout;

    @Override
    public void onCreate() {
        super.onCreate();
        //第一次开启的时候管理 吐司的显示
        //先对电话监听 电话管理器
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phoneStateListener = new MyPhoneStateListener();
        System.out.println("开启服务");
        if (telephonyManager != null) {
            // @param events The telephony state(s) of interest to the listener,
            //     *               as a bitwise-OR combination of {@link PhoneStateListener}
            //     *               LISTEN_ flags.
            //interest兴趣   监听的对象
            telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
            System.out.println("开启电话监听");
        }
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        //通过广播监听 播出电话
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //动态注册一个广播接受者
        calloutReceiver = new calloutReceiver();
//        calloutReceiver.
        System.out.println("注册");
        registerReceiver(calloutReceiver, intentFilter);

    }


    class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    //空闲状态
                    if (toastview!=null&&windowManager!=null){
                        windowManager.removeView(toastview);
                    }
                    System.out.println("空闲状态");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    showToast(phoneNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("接听状态");
                    break;
            }
        }

        private void showToast(String phoneNumber) {
            Toast.makeText(getApplicationContext(),"地址:"+phoneNumber,Toast.LENGTH_SHORT).show();

            final WindowManager.LayoutParams params = mParams;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
//            params.windowAnimations = com.android.internal.R.style.Animation_Toast;
//            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.setTitle("Toast");
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            params.gravity = Gravity.START+Gravity.TOP;

            //重现 图片位置的坐标
            int location_x = SpUtil.getint(getApplicationContext(), ConstantValue.LOCATION_X, 0);
            int location_y = SpUtil.getint(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
            params.x=location_x;
            params.y=location_y;

            toastview = View.inflate(getApplicationContext(), R.layout.activity_toast, null);
            mTv_address = toastview.findViewById(R.id.tv_from_address);
//            initToastStyle();//得到color值
//
//            toastview.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        Toast.makeText(getApplicationContext(),"jjj",Toast.LENGTH_SHORT).show();
//                        startX = event.getRawX();
//                        startY = event.getRawY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Toast.makeText(getApplicationContext(),"jjj",Toast.LENGTH_SHORT).show();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Toast.makeText(getApplicationContext(),"jjj",Toast.LENGTH_SHORT).show();
//                        //抬起 存储 移动位置到 sp
//                        //                        Toast.makeText(getApplicationContext(),"jjj",Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                    return true;
//                }
//            });

            //触摸时间和 点击事件都无法做到监听
            ints = new int[]{
                    Color.rgb(255, 255, 0),  Color.rgb(255, 0, 255),  Color.rgb(0, 0, 0),
                    Color.rgb(125, 125, 0),  Color.rgb(255, 0, 215)};
            color = SpUtil.getint(getApplicationContext(), ConstantValue.STYLE, 1);


            //设置点击事件
            //没有响应
//            toastview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //双击 点击 两次点击的间隔事件小于500
//                    //当前时间戳
//                    System.out.println("点击事件");
//                    if (time_first != 0){
//                        time_second = System.currentTimeMillis();
//                        if (time_second - time_first<500){
//                            Toast.makeText(getApplicationContext(),"shuangji",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    time_first = System.currentTimeMillis();
//                    System.out.println(time_first+"ssss"+time_second);
//                }
//            });

            if (windowManager != null) {
                width = windowManager.getDefaultDisplay().getWidth();
                height = windowManager.getDefaultDisplay().getHeight();

            }

            System.out.println("color数字变为："+color);
            toastview.setBackgroundColor(ints[color]);
            windowManager.addView(toastview,params);


            //拿到 电话号码在数据库中作查询操作 显示地址
            queryAddress(phoneNumber);
        }
    }

    private void queryAddress(final String phoneNumber) {
        //在数据库中查询 耗时操作
        new Thread(){
            @Override
            public void run() {
                //
                System.out.println(phoneNumber);
                mAddress = AddressDao.getAddress(phoneNumber);
                mHandler.sendEmptyMessage(0);
                System.out.println(mAddress);
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭服务的同时关闭监听
        if (phoneStateListener!=null&&telephonyManager!=null){
            telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_NONE);
        }
        unregisterReceiver(calloutReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class calloutReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String resultData = getResultData();
            queryAddressOut(resultData);
            System.out.println("收到播出电话的"+resultData);
            //拨打电话 之后 显示 toast
//            Toast.makeText(getApplicationContext(),"播出电话"+mCallout,Toast.LENGTH_SHORT).show();
        }
    }

    private void queryAddressOut(final String resultData) {
        //数据库查询
        final String[] address = new String[1];
//        final int[] i = new int[1];
        new Thread(){
            @Override
            public void run() {
                mCallout = AddressDao.getAddress(resultData);
                System.out.println(mCallout);
                mHandler.sendEmptyMessage(1);
            }
        }.start();
    }
}
