package com.easymi.component.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: hufeng
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */
public class PhoneFunc {

    private static TelephonyManager sTelManager;

    public static boolean hasGps(Context paramContext) {
        final LocationManager mgr = (LocationManager) paramContext
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    public static void init(Context paramContext) {
        sTelManager = (TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static boolean isGPSEnable(Context paramContext) {
        LocationManager localLocationManager = (LocationManager) paramContext
                .getSystemService(Context.LOCATION_SERVICE);

        return localLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isNetworkConnected(Context paramContext) {
        if (paramContext != null) {
            NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (localNetworkInfo != null)
                return localNetworkInfo.isAvailable();
        }
        return false;
    }

    public static boolean isNoSimCard() {
        return sTelManager.getSimState() == 1;
    }

    public static final boolean isScreenLocked(Context paramContext) {
        return ((KeyguardManager) paramContext.getSystemService(Context.KEYGUARD_SERVICE))
                .inKeyguardRestrictedInputMode();
    }

    public static boolean isWifiConnected(Context paramContext) {
        ConnectivityManager connectivity = (ConnectivityManager) paramContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] infos = connectivity.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.getTypeName().equals("WIFI")) {

                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkWifi(Context paramContext) {
        WifiManager mWifiManager = (WifiManager) paramContext
                .getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager.isWifiEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getNetOperationName() {
        return sTelManager.getNetworkOperatorName();
    }

    public static String getPhoneNumber() {
        if (sTelManager != null)
            return sTelManager.getLine1Number();
        return null;
    }

    public static String getImei() {
        String str = sTelManager.getDeviceId();
        if (TextUtils.isEmpty(str))
            return "";
        return checkIMEI(str);
    }

    public static String checkIMEI(String paramString) {
        if (TextUtils.isEmpty(paramString)) ;

        do {

            paramString = paramString + getIMEICheckDigit(paramString);

        } while ((paramString.length() != 14) || (!isIMEI(paramString)));

        return paramString;
    }

    private static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]*");
    private static boolean isIMEI(String paramString) {
        return NUMBER_PATTERN.matcher(paramString).matches();
    }

    private static int getIMEICheckDigit(String paramString)
            throws IllegalArgumentException {
        int i = 0;
        if (paramString.length() != 14)
            throw new IllegalArgumentException(
                    "IMEI Calculate Check digit, wrong length of imei");
        for (int j = 0; j < paramString.length(); j++) {
            int m = Character.digit(paramString.charAt(j), 10);
            if (j % 2 != 0)
                m *= 2;
            i += m / 10 + m % 10;
        }
        int k = i % 10;
        if (k == 0)
            return 0;
        return 10 - k;
    }

    public static boolean isWiFiActive(final Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] infos = connectivity.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.getTypeName().equals("WIFI") && ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isOpeningWifi(Context paramContext) {
        WifiManager mWifiManager = (WifiManager) paramContext
                .getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager.isWifiEnabled()) {
            return true;
        } else {
            return false;
        }
    }

}