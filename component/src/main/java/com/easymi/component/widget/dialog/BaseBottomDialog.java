package com.easymi.component.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.easymi.component.R;

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: BaseBottomDialog
 * @Author: hufeng
 * @Date: 2019/3/29 上午9:35
 * @Description:
 * @History:
 */
public class BaseBottomDialog extends Dialog {

    public Context context;

    public BaseBottomDialog(Context context) {
        super(context, R.style.BottomDialog);
        this.context = context;
    }

    public BaseBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //声明监听器接口
    public OnMyClickListener onMyClickListener;

    public OnMyClickListener getOnMyClickListener() {
        return onMyClickListener;
    }

    public void setOnMyClickListener(OnMyClickListener onMyClickListener) {
        this.onMyClickListener = onMyClickListener;
    }

    public interface OnMyClickListener {
        void onItemClick(View view);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
    }
}