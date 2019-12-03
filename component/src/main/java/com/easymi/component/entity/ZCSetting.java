package com.easymi.component.entity;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.easymi.component.utils.Log;

import com.easymi.component.db.SqliteHelper;
import com.google.gson.annotations.SerializedName;

public class ZCSetting {

    //附近司机推荐距离
    @SerializedName("driverDistance")
    public double emploiesKm;

    //代付（1开启，2关闭)
    @SerializedName("driverRepPay")
    public int isPaid;

    //是否允许调价（1开启，2关闭)
    public int isAddPrice = 2;

    //确认费用时是否能加垫付费之类的
    @SerializedName("driverAddCharge")
    public int employChangePrice;

    @SerializedName("driverReimbursCharge")
    public int isExpenses;//报销（1开启，2关闭）

    //是否能消单
    @SerializedName("driverCancelOrder")
    public int canCancelOrder;

    @SerializedName("employChangeOrder")
    public int employChangeOrder;//是否可以转单（1开启，2关闭)

    public int driverRepLowBalance; //允许代付时余额不足  //现在用的1开启，2关闭
    public int passengerDistance;
    public int version;

    public int grabOrder; //抢单开关
    public int distributeOrder; //派单开关
    public String serviceType; //业务类型

    /**
     * 行程未开始销单
     */
    public int unStartCancel;

    /**
     * 前往预约地销单
     */
    public int goToCancel;

    /**
     * 到达预约地销单
     */
    public int arriveCancel;

    /**
     * 到达预约地销单时间
     */
    public long arriveTime;

    /**
     * 拼车补单开关 1是开启 2是关闭 默认关闭
     */
    public int isRepairOrder;

    /**
     * 拼车操纵是按钮还是滑动
     */
    public int operationMode;


    public int driverOrder;




    public static void deleteAll() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        db.delete("t_zc_settinginfo", null, null);
    }

    public void save() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        db.delete("t_zc_settinginfo", null, null);//先删除再创建
        ContentValues values = new ContentValues();
        values.put("isPaid", isPaid);
        values.put("isExpenses", isExpenses);
        values.put("canCancelOrder", canCancelOrder);
        values.put("isAddPrice", isAddPrice);
        values.put("employChangePrice", employChangePrice);
        values.put("employChangeOrder", employChangeOrder);

        values.put("driverRepLowBalance", driverRepLowBalance);
        values.put("passengerDistance", passengerDistance);
        values.put("version", version);

        values.put("grabOrder", grabOrder);
        values.put("distributeOrder", distributeOrder);
        values.put("serviceType", serviceType);

        values.put("unStartCancel", unStartCancel);
        values.put("goToCancel", goToCancel);
        values.put("arriveCancel", arriveCancel);
        values.put("arriveTime", arriveTime);
        values.put("isRepairOrder", isRepairOrder);
        values.put("driverOrder",driverOrder);
        values.put("emploiesKm", emploiesKm);

        values.put("operationMode", operationMode);

        db.insert("t_zc_settinginfo", null, values);
    }

    public static ZCSetting findOne() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        Cursor cursor = db.rawQuery("select * from t_zc_settinginfo", null);
        ZCSetting settingInfo = new ZCSetting();
        try {
            if (cursor.moveToFirst()) {
                settingInfo.isPaid = cursor.getInt(cursor.getColumnIndex("isPaid"));
                settingInfo.isExpenses = cursor.getInt(cursor.getColumnIndex("isExpenses"));
                settingInfo.canCancelOrder = cursor.getInt(cursor.getColumnIndex("canCancelOrder"));
                settingInfo.isAddPrice = cursor.getInt(cursor.getColumnIndex("isAddPrice"));
                settingInfo.employChangePrice = cursor.getInt(cursor.getColumnIndex("employChangePrice"));
                settingInfo.employChangeOrder = cursor.getInt(cursor.getColumnIndex("employChangeOrder"));

                settingInfo.driverRepLowBalance = cursor.getInt(cursor.getColumnIndex("driverRepLowBalance"));
                settingInfo.passengerDistance = cursor.getInt(cursor.getColumnIndex("passengerDistance"));
                settingInfo.version = cursor.getInt(cursor.getColumnIndex("version"));

                settingInfo.grabOrder = cursor.getInt(cursor.getColumnIndex("grabOrder"));
                settingInfo.distributeOrder = cursor.getInt(cursor.getColumnIndex("distributeOrder"));
                settingInfo.serviceType = cursor.getString(cursor.getColumnIndex("serviceType"));

                settingInfo.unStartCancel = cursor.getInt(cursor.getColumnIndex("unStartCancel"));
                settingInfo.goToCancel = cursor.getInt(cursor.getColumnIndex("goToCancel"));
                settingInfo.arriveCancel = cursor.getInt(cursor.getColumnIndex("arriveCancel"));
                settingInfo.arriveTime = cursor.getLong(cursor.getColumnIndex("arriveTime"));
                settingInfo.isRepairOrder = cursor.getInt(cursor.getColumnIndex("isRepairOrder"));
                settingInfo.driverOrder = cursor.getInt(cursor.getColumnIndex("driverOrder"));
                settingInfo.emploiesKm = cursor.getInt(cursor.getColumnIndex("emploiesKm"));

                settingInfo.operationMode = cursor.getInt(cursor.getColumnIndex("operationMode"));
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            Log.e("ZCSetting", "catch   ");
        } finally {
            cursor.close();
        }
        return settingInfo;
    }


}
