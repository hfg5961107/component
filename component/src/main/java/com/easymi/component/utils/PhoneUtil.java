package com.easymi.component.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.easymi.component.Config;
import com.easymi.component.R;
import com.easymi.component.app.XApp;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

/**
 * Created by xyin on 2017/4/10.
 * 电话号码帮助类.
 */

public class PhoneUtil {

    private static final String TAG = "PhoneUtil";

    private PhoneUtil() {
    }

    /**
     * 获取联系人.
     *
     * @param activity    activity
     * @param requestCode requestCode
     */
    public static void getContacts(Activity activity, int requestCode) {
        if (activity != null) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 获取联系人.
     *
     * @param fragment    fragment
     * @param requestCode requestCode
     */
    public static void getContacts(Fragment fragment, int requestCode) {
        if (fragment != null) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 处理查询结果.
     *
     * @param resultCode resultCode
     * @param data       data
     */
    public static UserPhone handleResult(Context context, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null || context == null) {
            Log.e(TAG, "getContacts fail, context is null or intent data is null");
            return null;
        }

        UserPhone userPhone = null;

        //开始查询
        Uri uri = data.getData();
        if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String userName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null,
                        null);
                if (phoneCursor != null) {
//                    while (phoneCursor.moveToNext()) {  //遍历该联系人所有号码
//                        String phoneNo = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Log.d(TAG, phoneNo + " (" + userName + ")");
//                    }

                    //获取第一个电话
                    if (phoneCursor.moveToNext()) {
                        String phoneNo = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        userPhone = new UserPhone();
                        userPhone.name = userName;
                        if (!TextUtils.isEmpty(phoneNo)) {
                            try {
                                // 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
                                userPhone.phoneNo = phoneNo.replaceAll("^(\\+86)", "")
                                        .replaceAll("^(86)", "")
                                        .replaceAll("-", "")
                                        .replaceAll(" ", "")
                                        .trim();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                userPhone.phoneNo = phoneNo;
                            }
                        } else {
                            userPhone.phoneNo = phoneNo;
                        }
                    }

                    phoneCursor.close();
                }
                cursor.close();
            }
        }
        return userPhone;
    }

    /**
     * 电话号码实例对象.
     */
    public static class UserPhone {
        public String name; //名字
        public String phoneNo; //电话号码
    }

    public static void hideKeyboard(Activity paramActivity) {

        InputMethodManager localInputMethodManager = (InputMethodManager) paramActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View localView = paramActivity.getCurrentFocus();
        if (localView != null) {
            IBinder localIBinder = localView.getWindowToken();
            if (localIBinder != null)
                localInputMethodManager
                        .hideSoftInputFromWindow(localIBinder, 0);
        }
    }

//    public static void hideKeyboard1(Activity paramActivity){
//        InputMethodManager imm = (InputMethodManager) paramActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(paramActivity.getWindow().getDecorView().getWindowToken(), 0);
//        }
//    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void call(final Activity context, String phoneNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));//跳转到拨号界面，同时传递电话号码
        context.startActivity(dialIntent);
    }

    /**
     * 判断服务是否处于运行状态.
     *
     * @param servicename
     * @param context
     * @return
     */
    public static boolean isServiceRunning(String servicename, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = null;
        if (am != null) {
            try {
                infos = am.getRunningServices(100);
                for (ActivityManager.RunningServiceInfo info : infos) {
                    if (servicename.equals(info.service.getClassName())) {
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    /**
     * 检查GPS是否可用
     *
     * @param context
     * @return
     */
    public static boolean checkGps(final Context context) {
        if (!PhoneFunc.hasGps(context)) {
            ToastUtil.showMessage(context, context.getResources().getString(R.string.no_gps), Toast.LENGTH_LONG);
        }
//        if (!PhoneFunc.checkWifi(context)) {
//            ToastUtil.showMessage(context, context.getResources().getString(R.string.closed_wifi), Toast.LENGTH_LONG);
//        }
        if (!PhoneFunc.isGPSEnable(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getResources().getString(R.string.please_open_gps));
            builder.setNegativeButton(context.getResources().getString(R.string.ok),
                    (dialog, which) -> {
                        if (!PhoneFunc.isGPSEnable(context)) {
                            try {
                                if ("ZTE".equalsIgnoreCase(Build.MANUFACTURER)) {
                                    ToastUtil.showMessage(context, context.getResources().getString(R.string.please_open_gps));
                                } else {
                                    context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            } catch (Exception e) {
                                ToastUtil.showMessage(context, context.getResources().getString(R.string.please_open_gps));
                            }
                        } else {
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return false;
        } else {
            return true;
        }
    }

    private static Vibrator vib;
    private static final long[] VIBRATOR_PATTERN = {150L, 400L, 150L, 400L};

    public static void vibrate(Context paramContext, boolean isRepeat) {

        if (XApp.getMyPreferences().getBoolean(Config.SP_SHAKE_ABLE, true)) {
            if (vib == null) {
                vib = (Vibrator) paramContext
                        .getSystemService(Context.VIBRATOR_SERVICE);
            }
            if (vib != null) {
                vib.vibrate(VIBRATOR_PATTERN, isRepeat ? 0x0 : -0x1);
            }
        }
    }


    public static String getUDID(Context context) {
        String udid = "";
        synchronized (context) {
            @SuppressLint("HardwareIds")
            String androidId = Settings.Secure
                    .getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    udid = UUID.nameUUIDFromBytes(
                            androidId.getBytes("utf8")).toString();
                } else {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                        TelephonyManager manager = ((TelephonyManager) context
                                .getSystemService(Context.TELEPHONY_SERVICE));
                        if (manager != null) {
                            @SuppressLint("HardwareIds")
                            String deviceId = manager.getDeviceId();
                            udid =
                                    deviceId != null ? UUID
                                            .nameUUIDFromBytes(
                                                    deviceId.getBytes("utf8"))
                                            .toString() : UUID.randomUUID()
                                            .toString();
                        }
                    }

                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        XApp.getEditor().putString(Config.SP_UDID, udid).apply();
        return udid;
    }

    /**
     * @param activity
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context activity) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            // 做任何自己需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * 键盘keyCode转为英文字母
     *
     * @param code
     * @return
     */
    public static String code2Str(int code) {
        String str = "";
        switch (code) {
            case 29:
                str = "A";
                break;
            case 30:
                str = "B";
                break;
            case 31:
                str = "C";
                break;
            case 32:
                str = "D";
                break;
            case 33:
                str = "E";
                break;
            case 34:
                str = "F";
                break;
            case 35:
                str = "G";
                break;
            case 36:
                str = "H";
                break;
            case 37:
                str = "I";
                break;
            case 38:
                str = "J";
                break;
            case 39:
                str = "K";
                break;
            case 40:
                str = "L";
                break;
            case 41:
                str = "M";
                break;
            case 42:
                str = "N";
                break;
            case 43:
                str = "O";
                break;
            case 44:
                str = "P";
                break;
            case 45:
                str = "Q";
                break;
            case 46:
                str = "R";
                break;
            case 47:
                str = "S";
                break;
            case 48:
                str = "T";
                break;
            case 49:
                str = "U";
                break;
            case 50:
                str = "V";
                break;
            case 51:
                str = "W";
                break;
            case 52:
                str = "X";
                break;
            case 53:
                str = "Y";
                break;
            case 54:
                str = "Z";
                break;


        }
        return str;
    }

}
