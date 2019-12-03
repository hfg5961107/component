package com.easymi.component.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by xyin on 2016/10/10.
 * 与系统相关的工具方法类.
 */

public class SysUtil {

    /**
     * 判断当前手机是否有ROOT权限
     *
     * @return
     */
    public static boolean isRoot() {
        boolean bool = false;
        try {
            bool = !((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    private static String[] known_qemu_drivers = {"goldfish"};

    /**
     * 检测是否为模拟器.
     *
     * @return true表示模拟器
     */
    public static boolean checkEmulatorBuild() {
        File driver_file = new File("/proc/tty/drivers");
        if (driver_file.exists() && driver_file.canRead()) {
            byte[] data = new byte[(int) driver_file.length()];
            try {
                InputStream inStream = new FileInputStream(driver_file);
                inStream.read(data);
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String driverData = new String(data);
            for (String known_qemu_driver : known_qemu_drivers) {
                if (driverData.contains(known_qemu_driver)) {
                    Log.d("Result:", "Find known_qemu_drivers!");
                    return true;    //基于qemu模拟器
                }
            }
        }
        Log.d("Result:", "Not Find known_qemu_drivers!");
        return false;
    }

    /**
     * 跳转到该app对应的设置界面.
     *
     * @param activity 调用者activity
     */
    public static void toAppSetting(Activity activity) {
        PackageManager packageManager = activity.getPackageManager();
        String packageName = "";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
            packageName = packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Uri packageURI = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        activity.startActivity(intent);
    }

    /**
     * 判断是否有网络连接.
     *
     * @param context context
     * @return if the network is available, {@code false} otherwise
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取app的版本名.
     *
     * @return app版本的名字
     */
    public static String getVersionName(Context context) {
        String version = "unknown";
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                version = packInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return version;
    }

    /**
     * 是否运行在后台.
     *
     * @param context context
     * @return 是true
     */
    public static boolean isRunningInBackground(Context context) {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null)
            return false;

        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            ComponentName topConponent = tasksInfo.get(0).topActivity;
            return !packageName.equals(topConponent.getPackageName());
        }
        return false;
    }

    /**
     * 当前显示的activity的名字.
     *
     * @param context context
     * @return name
     */
    public static String getCurrentActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            ComponentName topConponent = tasksInfo.get(0).topActivity;
            return topConponent.getClassName();
        }
        return "";
    }


    /**
     * 获取app的版本号.
     *
     * @return app版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode = 1;
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                versionCode = packInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionCode;
    }

//    /**
//     * 拨打电话.
//     *
//     * @param context  context
//     * @param phoneNum 需要拨打的电话号码
//     */
//    public static void callPhone(Context context, String phoneNum) {
//        try {
//            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
//            context.startActivity(intent);
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//    }


//    /**
//     * 获取app的版本名.
//     *
//     * @return app版本的名字
//     */
//    public static String getVersionName(Context context) {
//        String version = "unknown";
//        if (context != null) {
//            PackageManager packageManager = context.getPackageManager();
//            try {
//                PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//                version = packInfo.versionCode;
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//        return version;
//    }
//
//
//    /**
//     * 获取APP名称
//     * @param context
//     * @return
//     */
//    public static String getAppName(Context context) {
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//            int labelRes = packageInfo.applicationInfo.labelRes;
//            return context.getResources().getString(labelRes);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 打开指定包名的APP
     *
     * @param context
     * @param packageName
     */
    public void openOtherApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent launchIntentForPackage = manager.getLaunchIntentForPackage(packageName);
        if (launchIntentForPackage != null) {
            context.startActivity(launchIntentForPackage);
        }
    }


    /**
     * 检测是否有存在外部存储.
     *
     * @return 如果存在返回true, 否则返回false
     */
    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断是否开启gps定位.
     *
     * @param context context
     * @return 如果开启gps定位返回true, 否则返回false
     */
    public static boolean isGPSEnable(Context context) {
        LocationManager localLocationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return localLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

//    /**
//     * 安装APK.
//     *
//     * @param context context
//     * @param apkPath 安装包的路径
//     */
//    public static void installApk(Context context, Uri apkPath) {
//        if (apkPath != null && context != null) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(apkPath, "application/vnd.android.package-archive");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                //添加临时权限,前提是已经处理了7.0文件读写的权限
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            }
//            context.startActivity(intent);
//        }
//    }

    /**
     * 卸载APP
     *
     * @param context
     * @param packageName 包名
     */
    public static void uninstallAPK(Context context, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }

    /**
     * 获取已安装应用的签名
     *
     * @param context
     * @return
     */
    public static String getInstalledApkSign(Context context) {
        PackageInfo packageInfo;
        try {
            PackageManager packageManager = context.getPackageManager();
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            return packageInfo.signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用的 源Apk路径
     *
     * @param context
     * @return
     */
    public static String getOldApkSrcPath(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);

            return applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取未安装APK的签名
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static List<String> getUninstallApkSignatures(File file) {
        List<String> signatures = new ArrayList<String>();
        try {
            JarFile jarFile = new JarFile(file);
            JarEntry je = jarFile.getJarEntry("AndroidManifest.xml");
            byte[] readBuffer = new byte[8192];
            Certificate[] certs = loadCertificates(jarFile, je, readBuffer);
            if (certs != null) {
                for (Certificate c : certs) {
                    String sig = toCharsString(c.getEncoded());
                    signatures.add(sig);
                }
            }
        } catch (Exception ex) {
        }
        return signatures;
    }

    /**
     * 加载签名
     *
     * @param jarFile
     * @param je
     * @param readBuffer
     * @return
     */
    private static Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] readBuffer) {
        try {
            InputStream is = jarFile.getInputStream(je);
            while (is.read(readBuffer, 0, readBuffer.length) != -1) {
            }
            is.close();
            return je != null ? je.getCertificates() : null;
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 将签名转成转成可见字符串
     *
     * @param sigBytes
     * @return
     */
    private static String toCharsString(byte[] sigBytes) {
        byte[] sig = sigBytes;
        final int N = sig.length;
        final int N2 = N * 2;
        char[] text = new char[N2];
        for (int j = 0; j < N; j++) {
            byte v = sig[j];
            int d = (v >> 4) & 0xf;
            text[j * 2] = (char) (d >= 10 ? ('a' + d - 10) : ('0' + d));
            d = v & 0xf;
            text[j * 2 + 1] = (char) (d >= 10 ? ('a' + d - 10) : ('0' + d));
        }
        return new String(text);
    }


    /**
     * 给app设置一个通用字体.
     *
     * @param context                 context
     * @param staticTypefaceFieldName 被替换掉的系统字体类型
     * @param fontAssetName           用于替换的字体文件名(文件放在assets目录下)
     */
    public static void setFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    /**
     * 用newTypeface替换掉staticTypefaceFieldName.
     *
     * @param staticTypefaceFieldName 被替换掉的系统字体类型
     * @param newTypeface             用于替换的字体文件
     */
    private static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (null != myAM) {
            try {
                List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
                if (myList == null || myList.size() <= 0) {
                    return false;
                }
                for (int i = 0; i < myList.size(); i++) {
                    String mName = myList.get(i).service.getClassName();
                    if (mName.equals(serviceName)) {
                        isWork = true;
                        break;
                    }
                }
                return isWork;
            } catch (Exception ex) {
                ex.printStackTrace();
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * md5值编码.
     */
    public static String md5(byte[] byteStr) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(byteStr);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
