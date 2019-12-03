package com.easymi.component.entity;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: TiredNotice
 *@Author: shine
 * Date: 2018/11/22 下午1:44
 * Description:
 * History:
 */
public class TiredNotice {

    //提示内容
    public String notice;
    //设置的应该休息时间
    public int relaxTime;
    //已经疲劳时间
    public int tiredTime;
    // 还有多少时间即将进入疲劳驾驶
    public int remainTime;
    /**
     * 是否疲劳状态1：已处于疲劳状态，0未处于疲劳状态
     */
    public int isTired;
}
