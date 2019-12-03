package com.easymi.component.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easymi.component.db.SqliteHelper;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class DymOrder implements Serializable {

    public long id;

    public long orderId;

    public String serviceType;

    /**
     * 专车的子业务类型（出租车）
     */
    public String orderType;

    public long passengerId;

    //起步费
    @SerializedName("startPrice")
    public double startFee;

    //等候时间 分
    @SerializedName("waitTime")
    public int waitTime;

    //等候费
    @SerializedName("waitFee")
    public double waitTimeFee;

    //行驶时间 分
    @SerializedName("time")
    public int travelTime;

    //行驶时间费
    @SerializedName("timeFee")
    public double travelFee;

    //行驶里程
    public double distance;

    //行驶里程费
    @SerializedName("distanceFee")
    public double disFee;

    //总价(计价算出来的钱)
    public double totalFee;

    //订单状态
    public int orderStatus;

    //垫付的钱
    @SerializedName("advancePrice")
    public double paymentFee;

    //附加的服务费用
    @SerializedName("otherPrice")
    public double extraFee;

    //备注
    @SerializedName("changePayReason")
    public String remark;

    //优惠券抵扣的金额
    @SerializedName("couponFee")
    public double couponFee;

    //订单总价钱 计价算出来的钱 + 垫付 + 附加费用 (不算优惠券的钱)
    @SerializedName("total_fee")
    public double orderTotalFee;

    //还需支付的钱 订单总价钱 - 优惠金额 - 预付费
    @SerializedName("shouldPay")
    public double orderShouldPay;

    //预付费用
    @SerializedName("prepaid")
    public double prepay;

    @SerializedName("minCost")
    public double minestMoney;//最低消费金额

    //夜间费
    @SerializedName("nightPrice")
    public double nightPrice;

    //低速费
    @SerializedName("lowSpeedFee")
    public double lowSpeedCost;

    //低速时间
    @SerializedName("lowSpeedTime")
    public int lowSpeedTime;

    //高峰里程
    @SerializedName("peakMile")
    public double peakMile;

    //运营高峰费
    @SerializedName("peakFee")
    public double peakCost;

    //夜间时间
    @SerializedName("nightTime")
    public int nightTime;

    //夜间里程
    @SerializedName("nightMile")
    public double nightMile;

    //夜间里程费
    public double nightMileFee;

    //夜间时间费用
    @SerializedName("nightTimeFee")
    public double nightTimePrice;

    //作弊增加的里程
    public int addedKm; //数据源来自于本地调价 不会来自后端

    //作弊增加的费用
    public double addedFee;//数据源来自于本地调价 不会来自后端

    //add
    /**
     *加价里程
     */
    public Double addDistance;
    /**
     *加价费用
     */
    public Double addFee;
    /**
     *预算金额
     */
    public Double budgetFee;
    /**
     *定价金额
     */
    public Double fixedPrice;
    /**
     *信息费
     */
    public Double messagePrice;
    /**
     *实际付款
     */
    public Double realPay;
    /**
     *insurance_price
     */
    public Double insurancePrice;
    /**
     *支付类型
     */
    public String payType;

    /**
     * 阶梯费
     */
    public String stageArrays;

    /**
     * 订单编号
     */
    public String orderNo;


    public DymOrder(long orderId, String serviceType, long passengerId, int orderStatus,String orderType) {
        this.orderId = orderId;
        this.serviceType = serviceType;
        this.passengerId = passengerId;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
    }

    public DymOrder() {

    }

    /**
     * 保存数据
     * <p>
     * 这里不保存作弊的公里数和费用
     */
    public boolean save() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("orderId", orderId);
        values.put("serviceType", serviceType);
        values.put("passengerId", passengerId);
        values.put("startFee", startFee);
        values.put("waitTime", waitTime);
        values.put("waitTimeFee", waitTimeFee);
        values.put("travelTime", travelTime);
        values.put("travelFee", travelFee);
        values.put("totalFee", totalFee);
        values.put("distance", distance);
        values.put("disFee", disFee);
        values.put("minestMoney", minestMoney);
        values.put("orderStatus", orderStatus);

        values.put("paymentFee", paymentFee);
        values.put("extraFee", extraFee);
        values.put("remark", remark);
        values.put("couponFee", couponFee);
        values.put("orderTotalFee", orderTotalFee);
        values.put("orderShouldPay", orderShouldPay);
        values.put("prepay", prepay);

        //添加专车
        values.put("peakCost", peakCost);
        values.put("nightPrice", nightPrice);
        values.put("lowSpeedCost", lowSpeedCost);
        values.put("lowSpeedTime", lowSpeedTime);
        values.put("peakMile", peakMile);
        values.put("nightTime", nightTime);
        values.put("nightMile", nightMile);
        values.put("nightTimePrice", nightTimePrice);

        values.put("nightMileFee", nightMileFee);


        values.put("stageArrays", stageArrays);
        values.put("orderNo",orderNo);
        values.put("orderType",orderType);

        boolean flag = db.insert("t_dyminfo", null, values) != -1;
        return flag;
    }

    /**
     * 更新调价费用
     *
     * @return
     */
    public boolean updateCheating() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        ContentValues values = new ContentValues();
        values.put("addedKm", addedKm);
        values.put("addedFee", addedFee);

        boolean flag = db.update("t_dyminfo", values, " id = ? ",
                new String[]{String.valueOf(id)}) == 1;
        return flag;
    }

    /**
     * 订单是否存在
     *
     * @param id
     * @return
     */
    public static boolean exists(long id) {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        Cursor cursor = db.rawQuery(
                "select count(*) from t_dyminfo where id = ? ",
                new String[]{String.valueOf(id)});
        boolean flag = false;
        try {
            if (cursor.moveToNext()) {
                flag = (cursor.getInt(0) == 1);
            }
        } finally {
            cursor.close();
        }
        return flag;
    }

    /**
     * 判断订单是否存在
     */
    public static boolean exists(long orderId, String serviceType) {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        Cursor cursor = db.rawQuery(
                "select count(*) from t_dyminfo where orderId = ? and serviceType = ?",
                new String[]{String.valueOf(orderId), serviceType});
        boolean flag = false;
        try {
            if (cursor.moveToNext()) {
                flag = (cursor.getInt(0) == 1);
            }
        } finally {
            cursor.close();
        }
        return flag;
    }

    /**
     * 根据ID和type查询数据
     */
    public static DymOrder findByIDType(long orderId, String serviceType) {

        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();

        Cursor cursor = db.rawQuery("select * from t_dyminfo where orderId = ? and serviceType = ? "
                , new String[]{String.valueOf(orderId), serviceType});

        try {
            if (cursor.moveToNext()) {
                return cursorToOrder(cursor);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public static List<DymOrder> findAll() {

        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();

        Cursor cursor = db.rawQuery("select * from t_dyminfo", new String[]{});

        List<DymOrder> list = new LinkedList<>();

        try {
            while (cursor.moveToNext()) {
                list.add(cursorToOrder(cursor));
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    private static DymOrder cursorToOrder(Cursor cursor) {
        DymOrder orderInfo = new DymOrder();
        orderInfo.id = cursor.getLong(cursor.getColumnIndex("id"));
        orderInfo.orderId = cursor.getLong(cursor.getColumnIndex("orderId"));
        orderInfo.serviceType = cursor.getString(cursor.getColumnIndex("serviceType"));
        orderInfo.startFee = cursor.getDouble(cursor.getColumnIndex("startFee"));
        orderInfo.waitTime = cursor.getInt(cursor.getColumnIndex("waitTime"));
        orderInfo.waitTimeFee = cursor.getDouble(cursor.getColumnIndex("waitTimeFee"));
        orderInfo.travelTime = cursor.getInt(cursor.getColumnIndex("travelTime"));
        orderInfo.travelFee = cursor.getDouble(cursor.getColumnIndex("travelFee"));
        orderInfo.totalFee = cursor.getDouble(cursor.getColumnIndex("totalFee"));
        orderInfo.distance = cursor.getDouble(cursor.getColumnIndex("distance"));
        orderInfo.disFee = cursor.getDouble(cursor.getColumnIndex("disFee"));
        orderInfo.minestMoney = cursor.getDouble(cursor.getColumnIndex("minestMoney"));

        orderInfo.orderStatus = cursor.getInt(cursor.getColumnIndex("orderStatus"));

        orderInfo.paymentFee = cursor.getDouble(cursor.getColumnIndex("paymentFee"));
        orderInfo.extraFee = cursor.getDouble(cursor.getColumnIndex("extraFee"));
        orderInfo.remark = cursor.getString(cursor.getColumnIndex("remark"));
        orderInfo.couponFee = cursor.getDouble(cursor.getColumnIndex("couponFee"));
        orderInfo.orderTotalFee = cursor.getDouble(cursor.getColumnIndex("orderTotalFee"));
        orderInfo.orderShouldPay = cursor.getDouble(cursor.getColumnIndex("orderShouldPay"));
        orderInfo.prepay = cursor.getDouble(cursor.getColumnIndex("prepay"));
        orderInfo.addedKm = cursor.getInt(cursor.getColumnIndex("addedKm"));
        orderInfo.addedFee = cursor.getDouble(cursor.getColumnIndex("addedFee"));

        orderInfo.peakCost = cursor.getDouble(cursor.getColumnIndex("peakCost"));
        orderInfo.nightPrice = cursor.getDouble(cursor.getColumnIndex("nightPrice"));
        orderInfo.lowSpeedCost = cursor.getDouble(cursor.getColumnIndex("lowSpeedCost"));
        orderInfo.lowSpeedTime = cursor.getInt(cursor.getColumnIndex("lowSpeedTime"));

        orderInfo.peakMile = cursor.getDouble(cursor.getColumnIndex("peakMile"));
        orderInfo.nightTime = cursor.getInt(cursor.getColumnIndex("nightTime"));
        orderInfo.nightMile = cursor.getDouble(cursor.getColumnIndex("nightMile"));
        orderInfo.nightTimePrice = cursor.getDouble(cursor.getColumnIndex("nightTimePrice"));
        orderInfo.nightMileFee = cursor.getDouble(cursor.getColumnIndex("nightMileFee"));

        orderInfo.passengerId = cursor.getLong(cursor.getColumnIndex("passengerId"));

        orderInfo.stageArrays = cursor.getString(cursor.getColumnIndex("stageArrays"));

        orderInfo.orderNo = cursor.getString(cursor.getColumnIndex("orderNo"));
        orderInfo.orderType = cursor.getString(cursor.getColumnIndex("orderType"));

        return orderInfo;
    }

    public boolean updateStatus() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        ContentValues values = new ContentValues();
        values.put("orderStatus", orderStatus);

        boolean flag = db.update("t_dyminfo", values, " id = ? ",
                new String[]{String.valueOf(id)}) == 1;
        return flag;
    }

    public boolean updateAll() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("orderId", orderId);
        values.put("serviceType", serviceType);
        values.put("startFee", startFee);
        values.put("waitTime", waitTime);
        values.put("waitTimeFee", waitTimeFee);
        values.put("travelTime", travelTime);
        values.put("travelFee", travelFee);
        values.put("totalFee", totalFee);
        values.put("distance", distance);
        values.put("disFee", disFee);
        values.put("minestMoney", minestMoney);
        values.put("passengerId", passengerId);
        values.put("orderStatus", orderStatus);

        values.put("paymentFee", paymentFee);
        values.put("extraFee", extraFee);
        values.put("remark", remark);
        values.put("couponFee", couponFee);
        values.put("orderTotalFee", orderTotalFee);
        values.put("orderShouldPay", orderShouldPay);
        values.put("prepay", prepay);

        values.put("peakCost", peakCost);
        values.put("nightPrice", nightPrice);
        values.put("lowSpeedCost", lowSpeedCost);
        values.put("lowSpeedTime", lowSpeedTime);

        values.put("peakMile", peakMile);
        values.put("nightTime", nightTime);
        values.put("nightMile", nightMile);
        values.put("nightTimePrice", nightTimePrice);
        values.put("nightMileFee", nightMileFee);


        values.put("stageArrays", stageArrays);

        values.put("orderNo",orderNo);
        values.put("orderType",orderType);

        boolean flag = db.update("t_dyminfo", values, " id = ? ",
                new String[]{String.valueOf(id)}) == 1;
        return flag;
    }

    public boolean updateFee() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        ContentValues values = new ContentValues();
        values.put("startFee", startFee);
        values.put("waitTime", waitTime);
        values.put("waitTimeFee", waitTimeFee);
        values.put("travelTime", travelTime);
        values.put("travelFee", travelFee);
        values.put("totalFee", totalFee);
        values.put("distance", distance);
        values.put("disFee", disFee);
        values.put("minestMoney", minestMoney);

        //专车
        values.put("peakCost", peakCost);
        values.put("nightPrice", nightPrice);
        values.put("lowSpeedCost", lowSpeedCost);
        values.put("lowSpeedTime", lowSpeedTime);

        values.put("peakMile", peakMile);
        values.put("nightTime", nightTime);
        values.put("nightMile", nightMile);
        values.put("nightTimePrice", nightTimePrice);
        values.put("nightMileFee", nightMileFee);

        values.put("stageArrays", stageArrays);

        values.put("orderNo",orderNo);

        boolean flag = db.update("t_dyminfo", values, " id = ? ",
                new String[]{String.valueOf(id)}) == 1;
        return flag;
    }

    public boolean updateConfirm() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        ContentValues values = new ContentValues();
        values.put("paymentFee", paymentFee);
        values.put("extraFee", extraFee);
        values.put("remark", remark);
        values.put("couponFee", couponFee);
        values.put("orderTotalFee", orderTotalFee);
        values.put("orderShouldPay", orderShouldPay);
        values.put("prepay", prepay);

        boolean flag = db.update("t_dyminfo", values, " id = ? ",
                new String[]{String.valueOf(id)}) == 1;
        return flag;
    }


    public boolean saveOrUpdate() {
        if (exists(id)) {
            return this.updateAll();
        } else {
            return this.save();
        }
    }

    public static void deleteAll() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        db.delete("t_dyminfo", null, null);
    }

    public static void deleteById(Long id) {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        db.delete("t_dyminfo", "id = ?", new String[]{String.valueOf(id)});
    }

    public void delete() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        db.delete("t_dyminfo", "id = ?", new String[]{String.valueOf(id)});
    }
}
