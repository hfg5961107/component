package com.easymi.component;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class ZXOrderStatus {
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
     * 等待开始行程
     */
    public static final int WAIT_START = 15;
    /**
     * 接人规划
     */
    public static final int ACCEPT_PLAN = 20;
    /**
     * 送人规划
     */
    public static final int SEND_PLAN = 25;
    /**
     * 接人中
     */
    public static final int ACCEPT_ING = 30;
    /**
     * 送人中
     */
    public static final int SEND_ING = 35;
    /**
     * 送完人
     */
    public static final int SEND_OVER = 40;
    /**
     * 行程结束
     */
    public static final int FINISH_TRIP = 45;
}
