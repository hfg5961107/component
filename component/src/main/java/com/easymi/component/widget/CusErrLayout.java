package com.easymi.component.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class CusErrLayout extends LinearLayout {

    private ImageView imageView;
    private TextView text;

    public CusErrLayout(Context context) {
        this(context, null);
    }

    public CusErrLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusErrLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.err_layout, this);
        imageView = view.findViewById(R.id.empty_img);
        text = view.findViewById(R.id.empty_text);
    }

    public CusErrLayout setImg(int imgId) {
        imageView.setImageResource(imgId);
        return this;
    }

    public CusErrLayout setText(int textId) {
        text.setText(textId);
        return this;
    }

    public CusErrLayout setErrText(int errcode) {
        text.setText(getResources().getString(R.string.err_error) + "（" + errcode + "）");
        return this;
    }

    public CusErrLayout setErrImg(){
        imageView.setImageResource(R.mipmap.ic_net_error);
        return this;
    }
}
