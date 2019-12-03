package com.easymi.component.loc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.trace.LBSTraceClient;
import com.easymi.component.Config;
import com.easymi.component.DJOrderStatus;
import com.easymi.component.R;
import com.easymi.component.app.XApp;
import com.easymi.component.db.SqliteHelper;
import com.easymi.component.entity.DymOrder;
import com.easymi.component.entity.EmLoc;
import com.easymi.component.utils.GPSUtils;
import com.easymi.component.utils.Log;
import com.google.gson.Gson;

import java.util.List;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: FinishActivity
 *
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class LocService extends Service implements AMapLocationListener {

    public static final String START_LOC = "com.easymi.eomponent.START_LOC";
    public static final String STOP_LOC = "com.easymi.eomponent.STOP_LOC";

    public static final String LOC_CHANGED = "com.easymi.eomponent.LOC_CHANGED";
    public static final String BROAD_TRACE_SUC = "com.easymi.eomponent.BROAD_TRACE_SUC";

    private int scanTime = Config.NORMAL_LOC_TIME;

    private static final int NOTI_ID = 1011;

    private static final String NOTIFICATION_CHANNEL_NAME = "定位服务通知栏";
    private NotificationManager notificationManager = null;
    boolean isCreateChannel = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && null != intent.getAction() && intent.getAction().equals(START_LOC)) {
            startLoc();
            return START_STICKY;
        } else {
            if (intent != null && intent.getAction() != null && intent.getAction().equals(STOP_LOC)) {
                stopService();
            }
        }
        return START_NOT_STICKY;
    }

    AMapLocationClient locClient;

    private void startLoc() {
        if (locClient == null) {
            locClient = new AMapLocationClient(this);
            locClient.setLocationListener(this);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                locClient.disableBackgroundLocation(true);
            }
            locClient.stopLocation();
        }

//        if (isDriverBusy()) {//通过是否有订单在执行设置周期
//            scanTime = Config.BUSY_LOC_TIME;
//        } else {
//            scanTime = Config.FREE_LOC_TIME;
//        }
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption()
                .setInterval(scanTime)
                .setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport)
//                .setWifiScan(true)
                .setLocationCacheEnable(false)
                .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                .setNeedAddress(true)
                .setMockEnable(false)
                .setSensorEnable(true);
        locClient.setLocationOption(mLocationOption);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            locClient.enableBackgroundLocation(NOTI_ID, buildNotification());
        } else {
            startForeground(NOTI_ID, buildNotification());
        }

        locClient.startLocation();
    }

    private static SqliteHelper helper;

    /**
     * 司机是否忙碌
     */
    private boolean isDriverBusy() {
        boolean isBusy;
        if (helper == null) {
            SqliteHelper.init(this);
            helper = SqliteHelper.getInstance();
        }
        List<DymOrder> dymOrders = DymOrder.findAll();
        if (dymOrders.size() == 0) {
            isBusy = false;
        } else {
            isBusy = true;
        }
        return isBusy;
    }

    protected void stopService() {
        if (null != locClient) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                locClient.disableBackgroundLocation(true);
            }
            locClient.stopLocation();
            locClient.onDestroy();
            locClient = null;
            stopSelf();
        }
    }

    LBSTraceClient lbsTraceClient;


    private void stopTrace() {
        Log.e("trace", "停止纠偏");
        if (null != lbsTraceClient) {
            Log.e("trace", "停止纠偏zz");
            lbsTraceClient.stopTrace();
            lbsTraceClient = null;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
                if (amapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_GPS) {
                    if (amapLocation.getAccuracy() > 200) {
                        return;
                    }
                } else if (amapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_WIFI) {
                    if (amapLocation.getAccuracy() > 300) {
                        return;
                    }
                } else {
                    if (amapLocation.getAccuracy() > 800) {
                        return;
                    }
                }

                EmLoc locationInfo = EmLoc.ALocToLoc(amapLocation);
                Log.e("locService", "emLoc>>>>" + locationInfo.toString());
                Intent intent = new Intent(LocService.this, LocReceiver.class);
                intent.setAction(LOC_CHANGED);
                intent.setPackage(getPackageName());
                intent.putExtra("locPos", new Gson().toJson(locationInfo));
                sendBroadcast(intent);//发送位置变化广播

            } else {
                Location location = GPSUtils.getInstance(this).getLngAndLat();
                EmLoc emLoc = new EmLoc();
                if (location != null) {
                    emLoc.latitude = location.getLatitude();
                    emLoc.longitude = location.getLongitude();
                    emLoc.accuracy = location.getAccuracy();
                    emLoc.locTime = location.getTime();
                    emLoc.altitude = location.getAltitude();
                    emLoc.speed = location.getSpeed();
                    emLoc.bearing = location.getBearing();
                }
                emLoc.isOffline = true;
                Intent intent = new Intent(LocService.this, LocReceiver.class);
                intent.setAction(LOC_CHANGED);
                intent.setPackage(getPackageName());
                intent.putExtra("locPos", new Gson().toJson(emLoc));
                sendBroadcast(intent);//发送位置变化广播

                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("locService", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }

    }

    private Notification buildNotification() {
        boolean isLogin = XApp.getMyPreferences().getBoolean(Config.SP_ISLOGIN, false);
        Intent intent = new Intent();

        if (isLogin) {
            intent.setClassName(this, "com.easymi.common.mvp.work.WorkActivity");

        } else {
            intent.setClassName(this, "com.easymi.common.activity.SplashActivity");
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);

        Notification.Builder builder = null;
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = getPackageName() + "/locChannel";
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationChannel.setSound(null, null);
                notificationChannel.enableVibration(false);
                notificationChannel.setVibrationPattern(null);
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }


        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setSound(null);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(getResources().getColor(R.color.colorPrimary));
        }
        builder.setContentTitle(getResources().getString(R.string.app_name));
        builder.setContentText(getResources().getString(R.string.app_name)
                + getResources().getString(R.string.houtai));
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setVibrate(null);

        notification = builder.build();

        return notification;
    }

    public static boolean needTrace() {
        if (!Config.NEED_TRACE) {
            return Config.NEED_TRACE;
        }
        List<DymOrder> dymOrders = DymOrder.findAll();
        boolean needTrace = false;
        for (DymOrder dymOrder : dymOrders) {
            if (dymOrder.serviceType.equals(Config.DAIJIA)) {
                if (dymOrder.orderStatus == DJOrderStatus.GOTO_DESTINATION_ORDER) {
                    needTrace = true;
                }
            }
        }
        return needTrace;
    }
}
