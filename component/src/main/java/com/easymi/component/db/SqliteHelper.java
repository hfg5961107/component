package com.easymi.component.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "data.db";

    private static final int VERSION = 194;

    private StringBuffer sqlBuf;

    private SQLiteDatabase sqlDatabase;

    private static SqliteHelper sqlHelpler;

    private Context context;

    private SqliteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        //创建一个数据库
        sqlBuf = new StringBuffer();
        this.context = context;
    }

    public static void init(Context context) {

        if (sqlHelpler == null) {
            sqlHelpler = new SqliteHelper(context);
        }
    }

    public static SqliteHelper getInstance() {
        if (sqlHelpler == null) {
            throw new NullPointerException(
                    "SqliteHelper init function not call");
        }
        return sqlHelpler;
    }

    public synchronized SQLiteDatabase openSqliteDatabase()
            throws SQLiteException {
        if (sqlDatabase != null) {
            if ((sqlDatabase.isDbLockedByCurrentThread())) {
                try {
                    Thread.sleep(0xa);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return sqlDatabase;
        }
        sqlDatabase = sqlHelpler.getReadableDatabase();
        return sqlDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDriverInfoTable(db);
        createDymTable(db);
        createSettingTable(db);
        createZCSettingTable(db);
        createVehicleInfoTable(db);
        createSystemTable(db);
        createOrderCustomer(db);

        createBusStation(db);
        createCPOrderCustomer(db);
        createHandlePojo(db);
        createTempMessage(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "t_driverinfo");
        createDriverInfoTable(db);

        db.execSQL("DROP TABLE IF EXISTS " + "t_dyminfo");
        createDymTable(db);

        db.execSQL("DROP TABLE IF EXISTS " + "t_settinginfo");
        createSettingTable(db);

        db.execSQL("DROP TABLE IF EXISTS " + "t_zc_settinginfo");
        createZCSettingTable(db);

        db.execSQL("DROP TABLE IF EXISTS " + "t_vehicle");
        createVehicleInfoTable(db);

        db.execSQL("DROP TABLE IF EXISTS " + "t_systemconfig");
        createSystemTable(db);

        db.execSQL("DROP TABLE IF EXISTS " + "t_zx_order_customer");
        createOrderCustomer(db);

        db.execSQL("DROP TABLE IF EXISTS " + "t_bus_order_station");
        createBusStation(db);

        db.execSQL("DROP TABLE IF EXISTS " + "t_cp_order_customer");
        createCPOrderCustomer(db);

        db.execSQL("DROP TABLE IF EXISTS " + "t_cp_handle_pojo");
        createHandlePojo(db);

        db.execSQL("DROP TABLE IF EXISTS " + "t_cp_temp_message");
        createTempMessage(db);
    }

    private void createCPOrderCustomer(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_cp_order_customer").append(" (")
                .append("id").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("orderId").append(" ").append("LONG").append(",")
                .append("bookTime").append(" ").append("LONG").append(",")
                .append("passengerId").append(" ").append("LONG").append(",")
                .append("orderType").append(" ").append("TEXT").append(",")
                .append("passengerName").append(" ").append("TEXT").append(",")
                .append("passengerPhone").append(" ").append("TEXT").append(",")
                .append("avatar").append(" ").append("TEXT").append(",")
                .append("startAddr").append(" ").append("TEXT").append(",")
                .append("orderRemark").append(" ").append("TEXT").append(",")
                .append("endAddr").append(" ").append("TEXT").append(",")
                .append("startLat").append(" ").append("DOUBLE").append(",")
                .append("startLng").append(" ").append("DOUBLE").append(",")
                .append("money").append(" ").append("DOUBLE").append(",")
                .append("endLat").append(" ").append("DOUBLE").append(",")
                .append("endLng").append(" ").append("DOUBLE").append(",")
                .append("acceptSequence").append(" ").append("INTEGER").append(",")
                .append("sendSequence").append(" ").append("INTEGER").append(",")
                .append("num").append(" ").append("INTEGER").append(",")
                .append("advanceAssign").append(" ").append("INTEGER").append(",")
                .append("customeStatus").append(" ").append("INTEGER").append(",")
                .append("created").append(" ").append("LONG").append(",")
                .append("lineName").append(" ").append("TEXT").append(",")
                .append("lineType").append(" ").append("INTEGER").append(",")
                .append("ticketNumber").append(" ").append("INTEGER").append(",")
                .append("timeSlot").append(" ").append("TEXT").append(",")
                .append("status").append(" ").append("INTEGER").append(",")
                .append("scheduleId").append(" ").append("LONG").append(",")
                .append("waitMinute").append(" ").append("INTEGER").append(",")
                .append("isContract").append(" ").append("INTEGER").append(",")

                .append("subStatus").append(" ").append("INTEGER")
                .append(");");
        execCreateTableSQL(db);
    }


    private void createHandlePojo(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_cp_handle_pojo").append(" (")
                .append("id").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("orderId").append(" ").append("LONG").append(",")
                .append("doAction").append(" ").append("TEXT").append(",")
                .append("serviceType").append(" ").append("TEXT")
                .append(");");
        execCreateTableSQL(db);
    }


    private void createTempMessage(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_cp_temp_message").append(" (")
                .append("id").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("timeStamp").append(" ").append("LONG").append(",")
                .append("data").append(" ").append("TEXT")
                .append(");");
        execCreateTableSQL(db);
    }

    private void createDriverInfoTable(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_driverinfo").append(" (")
                .append("id").append(" INTEGER PRIMARY KEY, ")
                .append("userName").append(" ").append("TEXT").append(",")
                .append("nickName").append(" ").append("TEXT").append(",")
                .append("realName").append(" ").append("TEXT").append(",")
                .append("sex").append(" ").append("INTEGER").append(",")
                .append("commissionStatus").append(" ").append("INTEGER").append(",")
                .append("driverType").append(" ").append("INTEGER").append(",")
                .append("phone").append(" ").append("TEXT").append(",")
                .append("portraitPath").append(" ").append("TEXT").append(",")
                .append("serviceType").append(" ").append("TEXT").append(",")
                .append("status").append(" ").append("TEXT").append(",")
                .append("companyId").append(" ").append("LONG").append(",")
                .append("driverCompanyId").append(" ").append("LONG").append(",")
                .append("deviceNo").append(" ").append("TEXT").append(",")
                .append("token").append(" ").append("TEXT").append(",")
                .append("balance").append(" ").append("DOUBLE").append(",")
                .append("qrCodeUrl").append(" ").append("TEXT").append(",")
                .append("serviceTel").append(" ").append("TEXT").append(",")
                .append("star").append(" ").append("LONG")
                .append(");");
        execCreateTableSQL(db);
    }

    private void createDymTable(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_dyminfo").append(" (")
                .append("id").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("orderId").append(" ").append("LONG").append(",")
                .append("serviceType").append(" ").append("TEXT").append(",")
                .append("startFee").append(" ").append("DOUBLE").append(",")
                .append("waitTime").append(" ").append("INTEGER").append(",")
                .append("waitTimeFee").append(" ").append("DOUBLE").append(",")
                .append("travelTime").append(" ").append("INTEGER").append(",")
                .append("passengerId").append(" ").append("LONG").append(",")
                .append("travelFee").append(" ").append("DOUBLE").append(",")
                .append("totalFee").append(" ").append("DOUBLE").append(",")
                .append("disFee").append(" ").append("DOUBLE").append(",")
                .append("distance").append(" ").append("DOUBLE").append(",")

                .append("paymentFee").append(" ").append("DOUBLE").append(",")
                .append("extraFee").append(" ").append("DOUBLE").append(",")
                .append("remark").append(" ").append("TEXT").append(",")
                .append("couponFee").append(" ").append("DOUBLE").append(",")
                .append("orderTotalFee").append(" ").append("DOUBLE").append(",")
                .append("orderShouldPay").append(" ").append("DOUBLE").append(",")
                .append("prepay").append(" ").append("DOUBLE").append(",")

                .append("addedFee").append(" ").append("DOUBLE").append(",")
                .append("addedKm").append(" ").append("INTEGER").append(",")

                .append("minestMoney").append(" ").append("DOUBLE").append(",")

                //添加专车收费
                .append("peakCost").append(" ").append("DOUBLE").append(",")
                .append("nightPrice").append(" ").append("DOUBLE").append(",")
                .append("lowSpeedCost").append(" ").append("DOUBLE").append(",")
                .append("lowSpeedTime").append(" ").append("INTEGER").append(",")
                .append("peakMile").append(" ").append("DOUBLE").append(",")
                .append("nightTime").append(" ").append("INTEGER").append(",")
                .append("nightMile").append(" ").append("DOUBLE").append(",")
                .append("nightTimePrice").append(" ").append("DOUBLE").append(",")
                .append("nightMileFee").append(" ").append("DOUBLE").append(",")

                .append("stageArrays").append(" ").append("TEXT").append(",")

                .append("orderNo").append(" ").append("TEXT").append(",")
                .append("orderType").append(" ").append("TEXT").append(",")

                .append("orderStatus").append(" ").append("INTEGER")
                .append(");");
        execCreateTableSQL(db);
    }

    private void createSettingTable(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_settinginfo").append(" (")
                .append("id").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("isPaid").append(" ").append("INTEGER").append(",")
                .append("isExpenses").append(" ").append("INTEGER").append(",")
                .append("canCancelOrder").append(" ").append("INTEGER").append(",")
                .append("isAddPrice").append(" ").append("INTEGER").append(",")
                .append("isWorkCar").append(" ").append("INTEGER").append(",")
                .append("employChangePrice").append(" ").append("INTEGER").append(",")
                .append("doubleCheck").append(" ").append("INTEGER").append(",")
                .append("canCallDriver").append(" ").append("INTEGER").append(",")
                .append("payMoney1").append(" ").append("DOUBLE").append(",")
                .append("payMoney2").append(" ").append("DOUBLE").append(",")
                .append("payMoney3").append(" ").append("DOUBLE").append(",")

                .append("driverRepLowBalance").append(" ").append("INTEGER").append(",")
                .append("passengerDistance").append(" ").append("INTEGER").append(",")
                .append("version").append(" ").append("INTEGER").append(",")

                .append("grabOrder").append(" ").append("INTEGER").append(",")
                .append("distributeOrder").append(" ").append("INTEGER").append(",")
                .append("serviceType").append(" ").append("TEXT").append(",")

                .append("workCarChangeOrder").append(" ").append("INTEGER").append(",")
                .append("employChangeOrder").append(" ").append("employChangeOrder")
                .append(");");
        execCreateTableSQL(db);
    }

    private void createZCSettingTable(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_zc_settinginfo").append(" (")
                .append("id").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("isPaid").append(" ").append("INTEGER").append(",")
                .append("isExpenses").append(" ").append("INTEGER").append(",")
                .append("emploiesKm").append(" ").append("DOUBLE").append(",")
                .append("canCancelOrder").append(" ").append("INTEGER").append(",")
                .append("isAddPrice").append(" ").append("INTEGER").append(",")
                .append("employChangePrice").append(" ").append("INTEGER").append(",")

                .append("driverRepLowBalance").append(" ").append("INTEGER").append(",")
                .append("passengerDistance").append(" ").append("INTEGER").append(",")
                .append("version").append(" ").append("INTEGER").append(",")

                .append("grabOrder").append(" ").append("INTEGER").append(",")
                .append("distributeOrder").append(" ").append("INTEGER").append(",")
                .append("serviceType").append(" ").append("TEXT").append(",")

                .append("unStartCancel").append(" ").append("INTEGER").append(",")
                .append("goToCancel").append(" ").append("INTEGER").append(",")
                .append("arriveCancel").append(" ").append("INTEGER").append(",")
                .append("arriveTime").append(" ").append("LONG").append(",")
                .append("isRepairOrder").append(" ").append("INTEGER").append(",")
                .append("driverOrder").append(" ").append("INTEGER").append(",")

                .append("operationMode").append(" ").append("INTEGER").append(",")

                .append("employChangeOrder").append(" ").append("INTEGER")
                .append(");");
        execCreateTableSQL(db);
    }

    private void createVehicleInfoTable(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_vehicle").append(" (")
                .append("employId").append(" INTEGER PRIMARY KEY, ")
                .append("vehicleId").append(" ").append("LONG").append(",")
                .append("companyId").append(" ").append("LONG").append(",")
                .append("vehicleBrand").append(" ").append("TEXT").append(",")
                .append("vehicleColor").append(" ").append("TEXT").append(",")
                .append("vehicleModel").append(" ").append("LONG").append(",")
                .append("plateColor").append(" ").append("TEXT").append(",")
                .append("vehicleNo").append(" ").append("TEXT").append(",")
                .append("vehicleType").append(" ").append("TEXT").append(",")
                .append("commercialType").append(" ").append("INTEGER").append(",")
                .append("isTaxiNormal").append(" ").append("INTEGER").append(",")

                .append("serviceType").append(" ").append("TEXT")
                .append(");");
        execCreateTableSQL(db);
    }

    private void createSystemTable(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_systemconfig").append(" (")
                .append("id").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("tixianBase").append(" ").append("DOUBLE").append(",")
                .append("tixianMin").append(" ").append("DOUBLE").append(",")
                .append("tixianMax").append(" ").append("DOUBLE").append(",")
                .append("tixianMemo").append(" ").append("TEXT").append(",")
                .append("payMoney1").append(" ").append("DOUBLE").append(",")
                .append("payMoney2").append(" ").append("DOUBLE").append(",")
                .append("payMoney3").append(" ").append("DOUBLE").append(",")
                .append("payType").append(" ").append("TEXT").append(",")

                .append("canCallDriver").append(" ").append("INTEGER")
                .append(");");
        execCreateTableSQL(db);
    }

    private void createOrderCustomer(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_zx_order_customer").append(" (")
                .append("id").append(" INTEGER PRIMARY KEY, ")
                .append("customerId").append(" ").append("LONG").append(",")
                .append("orderId").append(" ").append("LONG").append(",")
                .append("orderType").append(" ").append("TEXT").append(",")
                .append("name").append(" ").append("TEXT").append(",")
                .append("phone").append(" ").append("TEXT").append(",")
                .append("startAddr").append(" ").append("TEXT").append(",")
                .append("endAddr").append(" ").append("TEXT").append(",")
                .append("startLat").append(" ").append("DOUBLE").append(",")
                .append("startLng").append(" ").append("DOUBLE").append(",")
                .append("endLat").append(" ").append("DOUBLE").append(",")
                .append("endLng").append(" ").append("DOUBLE").append(",")
                .append("appointTime").append(" ").append("LONG").append(",")
                .append("acceptSequence").append(" ").append("INTEGER").append(",")
                .append("sendSequence").append(" ").append("INTEGER").append(",")
                .append("num").append(" ").append("INTEGER").append(",")
                .append("status").append(" ").append("INTEGER").append(",")
                .append("photo").append(" ").append("TEXT").append(",")
                .append("ticketNumber").append(" ").append("INTEGER").append(",")
                .append("subStatus").append(" ").append("INTEGER")
                .append(");");
        execCreateTableSQL(db);
    }

    private void createBusStation(SQLiteDatabase db) {
        sqlBuf.append("CREATE TABLE ").append("t_bus_order_station").append(" (")
                .append("growId").append(" INTEGER PRIMARY KEY, ")
                .append("id").append(" ").append("INTEGER").append(",")
                .append("name").append(" ").append("TEXT").append(",")
                .append("address").append(" ").append("TEXT").append(",")
                .append("longitude").append(" ").append("DOUBLE").append(",")
                .append("latitude").append(" ").append("DOUBLE").append(",")
                .append("scheduleId").append(" ").append("LONG").append(",")
                .append("orderType").append(" ").append("TEXT").append(",")
                .append("status").append(" ").append("INTEGER").append(",")
                .append("waitTime").append(" ").append("LONG")
                .append(");");
        execCreateTableSQL(db);
    }


    private void execCreateTableSQL(SQLiteDatabase db) {
        db.execSQL(sqlBuf.toString());
        sqlBuf.setLength(0x0);
    }

    /**
     * 删除数据库
     */
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DB_NAME);
    }

    public List<String> findAllTableName(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", new String[]{});

        List<String> list = new LinkedList<>();

        try {
            while (cursor.moveToNext()) {
                String table = cursor.getString(0);
                list.add(table);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

}
