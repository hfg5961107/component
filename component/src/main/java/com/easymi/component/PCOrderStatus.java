package com.easymi.component;

import com.easymi.component.app.XApp;

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: PCOrderStatus
 * @Author: hufeng
 * @Date: 2019/2/28 下午3:19
 * @Description:
 * @History:
 */
public class PCOrderStatus {
    /**
     * 城际拼车订单未支付
     */
    public static final int CARPOOL_STATUS_PAY = 1;
    /**
     * 城际拼车订单已支付未指派
     */
    public static final int CARPOOL_STATUS_NEW = 5;
    /**
     * 城际拼车订单已指派
     */
    public static final int CARPOOL_STATUS_ASSIGN = 10;
    /**
     * 城际拼车订单前往预约地
     */
    public static final int CARPOOL_STATUS_START = 15;
    /**
     * 城际拼车订单已到达预约地(默认开始等待)
     */
    public static final int CARPOOL_STATUS_ARRIVED = 20;
    /**
     * 城际拼车订单行程中(已经上车)
     */
    public static final int CARPOOL_STATUS_RUNNING = 25;
    /**
     * 城际拼车订单已到达目的地
     */
    public static final int CARPOOL_STATUS_FINISH = 30;
    /**
     * 城际拼车订单已跳过
     */
    public static final int CARPOOL_STATUS_SKIP = 35;
    /**
     * 城际拼车订单已评价
     */
    public static final int CARPOOL_STATUS_REVIEW = 40;
    /**
     * 城际拼车订单已取消
     */
    public static final int CARPOOL_STATUS_CANCEL = 45;

    public static String status2Str(int status) {
        switch (status) {
            case CARPOOL_STATUS_PAY:
                return XApp.getInstance().getString(R.string.carpool_status_pay);
            case CARPOOL_STATUS_NEW:
                return XApp.getInstance().getString(R.string.carpool_status_new);
            case CARPOOL_STATUS_ASSIGN:
                return XApp.getInstance().getString(R.string.carpool_status_assign);
            case CARPOOL_STATUS_START:
                return XApp.getInstance().getString(R.string.carpool_status_start);
            case CARPOOL_STATUS_ARRIVED:
                return XApp.getInstance().getString(R.string.carpool_status_arrived);
            case CARPOOL_STATUS_RUNNING:
                return XApp.getInstance().getString(R.string.carpool_status_running);
            case CARPOOL_STATUS_FINISH:
                return XApp.getInstance().getString(R.string.carpool_status_finish);
            case CARPOOL_STATUS_SKIP:
                return XApp.getInstance().getString(R.string.carpool_status_skip);
            case CARPOOL_STATUS_REVIEW:
                return XApp.getInstance().getString(R.string.carpool_status_review);
            case CARPOOL_STATUS_CANCEL:
                return XApp.getInstance().getString(R.string.carpool_status_cancel);
            default:
                return "";
        }
    }
}
