package com.easymi.component.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: GPSUtils
 * @Author: hufeng
 * @Date: 2019/1/22 下午5:03
 * @Description: 获取用户的地理位置
 * @History:
 */
public class GPSUtils {

    private static GPSUtils instance;
    private Context mContext;
    private LocationManager locationManager;

    private GPSUtils(Context context) {
        this.mContext = context;
    }

    public static GPSUtils getInstance(Context context) {
        if (instance == null) {
            instance = new GPSUtils(context);
        }
        return instance;
    }

    /**
     * 获取经纬度
     *
     * @return
     */
    public Location getLngAndLat() {
        if (locationManager == null) {
            locationManager = (LocationManager) mContext.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        } else {
            Location location = null;
            for (String provider : locationManager.getAllProviders()) {
                Location currentLocation = locationManager.getLastKnownLocation(provider);
                if (currentLocation != null) {
                    if (location == null || currentLocation.getAccuracy() < location.getAccuracy()) {
                        location = currentLocation;
                    }
                }
            }
            return location;
        }
    }
}
