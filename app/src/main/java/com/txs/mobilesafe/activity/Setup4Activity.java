package com.txs.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.SpUtil;

public class Setup4Activity extends AppCompatActivity {

    public CheckBox cb_box;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        initUi();
    }

    private void initUi() {
        cb_box = findViewById(R.id.cb_setup);
        boolean open_security = SpUtil.getBollean(this, ConstantValue.OPEN_SECURITY, false);
        //根据状态改变现实
        if (open_security){
            cb_box.setText("安全设置已开启");
        }else{
            cb_box.setText("安全设置已关闭");
        }
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtil.putBollean(getApplicationContext(),ConstantValue.OPEN_SECURITY,isChecked);
                if (isChecked){
                    cb_box.setText("安全设置已开启");
                }else{
                    cb_box.setText("安全设置已关闭");
                }

            }
        });
    }

    public void nextPage(View view) {
        boolean setup_over = SpUtil.getBollean(this, ConstantValue.OPEN_SECURITY, false);
        if (setup_over){

            Intent intent = new Intent(this, SetupOverActivity.class);
            startActivity(intent);
            finish();
            //传入一个 是否设置成功的标识
            SpUtil.putBollean(this, ConstantValue.SETUP_OVER,setup_over);
        }else{
            Toast.makeText(this,"勾选",Toast.LENGTH_SHORT).show();
        }
        overridePendingTransition(R.anim.next_in_ainm,R.anim.next_out_anim);


    }

    public void prePage(View view) {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

}
