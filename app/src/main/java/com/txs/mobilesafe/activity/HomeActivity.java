package com.txs.mobilesafe.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.receiver.SmsReceiver;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.Md5Util;
import com.txs.mobilesafe.utils.SpUtil;

public class HomeActivity extends AppCompatActivity{

    public String[] mTitleStrs;
    public int[] mDrawableIds;
    public GridView gv;
    public String MOBLE_SAFE_PSD = "password";
    public SmsReceiver smsReceiver;
    //    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //动态注册 不能在 关闭 程序之后实现
//        静态注册 无法收到短信 广播
//        smsReceiver = new SmsReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
//        HomeActivity.this.registerReceiver(smsReceiver,intentFilter);

        if (ActivityCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.RECEIVE_SMS)
                        !=PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                        !=PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)
                        !=PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.SEND_SMS)
                        !=PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.READ_PHONE_STATE)
                        !=PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.PROCESS_OUTGOING_CALLS)
                        !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_PHONE_STATE,Manifest.permission.PROCESS_OUTGOING_CALLS
                    },
                    1);
        }else{
            Toast.makeText(this, "所有权限都已加载", Toast.LENGTH_SHORT).show();
            //动态申请权限
        }
        //查询权限，包含了接收短信READ_PHONE_STATE
//        queryAuthority();
        
//        requestPermission();

        requestDrawOverLays();
        initUi();
        initData();
    }

    //参考自http://stackoverflow.com/questions/32061934/permission-from-manifest-doesnt-work-in-android-6
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @TargetApi(Build.VERSION_CODES.M)
    public void requestDrawOverLays() {
        if (!Settings.canDrawOverlays(HomeActivity.this)) {
            Toast.makeText(this, "can not DrawOverlays", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + HomeActivity.this.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            // Already hold the SYSTEM_ALERT_WINDOW permission, do addview or something.
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission not granted...
                Toast.makeText(this, "Permission Denieddd by user.Please Check it in Settings", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Allowed", Toast.LENGTH_SHORT).show();
                // Already hold the SYSTEM_ALERT_WINDOW permission, do addview or something.
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(smsReceiver);
    }
//    private void queryAuthority() {
//        int hasReadContactsPermission = 0;
//        //Android Marshmallow
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            hasReadContactsPermission = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
//        }
//
//        if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                请求授权对话框
//                requestPermissions(new String[] {Manifest.permission.RECEIVE_SMS},
//                        REQUEST_CODE_ASK_PERMISSIONS);
//            }
//            return;
//        }
//        //执行查询操作
////        registerReceiver();
//    }
//
//    private void requestPermission() {
//
//        //运行时异常
//        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.RECEIVE_SMS);
//
//        //已经写入的权限  包管理 中的 权限 如果不等
//        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
//          //如果有权限 啥都不敢
//        } else {
//            //如果获取了权限就直接调用call（）方法拨打电话
////                        call();
//            System.out.println("不相等");
//
//            //如果没有拿到权限 先调用 请求权限的方法
//            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
//        }
//    }

    private void initData() {
        //图片加文字两个数组
        mTitleStrs = new String[]{"手机防盗", "通信卫士", "软件管理", "进程管理", "凌晨统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        mDrawableIds = new int[]{R.drawable.timg, R.drawable.timg, R.drawable.timg, R.drawable.timg, R.drawable.timg, R.drawable.timg, R.drawable.timg, R.drawable.timg, R.drawable.timg,};
        gv.setAdapter(new MyAdapter());
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(i+""+l);

                switch (i){
                    case 0:
                        showDialog();
                        break;
                    case 1:
                        //进入通信卫士
                        Intent blackNumberActivity = new Intent(getApplicationContext(), BlackNumberActivity.class);
                        startActivity(blackNumberActivity);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        Intent Atool = new Intent(getApplicationContext(), AToolsActivity.class);
                        startActivity(Atool);

                        break;
                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
    }

    private void showDialog() {
        //1.设置初始密码 对话框
        //2
        String psd = SpUtil.getString(this, MOBLE_SAFE_PSD, "");
        if (TextUtils.isEmpty(psd)){
            //设置初始化密码
            showSetPsdDiaLog();
        }else {
            //确认对话框
            showConfirmPsdDiaLog();
        }
//        showSetPsdDiaLog();
    }

    private void showConfirmPsdDiaLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        View inflate = View.inflate(this, R.layout.dialog_confirm, null);
        builder.setView(inflate);
        final AlertDialog dialog = builder.show();

        final EditText password = inflate.findViewById(R.id.confirm_psd);
        Button btn_close = inflate.findViewById(R.id.btn_close);
        Button btn_set = inflate.findViewById(R.id.btn_set);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String psd = SpUtil.getString(getApplicationContext(), ConstantValue.PASSWORD, "");

                String s =Md5Util.encoder( password.getText().toString().trim());
                if (!TextUtils.isEmpty(s)){
                    if (psd.equals(s)){
                        //如果密码正确 进入
                        Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(),"密码不正确",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void showSetPsdDiaLog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View inflate = View.inflate(this, R.layout.dialog_set, null);
        builder.setView(inflate);
        //兼容低版本 设置内边距
//        builder.setView()
        final AlertDialog dialog = builder.show();

        Button btn_close = inflate.findViewById(R.id.btn_close);
        Button btn_set = inflate.findViewById(R.id.btn_set);

        final EditText confirm_id = inflate.findViewById(R.id.confirm_id);
        final EditText set_id = inflate.findViewById(R.id.set_id);
        //NullPointerException 空指针异常 什么为空了？ 没有拿到布局文件的控件
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消 关闭
                dialog.dismiss();
            }
        });

        //点击之后闪退
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击确定 将 数据存放
                String psd = set_id.getText().toString();
                String confirmpsd = confirm_id.getText().toString();
                if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmpsd)){
                    //密码非空 且相等
                    if (psd.equals(confirmpsd)){
                        SpUtil.putString(getApplicationContext(),ConstantValue.PASSWORD, Md5Util.encoder(psd));
                        //加密
                        //进入 界面
                        Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(),"密码不相等",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mTitleStrs.length;
        }

        @Override
        public Object getItem(int i) {
            return mTitleStrs[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View item_view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            ImageView gv_home = item_view.findViewById(R.id.iv_icon);
            TextView tv_title = item_view.findViewById(R.id.tv_title);
            tv_title.setText(mTitleStrs[i]);
            gv_home.setBackgroundResource(mDrawableIds[i]);
            return item_view;
        }
    }

    private void initUi() {
        gv = findViewById(R.id.gv_home);

    }

}
