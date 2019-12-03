package com.easymi.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.easymi.component.utils.StringUtils;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:GpsReceiver
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description: Gps状态变化广播处理
 * History:
 */

public class GpsReceiver extends BroadcastReceiver {
    boolean gpsIsOpen = false;
    private OnGpsStatusChangeListener listener;

    public void setListener(OnGpsStatusChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (StringUtils.isNotBlank(action)) {
            if (action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                gpsIsOpen = getGPSState(context);
                    if (null != listener) {
                        listener.showGpsState(gpsIsOpen);
                    }
            }
        }
    }


    /**
     * 获取ＧＰＳ当前状态
     *
     * @param context
     * @return
     */
    private boolean getGPSState(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean on = false;
        if (locationManager != null) {
            on = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        return on;
    }

    public interface OnGpsStatusChangeListener {
        void showGpsState(boolean on);
    }
}
