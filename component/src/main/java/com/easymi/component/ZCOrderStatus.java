package com.easymi.component;

import com.easymi.component.app.XApp;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: ZCOrderStatus
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description: 专车订单状态
 * History:
 */
public class ZCOrderStatus {
    /**
     * 新单
     */
    public static final int NEW_ORDER = 1;
    /**
     * 已派单
     */
    public static final int PAIDAN_ORDER = 5;
    /**
     * 已接单
     */
    public static final int TAKE_ORDER = 10;
    /**
     * 前往预约地
     */
    public static final int GOTO_BOOKPALCE_ORDER = 15;
    /**
     * 到达预约地
     */
    public static final int ARRIVAL_BOOKPLACE_ORDER = 20;
    /**
     * 前往目的地
     */
    public static final int GOTO_DESTINATION_ORDER = 25;
    /**
     * 中途等待
     */
    public static final int START_WAIT_ORDER = 28;
    /**
     * 到达目的地
     */
    public static final int ARRIVAL_DESTINATION_ORDER = 30;
    /**
     * 已结算
     */
    public static final int FINISH_ORDER = 35;
    /**
     * 已评价
     */
    public static final int RATED_ORDER = 40;
    /**
     * 已销单
     */
    public static final int CANCEL_ORDER = 45;

    public static String status2Str(int status) {
        switch (status) {
            case NEW_ORDER:
                return XApp.getInstance().getString(R.string.new_order);
            case PAIDAN_ORDER:
                return XApp.getInstance().getString(R.string.sended_order);
            case TAKE_ORDER:
                return XApp.getInstance().getString(R.string.accepted_order);
            case GOTO_BOOKPALCE_ORDER:
                return XApp.getInstance().getString(R.string.to_start);
            case ARRIVAL_BOOKPLACE_ORDER:
                return XApp.getInstance().getString(R.string.arrive_start);
            case GOTO_DESTINATION_ORDER:
                return XApp.getInstance().getString(R.string.to_end);
            case START_WAIT_ORDER:
                return XApp.getInstance().getString(R.string.middle_wait);
            case ARRIVAL_DESTINATION_ORDER:
                return XApp.getInstance().getString(R.string.arrive_end);
            case FINISH_ORDER:
                return XApp.getInstance().getString(R.string.settled);
            case RATED_ORDER:
                return XApp.getInstance().getString(R.string.exculated);
            case CANCEL_ORDER:
                return XApp.getInstance().getString(R.string.canceled);
            default:
                return "";
        }
    }
}
