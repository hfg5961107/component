package com.easymi.component.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: FinishActivity
 *
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */
public class BaseOrder implements Serializable {



    public String orderDetailType;

    @SerializedName("examineStatus")
    public int baoxiaoStatus;//1未报销，2申请中，3已报销  ///0 -可以申请  1- 申请中   2-申请同意  3-申请拒绝

    public String passengerTags;

    /**
     * 热订单id
     */
    public long id;
    /**
     * 班次状态
     */
    public int scheduleStatus;

    /**
     * 订单状态
     */
    public int status;

    /**
     * 班次id  专线、拼车特有
     */
    public long scheduleId;

    /**
     * 订单主键  专线没有此值
     */
    public long orderId;

    /**
     * 订单编号
     */
    public String orderNo;

    /**
     * 公司主键
     */
    public long companyId;

    /**
     * 公司名称
     */
    public String companyName;

    /**
     * 预约时间
     */
    public long bookTime;

    /**
     * 乘客主键
     */
    public long passengerId;

    /**
     * 乘客姓名
     */
    public String passengerName;

    /**
     * 乘客电话
     */
    public String passengerPhone;

    /**
     * 司机主键
     */
    public long driverId;

    /**
     * 司机姓名
     */
    public String driverName;

    /**
     * 司机电话
     */
    public String driverPhone;

    /**
     * 订单渠道名称
     */
    public String channelAlias;

    /**
     * 服务类型
     */
    public String serviceType;

    /**
     * 预约地
     */
    @SerializedName("startAddress")
    public String bookAddress;

    /**
     * 目的地
     */
    @SerializedName("endAddress")
    public String destination;

    /**
     * 预算金额
     */
    @SerializedName("money")
    public double budgetFee;

    /**
     * 预付费
     */
    public double prepaid;

    /**
     * 优惠券主键
     */
    public long couponId;

    /**
     * 预计时间
     */
    public int time;

    /**
     * 预计距离
     */
    public double distance;

    /**
     * 订单备注
     */
    public String orderRemark;

    /**
     * 应付金额
     */
    public double shouldPay;

    /**
     * 是否是预约订单 1是 2否
     */
    public int isBookOrder;

    /**
     * 订单地址信息json字符串
     */
    public String orderAddress;

    /**
     * 乐观锁
     */
    public long version;

//热订单
//    /**
//     *主键
//     */
//    private Long id;

//    /**
//     *订单主键(专线订单无此值)
//     */
//    private Long orderId;

//    /**
//     *业务类型
//     */
//    private String serviceType;

//    /**
//     *订单状态
//     */
//    private Integer status;

//    /**
//     *预约时间
//     */
//    private Long bookTime;

//    /**
//     *出发地
//     */
//    private String startAddress;

    /**
     * 起点纬度
     */
    public double startLatitude;

    /**
     * 起点经度
     */
    public double startLongitude;

//    /**
//     *目的地
//     */
//    private String endAddress;

    /**
     * 终点纬度
     */
    public double endLatitude;

    /**
     * 终点经度
     */
    public double endLongitude;

    /**
     * 线路ID
     */
    public long lineId;

    /**
     * 线路名称
     */
    public String lineName;

//    /**
//     * 班次出发时间
//     */
//    public long scheduleTime;

    /**
     * 购买票数
     */
    public int ticket;

    public String ticketNumber;

    /**
     * 提前接人分钟数
     */
    public int minute;

    /**
     * 停止售票时间
     */
    public int stopSaleMinute;

    /**
     * 座位数
     */
    public int seats;

    /**
     * 客服电话
     */
    public String companyPhone;

    /**
     * 客户头像
     */
    public String avatar;

    /**
     * 订单类型
     */
    public String orderType;


    /**
     * 是否是转单 1 == 转单
     */
    public int orderChange;

    /**
     * 班次结束时间
     */
    public long scheduleFinishTime;

    /**
     * 等待行程开始
     */
    public static final int PC_SCHEDULE_STATUS_NEW = 1;
    /**
     * 执行中
     */
    public static final int PC_SCHEDULE_RUNNING = 12;
    /**
     * 已结束
     */
    public static final int PC_SCHEDULE_STATUS_FINISH = 15;


    public String getPCOrderStatusStr() {
        if (scheduleStatus == PC_SCHEDULE_STATUS_NEW) {
            return "等待行程开始";
        } else if (scheduleStatus == PC_SCHEDULE_RUNNING) {
            return "执行中";
        } else if (scheduleStatus == PC_SCHEDULE_STATUS_FINISH) {
            return "已结束";
        } else {
            return "已结束";
        }
    }


    /**
     * 售票中
     */
    public static final int ZX_SCHEDULE_STATUS_SALE = 1;
    /**
     * 等待行程开始
     */
    public static final int ZX_SCHEDULE_STATUS_PREPARE = 5;
    /**
     * 司机接人
     */
    public static final int ZX_SCHEDULE_STATUS_TAKE = 10;
    /**
     * 司机送人
     */
    public static final int ZX_SCHEDULE_STATUS_RUN = 15;
    /**
     * 已结束
     */
    public static final int ZX_SCHEDULE_STATUS_FINISH = 20;

    /**
     * 更具班次状态获取状态文字
     *
     * @return
     */
    public String getZXOrderStatusStr() {
        if (scheduleStatus == ZX_SCHEDULE_STATUS_SALE) {
            return "售票中";
        } else if (scheduleStatus == ZX_SCHEDULE_STATUS_PREPARE) {
            return "未开始";
        } else if (scheduleStatus == ZX_SCHEDULE_STATUS_TAKE) {
            return "正在接人";
        } else if (scheduleStatus == ZX_SCHEDULE_STATUS_RUN) {
            return "正在送人";
        } else if (scheduleStatus == ZX_SCHEDULE_STATUS_FINISH) {
            return "已完成";
        } else {
            return "";
        }
    }


}
