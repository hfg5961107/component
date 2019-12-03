package com.easymi.component;

import com.easymi.component.app.XApp;

public class BusOrderStatus {


    //班车班次状态处理
    /**
     * 新班次
     */
    public static final int SCHEDULE_STATUS_NEW = 1;
    /**
     * 班次已出发
     */
    public static final int SCHEDULE_STATUS_RUNNING = 5;
    /**
     * 班次已结束
     */
    public static final int SCHEDULE_STATUS_FINISH = 10;

    public static String status2Str(int status) {
        switch (status) {
            case SCHEDULE_STATUS_NEW:
                return XApp.getInstance().getString(R.string.bus_status_new);
            case SCHEDULE_STATUS_RUNNING:
                return XApp.getInstance().getString(R.string.bus_status_begain);
            case SCHEDULE_STATUS_FINISH:
                return XApp.getInstance().getString(R.string.bus_status_end);
            default:
                return "";
        }
    }

    //班车订单状态处理

    /**
     * 客运班车订单未支付
     */
    public static final int CITY_COUNTRY_STATUS_PAY = 1;
    /**
     * 客运班车订单未开始
     */
    public static final int CITY_COUNTRY_STATUS_NEW = 5;
    /**
     * 等待上车(车辆未到达站点)
     */
    public static final int CITY_COUNTRY_STATUS_COMING = 6;
    /**
     * 验票中(车辆已经到达站点)
     */
    public static final int CITY_COUNTRY_STATUS_ARRIVED = 8;
    /**
     * 客运班车订单行程中
     */
    public static final int CITY_COUNTRY_STATUS_RUNNING = 10;
    /**
     * 客运班车订单已到达
     */
    public static final int CITY_COUNTRY_STATUS_FINISH = 15;
    /**
     * 客运班车订单已失效(跳过)
     */
    public static final int CITY_COUNTRY_STATUS_INVALID = 20;
    /**
     * 客运班车订单已评价
     */
    public static final int CITY_COUNTRY_STATUS_REVIEW = 25;
    /**
     * 客运班车订单已消单
     */
    public static final int CITY_COUNTRY_STATUS_CANCEL = 30;
    /**
     * 客运班车订单已退款
     */
    public static final int CITY_COUNTRY_ORDER_REFOUND = 35;


    public static String orderStatus2Str(int status) {
        switch (status) {
            case CITY_COUNTRY_STATUS_PAY:
                return XApp.getInstance().getString(R.string.cb_status_pay);
            case CITY_COUNTRY_STATUS_NEW:
                return XApp.getInstance().getString(R.string.cb_status_no_start);
            case CITY_COUNTRY_STATUS_COMING:
                return XApp.getInstance().getString(R.string.cb_status_wait);
            case CITY_COUNTRY_STATUS_ARRIVED:
                return XApp.getInstance().getString(R.string.cb_status_check);
            case CITY_COUNTRY_STATUS_RUNNING:
                return XApp.getInstance().getString(R.string.cb_status_running);
            case CITY_COUNTRY_STATUS_FINISH:
                return XApp.getInstance().getString(R.string.cb_status_finish);
            case CITY_COUNTRY_STATUS_INVALID:
                return XApp.getInstance().getString(R.string.cb_status_skip);
            case CITY_COUNTRY_STATUS_REVIEW:
                return XApp.getInstance().getString(R.string.cb_status_review);
            case CITY_COUNTRY_STATUS_CANCEL:
                return XApp.getInstance().getString(R.string.cb_status_cancel);
            case CITY_COUNTRY_ORDER_REFOUND:
                return XApp.getInstance().getString(R.string.cb_status_back);
            default:
                return "";
        }
    }

}
