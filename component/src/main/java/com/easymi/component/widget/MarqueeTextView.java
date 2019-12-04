package com.easymi.component.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by xyin on 2016/10/18.
 * 覆写Textview#isFocused方法获取焦点实现跑马灯效果.
 */

public class MarqueeTextView extends AppCompatTextView {
    public MarqueeTextView(Context context) {
        this(context, null);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSingleLine();    //设置只显示一行,才会有跑马灯效果
        setEllipsize(TextUtils.TruncateAt.MARQUEE); //设置跑马灯效果
        setMarqueeRepeatLimit(-1);  //设置为-1相当于marquee_forever
        setClickable(true); //设置可点击,消耗掉点击事件
    }

    @Override
    public boolean isFocused() {
        return true;    //设置获取焦点
    }
}
