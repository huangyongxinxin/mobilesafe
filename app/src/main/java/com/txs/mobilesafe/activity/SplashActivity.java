package com.txs.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.com.txs.mobilesafe.util.StreamUtil;
import com.txs.mobilesafe.utils.ConstantValue;
import com.txs.mobilesafe.utils.SpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public  class SplashActivity extends AppCompatActivity {

    private static final int UPDATE = 100;
    private static final int FAULT = 101;
    private static final int ENTER_HOME = 102;
    private static final int ALL_ERROR = 400;
    public TextView tv_version;
    public String tag = "Splash";
    public int localVersionCode;

//    class teset{
////        static  int hh;
//        //inner classes can not have static declaration
//    }
//
//    //静态方法 访问内部类 只能访问静态内部类
//    static void test(){
////        new teset();//静态方法不能访问 非静态 类
//        new mHandler();
//    }

    //使用弱引用
    //只有内部类 才能声明为静态类2.静态成员变量只能存在于 静态内部类
    //3.只能访问外部类的静态成员
    //4.外部类的静态方法 只能访问静态内部类
    static private class mHandler extends Handler{
        //操作Activity中的对象
        //不再持有外部类对象的引用

        //静态内部类 得到外部类的引用
        WeakReference<Activity> mActivityReferences;

        public mHandler(Activity activity) {
            mActivityReferences = new WeakReference<Activity>(activity);
        }

       @Override
        public void handleMessage(Message msg) {
            //得到外部activity
           final Activity activity = mActivityReferences.get();
           switch (msg.what){
                case UPDATE:
                    //静态内部类 访问 外部类 （3）
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }


    }

    private Handler myHandler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE:
                    //
                    choiceupdate();
                case ALL_ERROR:
                    Toast.makeText(getApplicationContext(),"出错",Toast.LENGTH_SHORT).show();
                    enterHomew();
                    break;
                case FAULT:
                    Toast.makeText(getApplicationContext(),"出错",Toast.LENGTH_SHORT).show();
                    enterHomew();
                    break;
                case ENTER_HOME:
                    enterHomew();
                    break;
            }

        }
    };

    private void choiceupdate() {
        //
        //如果需要更新 则 弹出对话框
//                            AlertDialog alertDialog = new AlertDialog();
//                            AlertDialog.Builder(this)
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
        builder.setTitle("标题");
        builder.setNegativeButton("negativve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //进入主界面
                enterHomew();
            }
        });
        builder.setPositiveButton("postive", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //更新
                download();
            }
        });

    }


    private void download() {
        enterHomew();
    }
    private void enterHomew() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //拿到 版本号

        //1.packagemanager

        initDatabase();
        //初始化UI
        initUi();
        //2.初始化 数据
        initData();
        //初始化动画
//        initAnimation();

    }

    private void initDatabase() {
        //将 第三方文件中的db文件 导入 程序中
        String dbname = "address.db";
        // 1files
        initFile(dbname);
        // 2sdk
        // 3cache
    }

    private void initFile(String dbName) {
        // 拿到files地址
        File filesDir = getFilesDir();
        //创建一个 制定文件名 和路径的 file文件
        File file = new File(filesDir, dbName);
        //如果 存在 直接返回
        if (file.exists()){
            return;
        }
            //如果不存在file文件1.将 assets中 db文件 读取出来 输出到file中
            InputStream is = null;
            FileOutputStream fileOutputStream = null;
            try {
                 is = getAssets().open(dbName);
                 fileOutputStream = new FileOutputStream(file);//输出流 通过 file指定
                byte[] bytes = new byte[1024];
                int temp = -1;
                while ((temp=is.read(bytes))!=-1){
                    //将文件
                    fileOutputStream.write(bytes,0,temp);
                }
                System.out.println("创建file db 成功");
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                //如果 输入流和输出流 不为空 则将其关闭
                if (is!=null&&fileOutputStream!=null){
                    try {
                        System.out.println("关闭流");
                        is.close();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

    }

    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        View splash = findViewById(R.id.splash);
        splash.startAnimation(alphaAnimation);

    }

    //    获取数据方法
    private void initData() {
//        getVersionCode();
        localVersionCode= getVersionCode();
        tv_version.setText(getVersionName());
        //对比版本号确定是否更新
        if (SpUtil.getBollean(this, ConstantValue.OPEN_UPDATE,false)){
            boolean b = myHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
//            checkServiceVersionCode();
        }else {
            //主线程 不能 睡眠
//            try {
//                Thread.sleep(4000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            boolean b = myHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
        }

    }


    private void checkServiceVersionCode() {
        //新建子线程 从服务器拿到 最新的版本号
        //比对版本号
        new Thread(new Runnable() {

            @Override
            public void run() {
                final Message msg = Message.obtain();

                //封装一个url 打开 httpconnectionurl 拿到 read流 写入 write流
                try {

                    URL url = new URL("192.168.10.197//test.json");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("GET");
                    if (urlConnection.getResponseCode()==200){
                        Log.i(tag,"连接成功");
                        //拿到 数据流
                        InputStream inputStream = urlConnection.getInputStream();
                        //缓存
                        //将 拿到的输入流 转变成一个字符串对象
                        //自定义一个 工具类 StreamUtil工具类
                        String json = StreamUtil.tostring(inputStream);
                        Log.i(tag,json);
                        JSONObject jsonObject = new JSONObject(json);
                        int versionCode = jsonObject.getInt("versionCode");
                        if (versionCode>localVersionCode){
                            msg.what=UPDATE;
                        }else {
                            msg.what = ENTER_HOME;
                        }

                    }
                } catch (MalformedURLException e) {
                    msg.what=ALL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what=ALL_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what=ALL_ERROR;
                    e.printStackTrace();
                }finally {
                    myHandler.handleMessage(msg);
                }
            }
        }).start();
    }

    //获取本地版本名称
    //@return 应用版本名称 返回null代表演唱
    private String getVersionName() {
        //获取包管理者对象
        PackageManager pm = getPackageManager();
        //2.通过pm拿到 对应的包 以及包下 内容
        try {
            //从包包管理者 中获取 指定报名 的基本信息（版本名称，版本号)
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
//            int versionCode = packageInfo.versionCode;
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private int getVersionCode() {
        //拿到清单文件中的版本号1.通过 上下文直接拿到 包管理者
        PackageManager pm = getPackageManager();
        //2.通过pm拿到 对应的包 以及包下 内容
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
//            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            return versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private void initUi() {
        tv_version = findViewById(R.id.tv_name);
        //=
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
    }
}
