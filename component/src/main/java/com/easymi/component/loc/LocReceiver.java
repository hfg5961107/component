package com.easymi.component.loc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easymi.component.Config;
import com.easymi.component.app.XApp;
import com.easymi.component.entity.EmLoc;
import com.easymi.component.utils.StringUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: FinishActivity
 *
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description: 接收来自其他线程的位置变化广播
 * History:
 */


public class LocReceiver extends BroadcastReceiver implements LocSubject {

    private static List<LocObserver> observers;

    private static LocReceiver locReceiver;

    public LocReceiver() {

    }

    public static LocReceiver getInstance() {
        if (null == locReceiver) {
            locReceiver = new LocReceiver();
        }
        return locReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent && StringUtils.isNotBlank(intent.getAction())
                && intent.getAction().equals(LocService.LOC_CHANGED)) {
            String loc = intent.getStringExtra("locPos");

            EmLoc emLoc = new Gson().fromJson(loc, EmLoc.class);

            //上次的定位信息
            String lastLocJson = XApp.getMyPreferences().getString(Config.SP_LAST_LOC, "");

            if (StringUtils.isNotBlank(lastLocJson)) {
                //相当于在没有poi时只保存了位置的经纬度，poi检索没变
                EmLoc lastLoc = new Gson().fromJson(lastLocJson, EmLoc.class);
                if (StringUtils.isBlank(emLoc.poiName)) {
                    //此次定位的poi为空时 只更新一些基础位置信息
                    lastLoc.latitude = emLoc.latitude;
                    lastLoc.longitude = emLoc.longitude;
                    lastLoc.accuracy = emLoc.accuracy;
                    lastLoc.locTime = emLoc.locTime;
                    lastLoc.altitude = emLoc.altitude;
                    lastLoc.speed = emLoc.speed;
                    lastLoc.bearing = emLoc.bearing;
                    lastLoc.isOffline = emLoc.isOffline;
                    XApp.getEditor()
                            .putString(Config.SP_LAST_LOC, new Gson().toJson(lastLoc))
                            .apply();//保存上次的位置信息 json格式字符创
                    notifyObserver(lastLoc);
                } else {
                    XApp.getEditor()
                            .putString(Config.SP_LAST_LOC, loc)
                            .apply();//保存上次的位置信息 json格式字符创
                    notifyObserver(emLoc);
                }
            } else {
                XApp.getEditor()
                        .putString(Config.SP_LAST_LOC, loc)
                        .apply();//保存上次的位置信息 json格式字符创
                notifyObserver(emLoc);
            }
        }
    }

    /**
     * 添加观察者
     *
     * @param obj
     */
    @Override
    public void addObserver(LocObserver obj) {
        if (null == observers) {
            observers = new ArrayList<>();
        }
        boolean hasd = false;
        for (LocObserver observer : observers) {
            if (obj == observer) {
                hasd = true;
            }
        }
        if (!hasd) {
            //避免重复添加观察者
            observers.add(obj);
        }
    }

    /**
     * @param obj
     */
    @Override
    public void deleteObserver(LocObserver obj) {
        if (null == observers) {
            return;
        }
        observers.remove(obj);
    }

    @Override
    public void notifyObserver(EmLoc loc) {
        if (null == observers) {
            return;
        }
        for (LocObserver observer : observers) {
            observer.receiveLoc(loc);
        }
    }
}
