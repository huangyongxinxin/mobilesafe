package com.txs.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.SpUtil;

public class RocketService extends Service {
    public WindowManager windowManager;
    public View rocket;
    private float startX;
    private float startY;
    public ImageView rocket1;

    Handler mHandler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            params.y = msg.what;
            //让窗体管理者更新ui
            windowManager.updateViewLayout(rocket,params);
        }
    };
    public WindowManager.LayoutParams params;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //自定义吐司 挂在 窗体上
        //1
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        int window_height = windowManager.getDefaultDisplay().getHeight();
        int window_width = windowManager.getDefaultDisplay().getWidth();
        //开启火箭 （toast）背景为火箭图片
        lunchRocket();

        super.onCreate();
    }

    private void lunchRocket() {
        //开启一个自定义 toast 流程

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
//            params.windowAnimations = com.android.internal.R.style.Animation_Toast;
//            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.type =WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity = Gravity.START+Gravity.TOP;
        //1配置视图参数

        //2.定义所在的布局
        rocket = View.inflate(this, R.layout.activity_rocket, null);
        rocket1 = this.rocket.findViewById(R.id.iv_rocket);
        final AnimationDrawable background = (AnimationDrawable) rocket1.getBackground();
        background.start();//开启动画
        //3.挂在 窗体上
        windowManager.addView(this.rocket, params);
        //设置touch监听 !!!!!!!!!!!!!!!!!!!!!!!
//        windowManager加载的View对象需要 设置 焦点
        rocket.setFocusable(true);
        rocket.setFocusableInTouchMode(true);
        rocket.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("klfjlkdslfkdsjf");
                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float endX = event.getRawX();
                        float endY = event.getRawY();
                        float moveX = endX - startX;
                        float moveY = endY - startY;
                        //容错处理 控件不能 拖拽到 屏幕外
//                        iv_drag.getLeft()
                        params.x= (int) (params.x+moveX);
                        params.y= (int) (params.y+moveY);
//                        rocket.layout(left,top,right,bottom);
                        //容错处理
//                        if(params.x<0||params.y<0){
////                            return true;
//                        }
//                        if (params.x>windowManager.getDefaultDisplay().getWidth()||params.y>windowManager.getDefaultDisplay().getHeight()){
////                            return true;
//                        }

                        windowManager.updateViewLayout(rocket, params);
                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                            sendRocket();
                            //背景烟雾动画

                        Intent intent = new Intent(getApplicationContext(), BackgroundActivity.class);
                        //在服务中开启一个activity需要设置flags
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在一个新 的栈中创建activity
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    private void sendRocket() {
        //发射火箭 将 y 递减
        //在主线程不能睡眠
        new Thread(){
            @Override
            public void run() {
                for (int i=0;i<81;i++){
                    int windowh = windowManager.getDefaultDisplay().getHeight();
                    int heights = windowh / 80;
                    int height = windowh - i*heights;
                    //更新ui 使用handler
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message m = Message.obtain();
                    m.what = height;
                    mHandler.sendMessage(m);

                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        if (windowManager!=null&&rocket!=null){
            windowManager.removeView(rocket);
        }
        super.onDestroy();
    }
}
