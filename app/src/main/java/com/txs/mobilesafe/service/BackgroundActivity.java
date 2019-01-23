package com.txs.mobilesafe.service;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.txs.mobilesafe.R;

public class BackgroundActivity extends Activity {
    private   Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //关闭动画 关闭activity
            finish();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_bg);
        //动画1.控件
        ImageView bg = findViewById(R.id.iv_rocket_bg);
        ImageView bgup = findViewById(R.id.iv_rocket_bgup);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        bg.startAnimation(alphaAnimation);
        bgup.startAnimation(alphaAnimation);
        //关闭 动画 消息机制 延迟处理
        mHandler.sendEmptyMessageDelayed(0,1000);
    }
}
