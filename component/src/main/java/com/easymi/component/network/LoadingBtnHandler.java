package com.easymi.component.network;

import android.os.Handler;
import android.os.Message;

import com.easymi.component.widget.LoadingButton;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class LoadingBtnHandler extends Handler {

    LoadingButton loadingButton;

    public static final int SHOW_BTN_LOADING = 0X03;
    public static final int HIDE_BTN_LOADING = 0X04;

    private ProgressDismissListener progressDismissListener;

    public LoadingBtnHandler(LoadingButton loadingButton, ProgressDismissListener listener) {
        this.loadingButton = loadingButton;
        this.progressDismissListener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SHOW_BTN_LOADING:
                if (loadingButton != null){
                    loadingButton.setClickable(false);
                    loadingButton.setStatus(LoadingButton.STATUS_LOADING);
                }
                break;

            case HIDE_BTN_LOADING:
                if (loadingButton != null){
                    loadingButton.setClickable(true);
                    loadingButton.setStatus(LoadingButton.STATUS_NORMAL);
                }
                if (null != progressDismissListener) {
                    progressDismissListener.onProgressDismiss();
                }
                break;
        }
    }
}
