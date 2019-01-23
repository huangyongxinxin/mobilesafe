package com.txs.mobilesafe.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.txs.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class ContactListActivity extends AppCompatActivity {

    public ListView lv_contact;
    public ArrayList<Map<String, String>> list;

    private Handler MyHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //填充数据适配器
            System.out.println("给listview 设置 数据适配器");
            myAdapter = new MyAdapter();
            lv_contact.setAdapter(myAdapter);
        }
    };
    public MyAdapter myAdapter;

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Map<String,String> getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View inflate = View.inflate(getApplicationContext(), R.layout.contact_item, null);
            TextView tv_name = inflate.findViewById(R.id.tv_name);
            TextView tv_phone = inflate.findViewById(R.id.tv_phone);
            tv_name.setText(list.get(position).get("name"));
            tv_phone.setText(list.get(position).get("phone"));
            return inflate;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initUi();
        initData();
    }

    private void initData() {

        // 检查是否已经具有权限
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // 是否需要告诉用户我们为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                //弹出信息，告诉用户我们为啥需要权限

            } else {
                //直接获取权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        0);
                //用户授权的结果会回调到FragmentActivity的onRequestPermissionsResult
            }
        }else {
            //已经拥有授权
            readContacts();
        }
//        ArrayAdapter<Map<String, String>> mapArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);
//        lv_contact.setAdapter(mapArrayAdapter);


    }

    //获得联系人
    private void readContacts() {

//        new List<Map<String,String>>
        list = new ArrayList<>();

        ContentResolver resolver=getContentResolver();
        Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri=Uri.parse("content://com.android.contacts/data");
        //循环联系人表
        Cursor cursor=resolver.query(uri, null, null, null, null);
        while(cursor.moveToNext()){
            String id=cursor.getString(cursor.getColumnIndex("contact_id"));
            HashMap<String, String> map = new HashMap<>();
            //查找数据表
            Cursor dataCursor=resolver.query(dataUri, null,"raw_contact_id=?", new String[]{id}, null);
            while(dataCursor.moveToNext()){

                String data1=dataCursor.getString(dataCursor.getColumnIndex("data1"));
                String mimetype=dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
                if (mimetype.equals("vnd.android.cursor.item/phone_v2")){
                    if (!TextUtils.isEmpty(data1)){
                        map.put("phone",data1);
                    }
                }
                if (mimetype.equals("vnd.android.cursor.item/name")){
                    if (!TextUtils.isEmpty(data1)){
                        map.put("name",data1);
                    }
                }

                System.out.println(map);
            }
            list.add(map);
            System.out.println("往list中添加map数据");
            System.out.println(list);
        }

        cursor.close();

        MyHandler.sendEmptyMessage(0);
        //消息机制
    }


    private void initUi() {
        lv_contact = findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.获取
                if (myAdapter!=null){
                    //点击之后
                    Map<String,String> item = myAdapter.getItem(position);
                    String phone = item.get("phone");
                    Intent intent = new Intent();
                    intent.putExtra("phone",phone);
                    setResult(0,intent);
                    finish();
                }
            }
        });
    }
}
