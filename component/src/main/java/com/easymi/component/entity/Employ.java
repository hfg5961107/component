package com.easymi.component.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.easymi.component.utils.Log;

import com.easymi.component.db.SqliteHelper;
import com.easymi.component.utils.AesUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 *
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */
public class Employ {
    public long id;

    /**
     * 工号
     */
    public String userName;


    public int commissionStatus;

    /**
     * 昵称
     */
    public String nickName;

    /**
     * 真实姓名
     */
    public String realName;

    /**
     * 性别 1，男；2，女；3，未知
     */
    public int sex;

    /**
     * 手机号
     */
    public String phone;

    /**
     * 头像照片地址
     */
    public String portraitPath;

    /**
     * 服务类型
     */
    public String serviceType;


    public int driverType;

    /**
     * 司机状态（0离线 5在线 10空闲 15派单 20接单 25前往预约地 30到达预约地 35前往目的地 40中途等待  45冻结）
     * 司机状态 1离线 2空闲 5派单 10接单 15前往预约地 20到达预约地 25前往目的地 28中途等待 45冻结   修改后的
     * 司机状态 1离线 2登陆未听单 3上线听单中（空闲） 5派单 10接单 15前往预约地 20到达预约地 25前往目的地 28中途等待 45冻结   再次修改后的
     **/
    public int status;

    /**
     * 所属公司Id
     */
    public long companyId;

    /**
     * 设备编号
     */
    public String deviceNo;

    /**
     * 登录token
     */
    public String token;

    /**
     * 余额
     */
    public double balance;

    /**
     * 司机平均星级
     */
    public double star;

    /**
     * 司机名片地址host
     */
    public String qrCodeUrl;

    /**
     * 客服电话
     */
    public String serviceTel;

    public long driverCompanyId;


    public void updateDriverCompanyId() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        ContentValues values = new ContentValues();
        values.put("driverCompanyId", companyId);

