package com.easymi.component.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

/**
 * Created by xyin on 2016/10/9.
 * 权限工具类.
 */

public class PermissionUtil {

    /**
     * 检测一系列权限是否全都已经获取.
     *
     * @param context     Context for accessing resources
     * @param permissions The permission to check
     * @return : 检测的权限全都存在时返回true,否则返回false
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasSelfPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (!hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测单个权限是否已经获取到权限.
     *
     * @param context    Context for accessing resources
     * @param permission The permission to check
     * @return : 如果已经存在权限则返回true,否则返回false
     */
    private static boolean hasSelfPermission(Context context, String permission) {
        try {
            return PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }

    /**
     * Checks given permissions are needed to show rationale.
     *
     * @param activity    activity
     * @param permissions permission list
     * @return returns true if one of the permission is needed to show rationale.
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks all given permissions have been granted.
     *
     * @param grantResults results
     * @return returns true if all permissions have been granted.
     */
    public static boolean verifyPermissions(int... grantResults) {
        if (grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限的回调接口.
     */
    public interface PermissionCallBack {
        /**
         * 已经获取权限回调方法.
         *
         * @param requestCode 请求权限时的请求码,可以用来判断是哪个请求
         */
        void onGranted(int requestCode);

        /**
         * 请求权限被拒绝时回调方法.
         *
         * @param requestCode 请求码,可以用来判断是哪个请求
         */
        void onDenied(int requestCode);

        /**
         * 请求权限时需要展示理由时回调方法.
         *
         * @param permissions 所请求的权限,可以用作重新请求权限
         * @param requestCode 请求码,可以用来判断是哪个请求
         */
        void showRationale(String[] permissions, int requestCode);
    }


}
