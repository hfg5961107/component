package com.easymi.component.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easymi.component.db.SqliteHelper;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class TaxiSetting {

    @SerializedName("driverRepPay")
    public int isPaid;//代付（1开启，2关闭)

    @SerializedName("driverReimbursCharge")
    public int isExpenses;//报销（1开启，2关闭）

    public int isAddPrice;//是否允许调价（1开启，2关闭)

    @SerializedName("is_work_car")
    public int isWorkCar;//是否是工作车（1开启，2关闭)

    @SerializedName("work_car_change_order")
    public int workCarChangeOrder;//（1开启，2关闭)

    @SerializedName("driverAddCharge")
    public int employChangePrice;//确认费用时是否能加垫付费之类的

    @SerializedName("employChangeOrder")
    public int employChangeOrder;//是否可以转单（1开启，2关闭)

    //是否能消单
    @SerializedName("driverCancelOrder")
    public int canCancelOrder;

    @SerializedName("employ_factor")
    public int doubleCheck;//双因子验证

    //附近司机推荐距离
    @SerializedName("driverDistance")
    public double emploiesKm;


    //下面四个字段疑似废弃
    public double payMoney1;
    public double payMoney2;
    public double payMoney3;
    public int canCallDriver;//能否拨打附近司机电话

    public int driverRepLowBalance; //允许代付时余额不足 1，关闭；2，开启  //现在用的1开启，2关闭
    public int passengerDistance;
    public int version;

    public int grabOrder; //抢单开关
    public int distributeOrder; //派单开关
    public String serviceType; //业务类型

    public void save() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        db.delete("t_settinginfo", null, null);//先删除再创建
        ContentValues values = new ContentValues();

        values.put("isPaid", isPaid);
        values.put("isExpenses", isExpenses);
        values.put("isAddPrice", isAddPrice);
        values.put("isWorkCar", isWorkCar);
        values.put("workCarChangeOrder", workCarChangeOrder);
        values.put("employChangePrice", employChangePrice);
        values.put("employChangeOrder", employChangeOrder);
        values.put("canCancelOrder", canCancelOrder);
        values.put("doubleCheck", doubleCheck);
        values.put("canCallDriver", canCallDriver);
        values.put("payMoney1", payMoney1);
        values.put("payMoney2", payMoney2);
        values.put("payMoney3", payMoney3);

        values.put("driverRepLowBalance", driverRepLowBalance);
        values.put("passengerDistance", passengerDistance);
        values.put("version", version);

        values.put("grabOrder", grabOrder);
        values.put("distributeOrder", distributeOrder);
        values.put("serviceType", serviceType);

        db.insert("t_settinginfo", null, values);
    }

    public static TaxiSetting findOne() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        Cursor cursor = db.rawQuery("select * from t_settinginfo", null);
        TaxiSetting taxiSettingInfo = new TaxiSetting();
        try {
            if (cursor.moveToFirst()) {
                taxiSettingInfo.isPaid = cursor.getInt(cursor.getColumnIndex("isPaid"));
                taxiSettingInfo.isExpenses = cursor.getInt(cursor.getColumnIndex("isExpenses"));
                taxiSettingInfo.isAddPrice = cursor.getInt(cursor.getColumnIndex("isAddPrice"));
                taxiSettingInfo.isWorkCar = cursor.getInt(cursor.getColumnIndex("isWorkCar"));
                taxiSettingInfo.workCarChangeOrder = cursor.getInt(cursor.getColumnIndex("workCarChangeOrder"));
                taxiSettingInfo.employChangePrice = cursor.getInt(cursor.getColumnIndex("employChangePrice"));
                taxiSettingInfo.employChangeOrder = cursor.getInt(cursor.getColumnIndex("employChangeOrder"));
                taxiSettingInfo.canCancelOrder = cursor.getInt(cursor.getColumnIndex("canCancelOrder"));
                taxiSettingInfo.doubleCheck = cursor.getInt(cursor.getColumnIndex("doubleCheck"));
                taxiSettingInfo.canCallDriver = cursor.getInt(cursor.getColumnIndex("canCallDriver"));

                taxiSettingInfo.payMoney1 = cursor.getDouble(cursor.getColumnIndex("payMoney1"));
                taxiSettingInfo.payMoney2 = cursor.getDouble(cursor.getColumnIndex("payMoney2"));
                taxiSettingInfo.payMoney3 = cursor.getDouble(cursor.getColumnIndex("payMoney3"));

                taxiSettingInfo.driverRepLowBalance = cursor.getInt(cursor.getColumnIndex("driverRepLowBalance"));
                taxiSettingInfo.passengerDistance = cursor.getInt(cursor.getColumnIndex("passengerDistance"));
                taxiSettingInfo.version = cursor.getInt(cursor.getColumnIndex("version"));

                taxiSettingInfo.grabOrder = cursor.getInt(cursor.getColumnIndex("grabOrder"));
                taxiSettingInfo.distributeOrder = cursor.getInt(cursor.getColumnIndex("distributeOrder"));
                taxiSettingInfo.serviceType = cursor.getString(cursor.getColumnIndex("serviceType"));
            }
        } finally {
            cursor.close();
        }
        return taxiSettingInfo;
    }

    public static void deleteAll() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        db.delete("t_settinginfo", null, null);
    }


}