        boolean flag = db.update("t_driverinfo", encryptString(values), " id = ? ",
                new String[]{String.valueOf(id)}) == 1;
    }

    /**
     * 保存数据
     */
    public boolean save() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("userName", userName);
        values.put("nickName", nickName);
        values.put("realName", realName);
        values.put("sex", sex);
        values.put("commissionStatus", commissionStatus);
        values.put("phone", phone);
        values.put("portraitPath", portraitPath);
        values.put("serviceType", serviceType);
        values.put("driverType", driverType);
        values.put("status", status);
        values.put("companyId", companyId);
        values.put("deviceNo", deviceNo);
        values.put("token", token);
        values.put("balance", balance);
        values.put("qrCodeUrl", qrCodeUrl);
        values.put("serviceTel", serviceTel);
        values.put("star", star);
        Log.e("Employ", "save");
        boolean flag = db.insert("t_driverinfo", null, encryptString(values)) != -1;
        Log.e("Employ", "save+  " + flag);
        return flag;
    }

    /**
     * 加密string字符串
     *
     * @param values
     * @return
     */
    private ContentValues encryptString(ContentValues values) {
        for (Map.Entry<String, Object> item : values.valueSet()) {
            String key = item.getKey();
            Object value = item.getValue();
            if (value instanceof String) {
                value = AesUtil.aesEncrypt(AesUtil.AAAAA, (String) item.getValue());
                values.put(key, (String) value);
            }
        }
        return values;
    }

    /**
     * 判断司机是否存在
     */
    public static boolean exists(Long driverID) {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        Cursor cursor = db.rawQuery("select count(*) from t_driverinfo where id = ? ", new String[]{String.valueOf(driverID)});
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
     * 根据ID查询数据
     */
    public static Employ findByID(Long driverID) {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        Cursor cursor = db.rawQuery("select * from t_driverinfo where id = ?", new String[]{String.valueOf(driverID)});
        Employ driverInfo = null;
        try {
            if (cursor.moveToFirst()) {
                driverInfo = cursorToEmploy(cursor);
            }
        } catch (Exception e) {
//			CrashReport.setUserSceneTag();
        } finally {
            cursor.close();
        }
        return driverInfo;
    }

    /**
     * main进程 查询所有司机
     *
     * @return
     */
    public static List<Employ> findAll() {

        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();

        Cursor cursor = db.rawQuery("select * from t_driverinfo", new String[]{});

        List<Employ> list = new LinkedList<>();

        try {
            while (cursor.moveToNext()) {
                Employ employ = cursorToEmploy(cursor);
                list.add(employ);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    /**
     * 其他进程 查询所有司机
     *
     * @return
     */
    public static List<Employ> findAll(SqliteHelper helper) {

        SQLiteDatabase db = helper.openSqliteDatabase();

        Cursor cursor = db.rawQuery("select * from t_driverinfo", new String[]{});

        List<Employ> list = new LinkedList<>();

        try {
            while (cursor.moveToNext()) {
                Employ employ = cursorToEmploy(cursor);
                list.add(employ);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public static Employ cursorToEmploy(Cursor cursor) {
        Employ driverInfo = new Employ();

        driverInfo.id = cursor.getLong(cursor.getColumnIndex("id"));
        driverInfo.userName = cursor.getString(cursor
                .getColumnIndex("userName"));
        driverInfo.nickName = cursor.getString(cursor
                .getColumnIndex("nickName"));
        driverInfo.commissionStatus = cursor.getInt(cursor
                .getColumnIndex("commissionStatus"));
        driverInfo.realName = cursor.getString(cursor
                .getColumnIndex("realName"));
        driverInfo.sex = cursor.getInt(cursor.getColumnIndex("sex"));
        driverInfo.phone = cursor.getString(cursor
                .getColumnIndex("phone"));
        driverInfo.portraitPath = cursor.getString(cursor
                .getColumnIndex("portraitPath"));
        driverInfo.serviceType = cursor.getString(cursor
                .getColumnIndex("serviceType"));
        driverInfo.driverType = cursor.getInt(cursor.getColumnIndex("driverType"));
        driverInfo.status = cursor.getInt(cursor.getColumnIndex("status"));
        driverInfo.companyId = cursor.getLong(cursor
                .getColumnIndex("companyId"));
        driverInfo.driverCompanyId = cursor.getLong(cursor
                .getColumnIndex("driverCompanyId"));
        driverInfo.deviceNo = cursor.getString(cursor
                .getColumnIndex("deviceNo"));
        driverInfo.token = cursor.getString(cursor
                .getColumnIndex("token"));
        driverInfo.balance = cursor.getDouble(cursor
                .getColumnIndex("balance"));
        driverInfo.qrCodeUrl = cursor.getString(cursor.getColumnIndex("qrCodeUrl"));
        driverInfo.serviceTel = cursor.getString(cursor.getColumnIndex("serviceTel"));
        driverInfo.star = cursor.getLong(cursor.getColumnIndex("star"));
        return decrptyString(driverInfo);
    }

    /**
     * 根据ID修改数据
     */
    public boolean updateAll() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        ContentValues values = new ContentValues();

        values.put("id", id);
        values.put("userName", userName);
        values.put("nickName", nickName);
        values.put("realName", realName);
        values.put("sex", sex);
        values.put("phone", phone);
        values.put("portraitPath", portraitPath);
        values.put("serviceType", serviceType);
        values.put("commissionStatus", commissionStatus);
        values.put("driverType", driverType);
        values.put("status", status);
        values.put("companyId", companyId);
        values.put("deviceNo", deviceNo);
        values.put("token", token);
        values.put("balance", balance);
        values.put("serviceTel", serviceTel);
        values.put("qrCodeUrl", qrCodeUrl);
        values.put("star", star);
        Log.e("Employ", "updateAll");

        boolean flag = db.update("t_driverinfo", encryptString(values), " id = ? ",
                new String[]{String.valueOf(id)}) == 1;
        Log.e("Employ", "updateAll+  " + flag);
        return flag;
    }

    public boolean saveOrUpdate() {
        if (exists(this.id)) {
            return this.updateAll();
        } else {
            return this.save();
        }
    }

    public Employ() {

    }

    /**
     * 解密String
     *
     * @param employ
     * @return
     */
    private static Employ decrptyString(Employ employ) {
        employ.userName = AesUtil.aesDecrypt(AesUtil.AAAAA, employ.userName);
        employ.nickName = AesUtil.aesDecrypt(AesUtil.AAAAA, employ.nickName);
        employ.realName = AesUtil.aesDecrypt(AesUtil.AAAAA, employ.realName);
        employ.phone = AesUtil.aesDecrypt(AesUtil.AAAAA, employ.phone);
        employ.portraitPath = AesUtil.aesDecrypt(AesUtil.AAAAA, employ.portraitPath);
        employ.serviceType = AesUtil.aesDecrypt(AesUtil.AAAAA, employ.serviceType);
        employ.deviceNo = AesUtil.aesDecrypt(AesUtil.AAAAA, employ.deviceNo);
        employ.token = AesUtil.aesDecrypt(AesUtil.AAAAA, employ.token);
        employ.qrCodeUrl = AesUtil.aesDecrypt(AesUtil.AAAAA, employ.qrCodeUrl);
        employ.serviceTel = AesUtil.aesDecrypt(AesUtil.AAAAA, employ.serviceTel);
        return employ;
    }
}
