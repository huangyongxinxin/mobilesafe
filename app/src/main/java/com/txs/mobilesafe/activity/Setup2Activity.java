package com.txs.mobilesafe.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.SpUtil;

public class Setup2Activity extends AppCompatActivity{

    public TelephonyManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUi();
    }

    private void initUi() {
        final SettingItemView sim_bound = findViewById(R.id.siv_sim_bound);
        //读显

        String sim = SpUtil.getString(this, ConstantValue.SIM_BOUND, "");
        System.out.println("1"+TextUtils.isEmpty(sim));
        System.out.println("2"+(sim == ""));
        System.out.println("sim卡的序列号："+sim);
        if (TextUtils.isEmpty(sim)){
            //没帮
            sim_bound.setCheck(false);
        }else {
            sim_bound.setCheck(true);
        }

        sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //状态取反 1获取原有状态
                boolean check = sim_bound.isCheck();//sim为局部变量 final
                sim_bound.setCheck(!check);

                //权限  what？
                if (!check){
                    //1拿到sim
                    System.out.println("储存 序列卡号");
                    manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


                    //运行时异常
                    int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_PHONE_STATE);

                    //已经写入的权限  包管理 中的 权限 如果不等
                    if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                        //调用ActivityCompat.requestPermissions（）方法获取权限
                        //param1：当前环境   param2：通过string数组来添加我们想要获取的权限 param3：请求码

                        if (manager != null) {
                            @SuppressLint("MissingPermission") String simSerialNumber = manager.getSimSerialNumber();

                            SpUtil.putString(getApplicationContext(),ConstantValue.SIM_BOUND,simSerialNumber);
                        }

                        System.out.println("相等 存入 序列号");
                    } else {
                        //如果获取了权限就直接调用call（）方法拨打电话
//                        call();
                        System.out.println("不相等");

                        //如果没有拿到权限 先调用 请求权限的方法
                        ActivityCompat.requestPermissions(Setup2Activity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                        if (manager != null) {
                            @SuppressLint("MissingPermission") String simSerialNumber = manager.getSimSerialNumber();

                            SpUtil.putString(getApplicationContext(),ConstantValue.SIM_BOUND,simSerialNumber);
                        }
                    }

                }else {
                    System.out.println("删除 序列卡号");
                    SpUtil.remove(getApplicationContext(),ConstantValue.SIM_BOUND);
                }
            }
        });

    }

    public void nextPage(View view) {
        if(!SpUtil.getString(this,ConstantValue.SIM_BOUND,"").isEmpty()){
            //sp中有sim
            Intent intent = new Intent(this, Setup3Activity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this,"绑定sim卡",Toast.LENGTH_SHORT).show();
        }
        overridePendingTransition(R.anim.next_in_ainm,R.anim.next_out_anim);
    }

    public void prePage(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == CODE_FOR_WRITE_PERMISSION){
//            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    &&grantResul用户同意ts[0] == PackageManager.PERMISSION_GRANTED){
//                //
//            } else {
//                // 用户不同意，向用户展示该权限作用
//                if (!shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    showPermissionDialog();return;
//                }
//            }
//        }
//    }

}
