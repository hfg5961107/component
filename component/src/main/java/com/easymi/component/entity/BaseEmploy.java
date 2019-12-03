//package com.easymi.component.entity;
//
//import android.content.ContentValues;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.easymi.component.Config;
//import com.easymi.component.app.XApp;
//import com.easymi.component.db.SqliteHelper;
//import com.easymi.component.utils.Log;
//import com.google.gson.annotations.SerializedName;
//
///**
// * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
// * FileName:
// * @Author: shine
// * Date: 2018/12/24 下午1:10
// * Description:
// * History:
// */
//
//public class BaseEmploy {
//    public long id;
//
////   司机状态 1离线 2登陆未听单 3上线听单中（空闲） 5派单 10接单 15前往预约地 20到达预约地 25前往目的地 28中途等待 45冻结   再次修改后的
//    public int status;
//
////    public String real_name;//真实姓名
//    public long company_id;
//    public String phone;
//    public String child_type;//服务子类型
//
//    /**
//     * 根据ID修改数据
//     */
//    public boolean updateBase() {
//        SqliteHelper helper = SqliteHelper.getInstance();
//        SQLiteDatabase db = helper.openSqliteDatabase();
//        ContentValues values = new ContentValues();
//        values.put("id", id);
////        values.put("real_name", real_name);
//        values.put("company_id", company_id);
//        values.put("phone", phone);
//        values.put("child_type", child_type);
//        values.put("status", status);
//
//        boolean flag = db.update("t_driverinfo", values, " id = ? ",
//                new String[]{String.valueOf(id)}) == 1;
//        return flag;
//    }
//
//    public BaseEmploy employ2This() {
//        return Employ.findByID(XApp.getMyPreferences().getLong(Config.SP_DRIVERID, -1));
////        if (employ == null) {
////            Log.e("TAG", "查询为null");
////            return null;
////        } else {
////            id = employ.id;
////            status = employ.status;
////            real_name = employ.real_name;
////            company_id = employ.company_id;
////            phone = employ.phone;
////            child_type = employ.child_type;
////            return employ;
////        }
//    }
//}
