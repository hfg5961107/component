package com.easymi.component.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easymi.component.db.SqliteHelper;

public class HandleBean {

    public static void save(long orderId, String serviceType, String doAction) {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        ContentValues values = new ContentValues();
        values.put("orderId", orderId);
        values.put("serviceType", serviceType);
        values.put("doAction", doAction);
        db.insert("t_cp_handle_pojo", null, values);
    }

    public static void deleteAll() {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        db.delete("t_cp_handle_pojo", null, null);
    }

    public static void delete(long orderId, String serviceType) {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        db.delete("t_cp_handle_pojo", "orderId = ? and serviceType = ?", new String[]{String.valueOf(orderId), serviceType});
    }

    public static boolean exists(long id, String serviceType, String doAction) {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        Cursor cursor = db.rawQuery(
                "select count(*) from t_cp_handle_pojo where orderId = ? and serviceType = ? and doAction = ?",
                new String[]{String.valueOf(id), serviceType, doAction});
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

    public static boolean exists(long id, String serviceType) {
        SqliteHelper helper = SqliteHelper.getInstance();
        SQLiteDatabase db = helper.openSqliteDatabase();
        Cursor cursor = db.rawQuery(
                "select count(*) from t_cp_handle_pojo where orderId = ? and serviceType = ?",
                new String[]{String.valueOf(id), serviceType});
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

}