package com.txs.mobilesafe.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txs.mobilesafe.R;

public class SettingItemView extends RelativeLayout {

    public CheckBox cb_box;
    public TextView tv_describtion;
    public String TAG = "setting";
    public String s = "http://schemas.android.com/apk/res/com.txs.mobilesafe";
    public TextView tv_title;
    public String desoff;
    public String deson;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //将设置界面的 一个条目 转换成一个view对象
        View inflate = View.inflate(context, R.layout.setting_item_view, this);

        tv_title = this.findViewById(R.id.tv_setting_1);
        tv_describtion = this.findViewById(R.id.tv_setting_2);
        cb_box = this.findViewById(R.id.cb_setting);

//        boolean checked = cb_box.isChecked();

        //获取自定义属性 从attribute对中获取
        initAttribute(attrs);

    }

    private void initAttribute(AttributeSet attrs) {
        int attributeCount = attrs.getAttributeCount();
        System.out.println("有几个属性"+attributeCount);

        //
        String title = attrs.getAttributeValue(s, "title");
        desoff = attrs.getAttributeValue(s, "desoff");
        deson = attrs.getAttributeValue(s, "deson");
//        Log.i(TAG,attributeValue);
        tv_title.setText(title);
//        if (isCheck()){
//            tv_describtion.setText(desoff);
//        }else {
//            tv_describtion.setText(deson);
//        }
        setCheck(isCheck());

    }


    public boolean isCheck(){
        return cb_box.isChecked();
    }

    public void setCheck(Boolean isCheck){
        cb_box.setChecked(isCheck);
        if (isCheck){
            tv_describtion.setText(desoff);
        }else {
            tv_describtion.setText(deson);
        }
    }

}
