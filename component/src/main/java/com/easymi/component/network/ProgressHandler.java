package com.easymi.component.network;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.easymi.component.R;
import com.easymi.component.widget.RxProgressHUD;

/**
 * Created by Administrator on 2016/9/26.
 */

public class ProgressHandler extends Handler {

    private Context context;

    private RxProgressHUD progressHUD;

    private boolean cancelable;

    public static final int SHOW_DIALOG = 0X01;
    public static final int DISMISS_DIALOG = 0X02;

    private ProgressDismissListener progressDismissListener;

    public ProgressHandler(Context context, boolean cancelable, final ProgressDismissListener progressDismissListener) {
        this.context = context;
        this.cancelable = cancelable;
        this.progressDismissListener = progressDismissListener;
    }

    private void initDialog() {

        progressHUD = new RxProgressHUD.Builder(context)
                .setTitle("")
                .setMessage(context.getString(R.string.wait))
                .setCancelable(cancelable)
                .setOnDismissListener(dialog -> progressDismissListener.onProgressDismiss()).create();
        if (!progressHUD.isShowing()) {
            if (context != null && context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }
            progressHUD.show();
        }
    }

    private void dismissDialog() {
        if (null != progressHUD && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {

            case SHOW_DIALOG:
                initDialog();
                break;

            case DISMISS_DIALOG:
                dismissDialog();
                break;
        }
    }
}
