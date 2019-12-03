package com.easymi.component.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast sToast = null;

    public static void showMessage(Context context, String msg) {
        showMessage(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showMessage(Context context, int msg) {
        showMessage(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showMessage(Context context, String msg, int len) {
        try {
            if (sToast != null) {
                sToast.cancel();
            }
            sToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            sToast.setText(msg);
            sToast.show();
        } catch (Exception e) {
        }
    }

    public static void showMessage(Context context, int msg, int len) {
        try {
            if (sToast != null) {
                sToast.cancel();
            }
            sToast = Toast.makeText(context, null, len);
            sToast.setText(msg);
            sToast.show();
        } catch (Exception e) {
        }
    }
}