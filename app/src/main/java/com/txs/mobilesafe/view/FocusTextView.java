package com.txs.mobilesafe.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusTextView extends android.support.v7.widget.AppCompatTextView{
    public FocusTextView(Context context) {
        super(context);
    }

    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //获取焦点的方法

    @Override
    public boolean isFocused() {
        //一直获取焦点
        return true;
    }
}
