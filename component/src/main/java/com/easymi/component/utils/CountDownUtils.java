package com.easymi.component.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: CountDownUtils
 * @Author: hufeng
 * @Date: 2019/2/16 下午1:27
 * @Description: 倒计时工具类 开发中
 * @History:
 */
public class CountDownUtils {


    private Activity activity;
    private Long time;
    private TimeChangeListener timeChangeListener;

    public CountDownUtils(Activity activity,Long time,TimeChangeListener timeChangeListener){
        this.activity = activity;
        this.time = time;
        this.timeChangeListener = timeChangeListener;
        initCountDown();
    }

    /**
     * 剩余时间
     */
    long LeftSec;

    /**
     * 定时器
     */
    private Timer timer;
    private TimerTask timerTask;

    /**
     * 初始化定时器
     */
    private void initCountDown() {
        cancelTimer();
        //剩余的秒钟数
        LeftSec = (time - System.currentTimeMillis()) / 1000;
        if (LeftSec < 0) {
            LeftSec = 0;
        }

        setLeftText(LeftSec);

        if (LeftSec >= 0) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    LeftSec--;
                    activity.runOnUiThread(() -> {
                        setLeftText(LeftSec);
                        if (LeftSec <= 0) {
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            if (timerTask != null) {
                                timerTask.cancel();
                                timerTask = null;
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask, 0, 1000);
        }
    }

    /**
     * 取消定时器
     */
    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    /**
     * 倒计时显示
     * @param leftSec
     */
    private void setLeftText(long leftSec) {
        long day = leftSec / 60 / 60 / 24;
        long hour = (leftSec / 60 / 60) % 24;
        long minute = leftSec / 60 % 60;
        long sec = leftSec % 60;

        if (minute == 0 && sec > 0) {
            minute++;
        }

//        StringBuilder sb = new StringBuilder();
//        if (day >= 10) {
//            sb.append(String.valueOf(day));
//        } else {
//            sb.append("0").append(String.valueOf(day));
//        }
//        sb.append("天");
//
//        if (hour >= 10) {
//            sb.append(String.valueOf(hour));
//        } else {
//            sb.append("0").append(String.valueOf(hour));
//        }
//        sb.append("时");
//
//        if (minute >= 10) {
//            sb.append(String.valueOf(minute));
//        } else {
//            sb.append("0").append(String.valueOf(minute));
//        }
//        sb.append("分");

        if (timeChangeListener!= null){
            timeChangeListener.onTimeChanger(day,hour,minute);
        }
    }

    public interface TimeChangeListener {
        /**
         * 时间更新回调
         * @param day
         * @param hour
         * @param minute
         */
        void onTimeChanger(long day,long hour,long minute);
    }

}
