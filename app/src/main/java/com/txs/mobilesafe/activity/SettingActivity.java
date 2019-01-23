package com.txs.mobilesafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.service.AddressService;
import com.txs.mobilesafe.service.BlacknumService;
import com.txs.mobilesafe.service.RocketService;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.ServiceUtil;
import com.txs.mobilesafe.utils.SpUtil;
import com.txs.mobilesafe.view.SettingClickView;

public class SettingActivity  extends AppCompatActivity {

    public String OPEN_UPDATE;
    public String[] mstyles;
    public int num;
    public SettingClickView scv_toast1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initToastStyle();

        initLocation();

        initUpdate();
        initAddress();

        initBlacknum();

    }

    private void initBlacknum() {
        final SettingItemView siv_blacknum = findViewById(R.id.setting_blacknum);
//        boolean open_address = SpUtil.getBollean(this, ConstantValue.OPEN_Address, false);

        boolean running = ServiceUtil.isRunning(this, "com.txs.mobilesafe.service.BlacknumService");
//        System.out.println(running);
        siv_blacknum.setCheck(running);

        siv_blacknum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = siv_blacknum.isCheck();
                siv_blacknum.setCheck(!check);
                Intent blacknumService;
                if (!check){
                    //开启服务
                    blacknumService = new Intent(getApplicationContext(), BlacknumService.class);
                    startService(blacknumService);
                }else {
                    //关闭服务
                    blacknumService = new Intent(getApplicationContext(), BlacknumService.class);
                    stopService(blacknumService);
                }
            }
        });
    }

    private void initLocation() {
        SettingClickView scv_location = findViewById(R.id.scv_activity_view);
        scv_location.setTv_title("归属地提示框的位置");
        scv_location.setTv_describtion("设置归属地提示框的位置");
        scv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tl_activity = new Intent(getApplicationContext(), ToastLocationActivity.class);
                startActivity(tl_activity);
            }
        });
    }

    private void initToastStyle() {
        scv_toast1 = findViewById(R.id.scv_toast_view);
        scv_toast1.setTv_title("设置归属地显示风格");
        //创建描述文字所在的String类型数组
        mstyles = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        //通过sp 储存索引
        num = SpUtil.getint(this, ConstantValue.STYLE, 0);
        scv_toast1.setTv_describtion(mstyles[num]);

        scv_toast1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                num = SpUtil.getint(getApplicationContext(), ConstantValue.STYLE, 0);
                showToastDialog();
            }
        });
    }

    //显示对话框
    private void showToastDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setTitle("请选择样式");
        //参数1 ：String 类型数组2。弹出对话框的时候选中的索引值，3.点击某一个条目的 点击事件
        builder.setSingleChoiceItems(mstyles, num, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SpUtil.putint(getApplicationContext(),ConstantValue.STYLE,which);
                //关闭dialog
                dialog.dismiss();
                scv_toast1.setTv_describtion(mstyles[which]);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void initAddress() {
        final SettingItemView siv_address = findViewById(R.id.setting_address);
//        boolean open_address = SpUtil.getBollean(this, ConstantValue.OPEN_Address, false);

        boolean running = ServiceUtil.isRunning(this, "com.txs.mobilesafe.service.AddressService");


        System.out.println(running);
        siv_address.setCheck(running);

        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = siv_address.isCheck();
                siv_address.setCheck(!check);
//                SpUtil.putBollean(getApplicationContext(),ConstantValue.OPEN_Address,!check);
                //如果点击之后变为真
                Intent addressService;
                if (!check){
                    //开启服务
                    addressService = new Intent(getApplicationContext(), AddressService.class);
                    Intent intent = new Intent(getApplicationContext(), RocketService.class);
                    startService(intent);
                    startService(addressService);
                }else {
                    //关闭服务
                    addressService = new Intent(getApplicationContext(), AddressService.class);
                    stopService(addressService);
                }
            }
        });
    }

    private void initUpdate() {
        final SettingItemView stv = findViewById(R.id.setting_update);
        //获取已有的状态
        OPEN_UPDATE = "open_dupdate";
        boolean open_dupdate = SpUtil.getBollean(this, ConstantValue.OPEN_UPDATE, false);
        //是否存储 根据上次 的结果
        stv.setCheck(open_dupdate);


        stv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击之后改变状态
                boolean check = stv.isCheck();
                //将原有状态去翻
                stv.setCheck(!check);
                SpUtil.putBollean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!check);
            }
        });
    }

}
