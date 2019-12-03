package com.easymi.component.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easymi.component.R;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: FinishActivity
 *@Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class CusToolbar extends RelativeLayout {

    public ImageView leftIcon;
    private TextView title;
    private ImageView rightIcon;
    private TextView rightText;

    public CusToolbar(Context context) {
        this(context, null);
    }

    public CusToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bar_layout, this);
        leftIcon = view.findViewById(R.id.left_icon);
        title = view.findViewById(R.id.title);
        rightIcon = view.findViewById(R.id.right_icon);
        rightText = view.findViewById(R.id.right_text);

    }

    public CusToolbar setLeftIcon(int resId, OnClickListener listener) {
        leftIcon.setVisibility(View.VISIBLE);
        leftIcon.setImageResource(resId);
        leftIcon.setOnClickListener(listener);
        return this;
    }

    public CusToolbar setLeftBack(OnClickListener listener) {
        leftIcon.setOnClickListener(listener);
        return this;
    }

    public CusToolbar setTitle(int textResId) {
        title.setText(textResId);
        return this;
    }
    public CusToolbar setTitle(String text) {
        title.setText(text);
        return this;
    }

    public CusToolbar setRightIcon(int resId, OnClickListener listener) {
        rightIcon.setVisibility(View.VISIBLE);
        rightIcon.setImageResource(resId);
        rightIcon.setOnClickListener(listener);
        rightText.setVisibility(View.GONE);
        return this;
    }

    public CusToolbar setRightText(int textResId, OnClickListener listener) {
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(textResId);
        rightText.setOnClickListener(listener);
        rightIcon.setVisibility(View.GONE);
        return this;
    }

    public CusToolbar setRightText(String textResId, OnClickListener listener) {
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(textResId);
        rightText.setOnClickListener(listener);
        rightIcon.setVisibility(View.GONE);
        return this;
    }

    public CusToolbar setRightGone(){
        rightText.setVisibility(View.GONE);
        rightIcon.setVisibility(View.GONE);
        return this;
    }

}
