package com.txs.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txs.mobilesafe.R;

public class SettingClickView extends RelativeLayout {

    public TextView tv_describtion;
    public String TAG = "setting";
    public TextView tv_title;

    public SettingClickView(Context context) {
        this(context,null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View inflate = View.inflate(context, R.layout.setting_click_view, this);
        tv_title = inflate.findViewById(R.id.tv_title);
        tv_describtion = inflate.findViewById(R.id.tv_des);
    }
    public void setTv_title(String title){
        tv_title.setText(title);
    }
    public void setTv_describtion(String describtion){
        tv_describtion.setText(describtion);
    }

}
