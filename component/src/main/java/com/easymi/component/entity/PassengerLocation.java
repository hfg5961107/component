package com.easymi.component.entity;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: PassengerLocation
 * @Author: shine
 * Date: 2018/12/11 上午11:57
 * Description: 乘客位置信息
 * History:
 */
public class PassengerLocation {

    /**
     * passengerId : 97
     * orderId : 292
     * longitude : 103.864561
     * latitude : 30.686619
     * serviceType : taxi
     */
    /**
     * 客户id
     */
    public long passengerId;
    /**
     * 订单id
     */
    public long orderId;

    public double longitude;
    public double latitude;
    /**
     * 服务类型
     */
    public String serviceType;

    @Override
    public String toString() {
        return "PassengerLocation{" +
                "passengerId=" + passengerId +
                ", orderId=" + orderId +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", serviceType='" + serviceType + '\'' +
                '}';
    }
}
