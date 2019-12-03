package com.easymi.component.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.easymi.component.R;

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: SureBaseDialog
 * @Author: hufeng
 * @Date: 2019/10/29 上午10:43
 * @Description:
 * @History:
 */
public class SureBaseDialog extends BaseCenterDialog{

    TextView tv_sure;
    TextView tv_cancel;

    String title;

    TextView tv_title;

    public SureBaseDialog(Context context,String title) {
        super(context);
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sure_base);
        tv_sure = findViewById(R.id.tv_sure);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_title = findViewById(R.id.tv_title);

        tv_title.setText(title);

        tv_sure.setOnClickListener(view -> {
            if (getOnMyClickListener() != null) {
                getOnMyClickListener().onItemClick(view, "");
            }
            dismiss();
        });
        tv_cancel.setOnClickListener(view -> {
            dismiss();
        });
    }
}
