package com.txs.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.SpUtil;

public class ToastLocationActivity extends Activity{

    public int width;
    public int height;
    public long time_first = 0;
    public long time_second;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aactivty_toast_location);
        initUi();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUi() {
        final ImageView iv_drag = findViewById(R.id.iv_drag);
        final Button btn_up = findViewById(R.id.btn_up);
        final Button btn_down = findViewById(R.id.btn_down);
        int locationx = SpUtil.getint(this, ConstantValue.LOCATION_X, 0);
        int locationy = SpUtil.getint(this, ConstantValue.LOCATION_Y, 0);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.leftMargin = locationx;
        layoutParams.topMargin = locationy;
        //将规则 作用在 iv_drag上
        iv_drag.setLayoutParams(layoutParams);


        //如果位置在下面 up 显示
        if (locationy>height/2){
            btn_up.setVisibility(View.VISIBLE);//上按钮 显示
            btn_down.setVisibility(View.INVISIBLE);//下按钮不显示
        }else {
            btn_up.setVisibility(View.VISIBLE);//上按钮 不显示
            btn_down.setVisibility(View.INVISIBLE);//下按钮 显示

        }

        //窗体管理者 对象 获得屏幕 宽高
        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (wm != null) {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();

        }


        iv_drag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //双击
                //双击 点击 两次点击的间隔事件小于500
                //当前时间戳

                System.out.println("点击事件");
                if (time_first != 0){
                    time_second = System.currentTimeMillis();
                    if (time_second - time_first<500){
                        Toast.makeText(getApplicationContext(),"shuangji",Toast.LENGTH_SHORT).show();
                        //居中

                        int left = (wm.getDefaultDisplay().getWidth()/2-iv_drag.getWidth()/2);//控件与左边缘的距离
                        int top =  (wm.getDefaultDisplay().getHeight()/2-iv_drag.getHeight()/2);//控件与上边缘的距离
                        int right =  (wm.getDefaultDisplay().getWidth()/2+iv_drag.getWidth()/2);
                        int bottom =  (wm.getDefaultDisplay().getHeight()/2+iv_drag.getHeight()/2);

                        iv_drag.layout(left,top,right,bottom);

                        SpUtil.putint(getApplicationContext(), ConstantValue.LOCATION_X,iv_drag.getLeft());
                        SpUtil.putint(getApplicationContext(), ConstantValue.LOCATION_Y,iv_drag.getTop());
                    }
                }
                time_first = System.currentTimeMillis();

                System.out.println(time_first+"ssss"+time_second);
            }
        });

        iv_drag.setOnTouchListener(new View.OnTouchListener() {

            public float startY;
            public float startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float endX = event.getRawX();
                        float endY = event.getRawY();
                        float moveX = endX - startX;
                        float moveY = endY - startY;
                        //拿到屏幕左上角位置
                        int left = (int) (iv_drag.getLeft()+moveX);//控件与左边缘的距离
                        int top = (int) (iv_drag.getTop()+moveY);//控件与上边缘的距离
                        int right = (int) (iv_drag.getRight()+moveX);
                        int bottom = (int) (iv_drag.getBottom()+moveY);

                        //容错处理 控件不能 拖拽到 屏幕外
//                        iv_drag.getLeft()

                        if (left<0){
                            return true;
                        }
                        if (right>width){
                            return true;
                        }
                        if (top<0){
                            return true;
                        }
                        if (bottom>height){
                            return true;
                        }
                        if (top>height/2){
                            btn_up.setVisibility(View.VISIBLE);
                            btn_down.setVisibility(View.INVISIBLE);
                        }
                        if (top<height/2){
                            btn_down.setVisibility(View.VISIBLE);
                            btn_up.setVisibility(View.INVISIBLE);
                        }

                        iv_drag.layout(left,top,right,bottom);

                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起 存储 移动位置到 sp
                        SpUtil.putint(getApplicationContext(), ConstantValue.LOCATION_X,iv_drag.getLeft());
                        SpUtil.putint(getApplicationContext(), ConstantValue.LOCATION_Y,iv_drag.getTop());
                        break;
                }
                return false;//false不响应 事件
            }
        });
    }
}
