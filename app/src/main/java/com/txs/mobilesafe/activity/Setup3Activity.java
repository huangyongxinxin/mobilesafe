package com.txs.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.SpUtil;

public class Setup3Activity extends AppCompatActivity {

    public EditText et_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUi();

    }

    private void initUi() {
        et_phone = findViewById(R.id.et_phone_number);
        Button btn_phone = findViewById(R.id.btn_phone_number);
        String sp_phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        et_phone.setText(sp_phone);
        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击联系人按钮1.跳转页面
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            String phone = data.getStringExtra("phone");
            System.out.println("123"+data);
            String trim = phone.replace("-", "").replace(" ", "").trim();
            et_phone.setText(trim);
            //存储联系人电话号码 到sp
            SpUtil.putString(this, ConstantValue.CONTACT_PHONE,trim);

        }

    }

    public void nextPage(View view) {
        String phone = et_phone.getText().toString().trim();


//        String string = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");

        if (!TextUtils.isEmpty(phone)){

            Intent intent = new Intent(this, Setup4Activity.class);
            startActivity(intent);
            finish();
            SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);
        }else {
            Toast.makeText(this,"请输入电话号码",Toast.LENGTH_SHORT).show();
        }

        overridePendingTransition(R.anim.next_in_ainm,R.anim.next_out_anim);
    }

    public void prePage(View view) {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

}
