package com.txs.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.engine.AddressDao;

public class QueryAddressActivity  extends Activity{

    public EditText phonenum;
    public String mAdress;

    //handler  应该被定义为静态的 为了避免 内存泄漏 ？ 为什么静态的可以避免内存泄漏
    private Handler mH = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            queryResult.setText(mAdress);
        }
    };
    public TextView queryResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queryaddress);
        initUi();
    }

    private void initUi() {
        phonenum = findViewById(R.id.et_query);
        queryResult = findViewById(R.id.tv_query_result);
        Button btn_query = findViewById(R.id.btn_query);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询 数据库 中 的数据属于耗时操作
                String s = phonenum.getText().toString();
                if (!TextUtils.isEmpty(s)){
                    queryAdress();
                }else {
                    //抖动动画
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
//                    animation.setInterpolator(new Interpolator() {
//                        @Override
//                        public float getInterpolation(float input) {
//                            return 0;
//                        }
//                    });
                    phonenum.startAnimation(animation);

                    Vibrator vibarator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibarator.vibrate(2000);

                }
            }
        });
        phonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("text改变之前");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("text改变之中");
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("text改变之后 做查询操作");
                queryAdress();
            }
        });
    }

    private void queryAdress() {
        new Thread(){
            @Override
            public void run() {
                String phonenumText = phonenum.getText().toString();
                mAdress = AddressDao.getAddress(phonenumText);
                mH.sendEmptyMessage(0);
            }
        }.start();
    }
}
