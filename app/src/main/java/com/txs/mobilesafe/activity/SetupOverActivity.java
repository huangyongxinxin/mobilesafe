package com.txs.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.SpUtil;

public class SetupOverActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setop_over = SpUtil.getBollean(this, ConstantValue.SETUP_OVER, false);

        if (setop_over){
            //如果设置成功
            setContentView(R.layout.activity_setup_over);
        }else {
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
            finish();
        }

        initUi();
    }

    private void initUi() {
        String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        TextView tv_phonesafe = findViewById(R.id.tv_phonesafe);
        tv_phonesafe.setText(phone);


    }
}
