package com.easymi.component;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class EmployStatus {
    /**
     * 离线(未登录)
     */
    public static final String OFFLINE = String.valueOf(1);
    /**
     * 在线(登录未听单)
     */
    public static final String ONLINE = String.valueOf(2);
    /**
     * 空闲（登录后听单中）
     */
    public static final String WORK = String.valueOf(3);
    /**
     * 派单
     */
    public static final String SEND_ORDER = String.valueOf(5);
    /**
     * 接单
     */
    public static final String ACCEPT_ORDER = String.valueOf(10);
    /**
     * 前往预约地
     */
    public static final String TO_START = String.valueOf(15);
    /**
     * 到达预约地
     */
    public static final String ARRIVE_START = String.valueOf(20);
    /**
     * 前往目的地
     */
    public static final String TO_END = String.valueOf(25);
    /**
     * 中途等待
     */
    public static final String MIDDLE_WAIT = String.valueOf(28);
    /**
     * 冻结
     */
    public static final String FROZEN = String.valueOf(45);
}
