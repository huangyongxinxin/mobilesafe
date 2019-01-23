package com.txs.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.domain.BlacknumInfo;
import com.txs.mobilesafe.engine.BlackNumDao;

import java.util.ArrayList;
import java.util.List;

public class BlackNumberActivity extends Activity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //更新adapter
            if (myAdapter==null){
                myAdapter = new MyAdapter();
                mlv_black.setAdapter(myAdapter);
            }else {
                myAdapter.notifyDataSetChanged();
            }
        }
    };
    public ListView mlv_black;
    public Button mbtn_add;
    public ArrayList<BlacknumInfo> listBN;
    private int mode = 1;
    public BlackNumDao blackNumDao;
    public MyAdapter myAdapter;
    private boolean isload = false;
    public int count;

    private class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return listBN.size();
        }

        @Override
        public Object getItem(int position) {
            return listBN.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //使用 convertview 优化
//            TextView item_phone = null;TextView item_mode = null;ImageView iv_black_delete = null;
            ViewHolder viewHolder = null;
            if (convertView==null){
                //之前没有 可以复用的对象
                //穿件一个 新的view对象
                convertView = View.inflate(getApplicationContext(), R.layout.activity_blacknumber_item, null);

                //对findbyviewid进行优化  使用viewholder 减少findviewbyid的次数

                 viewHolder = new ViewHolder();
                viewHolder.phone = convertView.findViewById(R.id.tv_item_phone);
                viewHolder.mode = convertView.findViewById(R.id.tv_item_mode);
                viewHolder.img = convertView.findViewById(R.id.iv_black);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.phone.setText(listBN.get(position).phone);
            viewHolder.mode.setText(listBN.get(position).mode);
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击删除 数据库中内容 更新list 通知 adapter
                    blackNumDao.delete(listBN.get(position).phone);
                    listBN.remove(position);
                    if (myAdapter!=null){
                        myAdapter.notifyDataSetChanged();
                    }
                }
            });

            //在数据适配器中 判断拿到的值赋给 textview不同的值
            return convertView;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacknumber);
        initUi();
        initData();
    }

    private void initData() {
//        blackNumDao = new BlackNumDao(this);
         blackNumDao = BlackNumDao.getInstance(this);
//         for (int i=0;i<50;i++){
//        blackNumDao.insert("119"+i,"1");
//         }
        //得到 list集合
        //第一次查询 加载20条
        listBN = blackNumDao.findlimit(0);
        count = blackNumDao.getCount();
        System.out.println(listBN.size()+"刚进入只加载20条");
        //handler
        handler.sendEmptyMessage(1);


    }

    private void initUi() {
        mbtn_add = findViewById(R.id.btn_add);
        mlv_black = findViewById(R.id.lv_blacknum);

        mbtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹窗
                showDialog();
            }
        });

        mlv_black.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                    case SCROLL_STATE_IDLE:
                        //分页查询
                        //空闲状
                        if(!listBN.isEmpty()){
                            if (mlv_black.getLastVisiblePosition()>=listBN.size()-1&&!isload){
                                //isload放置重复加载

                                //判断数据是否有更多数据
                                if (count > listBN.size()){
                                    //耗时操作且需要更新ui
                                    new Thread(){
                                        @Override
                                        public void run() {
                                            //加载下一页数据
                                            ArrayList<BlacknumInfo> findlimit = blackNumDao.findlimit(listBN.size());
                                            listBN.addAll(findlimit);
                                            handler.sendEmptyMessage(0);
                                            System.out.println("最后一个可见位置 从最后一个位置开始加载"+findlimit);
                                        }
                                    }.start();
                                }

                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View inflate = View.inflate(this, R.layout.activity_dialog, null);
        dialog.setView(inflate);

        //弹窗中 三个按钮的监听
        RadioGroup rg_black = inflate.findViewById(R.id.rg_blacknum);
        rg_black.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_sms:
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        mode = 3;
                        break;
                }
            }
        });

        //输入框得到号码
        final EditText et_blacknum = inflate.findViewById(R.id.et_blacknum);

        //确认按钮 添加
        Button btn_black_add = inflate.findViewById(R.id.btn_black_add);
        btn_black_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_blacknum.getText().toString();

                //放入数据库,对电话进行判空
                if (!TextUtils.isEmpty(phone)){
                    blackNumDao.insert(phone,Integer.toString(mode));
                    //更新 数据适配器
                    BlacknumInfo blacknumInfo = new BlacknumInfo();
                    blacknumInfo.setPhone(phone);
                    blacknumInfo.setMode(Integer.toString(mode));
                    listBN.add(0,blacknumInfo);//将数据插入 集合中后 通知数据适配器 刷新
                    if (myAdapter!=null){
                        myAdapter.notifyDataSetChanged();
                    }
                    //关闭 对话框
                    dialog.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(),"请输入电话号码",Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();
    }

     static private class ViewHolder {
        TextView phone;
        TextView mode;
        ImageView img;

    }
}
