package com.easymi.component;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 *
 * @Author: shine
 * Date: 2018/12/24 下午5:00
 * Description:
 * History:
 */

public class Config {

    public static String HOST;


    public static String H5_HOST;

    public static String MQTT_HOST;
    public static boolean IS_ENCRYPT;

    public static int PORT_HTTP;
    public static int PORT_TCP;
    public static String MQTT_TOPIC;
    public static String ACK_TOPIC;
    public static String MQTT_CLIENT_ID;
    public static String MQTT_USER_NAME;
    public static String MQTT_PSW;
    public static String MQTT_CONNECTION_URL;
    public static String MQTT_PARENT_TOPIC;
    public static String MQTT_GROUP_ID;
    public static String APP_KEY;


    /**
     * 上传图片地址.
     */
    public static final String HOST_UP_PIC = "http://up-z2.qiniu.com";

    //是否是通用司机端
    public static final boolean COMM_USE = false;

    /**
     * QQ分享id
     */
    public static String QQ_APP_ID;
    /**
     * 微信分享id
     */
    public static String WX_APP_ID;

    /**
     * 百度TTS_ID
     */
    public static String TTS_APP_ID;
    /**
     * 百度TTS_KEY
     */
    public static String TTS_APP_KEY;
    /**
     * 百度TTS_SECRET
     */
    public static String TTS_APP_SECRET;

//// 人脸识别start

    /**
     * 人脸识别 appid  (虹软)
     */
    public static String FACE_APP_ID = "DdU5KdD96mNGpq949QLxzxa5nFvQoeVBnGkvdi1rXCfY";
    /**
     * 人脸识别 sdk_key  (虹软)
     */
    public static String FACE_SDK_KEY = "AeP1rPdQvo1bY1uL2H8mvPjofGfFwT2D5bR2iqPWH2L7";

    public static String FT_ORIENT = "ftOrient";
/////人脸识别end

    /**
     * 图片服务器地址  线上
     */
    public static String IMG_SERVER;
    public static String IMG_PATH;
    public static String VERSION_NAME;
    public static String VERSION_DATA;

    public static final String MQTT_PUSH_TOPIC = "/driver/gps";

    /**
     * SharedPrefence 常量配置
     */
    public static final String SP_DRIVERID = "driverId";
    public static final String SP_MANUAL_DATA = "manualData";
    public static final String SP_ISLOGIN = "isLogin";
    public static final String SP_DZBUS_ORDER = "isLogin";
    public static final String SP_LAST_LOC = "lastLoc";
    public static final String SP_NAME = "em";

    public static final String PC_BOOKTIME = "pc_booktime";

    public static final String SP_QIYE_CODE = "qiye_code"; //企业编码
    public static final String SP_APP_KEY = "app_key"; //app_key
    public static final String SP_LAT_QIYE_CODE = "last_qiye_code"; //上次的企业编码


    public static final String SP_CONGESTION = "congestion";//躲避拥堵
    public static final String SP_AVOID_HIGH_SPEED = "avoidhightspeed";//不走高速
    public static final String SP_COST = "cost";//避免收费
    public static final String SP_HIGHT_SPEED = "hightspeed";//高速优先

    public static final String SP_USER_LANGUAGE = "user_choice_language";
    public static final int SP_LANGUAGE_AUTO = 0x01;   //语言跟随系统
    public static final int SP_SIMPLIFIED_CHINESE = 0x02; //简体中文
    public static final int SP_TRADITIONAL_CHINESE = 0x03; //繁体中文
    public static final int SP_ENGLISH = 0x04; //英文

    public static final String SP_SYS_LANGUAGE = "sys_language"; //英文


    public static final String SP_VOICE_ABLE = "voice_able";//能否语音播报
    public static final String SP_SHAKE_ABLE = "shake_able";//能否震动
    public static final String SP_ALWAYS_OREN = "always_oren";//是否始终横屏计价
    public static final String SP_DEFAULT_NAVI = "default_navi";//默认启动导航


    public static final String SP_LOGIN_ACCOUNT = "login_account";//账号
    public static final String SP_LOGIN_PSW = "login_psw";//密码
    public static final String SP_REMEMBER_PSW = "remember_psw";//密码

    public static final String SP_UDID = "udid";//密码

    public static final String SP_DAIJIA_LISTEN_ORDER = "daijia_listen_order";//代驾是否听单
    public static final String SP_ZHUANCHE_LISTEN_ORDER = "zhuanche_listen_order";//专车是否听单

    public static final String SP_PLAY_CLIENT_MUSIC = "play_slient_music";//是否播放静音音乐音乐

    public static final String SP_SHOW_GUIDE = "show_guide";//是否播放静音音乐音乐

    public static final String SP_VERSION = "sp_version";
    public static final String SP_LAST_GPS_PUSH_TIME = "sp_last_gps_push_time";//mqtt上次响应的时间点

    //是否只上传GPS类型点
    public static final String SP_GPS_FILTER = "sp_gps_filter";

    /**
     * 业务类型
     */
    public static final String DAIJIA = "daijia";
    public static final String ZHUANCHE = "special";
    public static final String TAXI = "taxi";
    public static final String CITY_LINE = "cityline";
    //定制包车
    public static final String CHARTERED = "chartered";
    //定制租车
    public static final String RENTAL = "rental";
    //客运班车
    public static final String COUNTRY = "country";
    //哈罗客运班车
    public static final String CUSTOMBUS = "custombus";
    //哈罗城际拼车
    public static final String CARPOOL = "carpool";
    //公务用车
    public static final String GOV = "gov";

    //需要导航的模式
    public static final String NAVI_MODE = "navi_mode";
    public static final int DRIVE_TYPE = 1;
    public static final int WALK_TYPE = 2;

    public static final String BROAD_CANCEL_ORDER = "com.easymi.v5driver.BROAD_CANCEL_ORDER";
    public static final String BROAD_BACK_ORDER = "com.easymi.v5driver.BROAD_BACK_ORDER";
    public static final String BROAD_EMPLOY_STATUS_CHANGE = "com.easymi.v5driver.EMPLOY_STATUS_CHANGE";
    public static final String BROAD_NOTICE = "com.easymi.v5driver.BROAD_NOTICE";
    public static final String BROAD_ANN = "com.easymi.v5driver.BROAD_ANN";
    public static final String BROAD_FINISH_ORDER = "com.easymi.v5driver.BROAD_FINISH_ORDER";
    public static final String TIRED_NOTICE = "com.easymi.v5driver.TIRED_NOTICE";
    public static final String ORDER_REFRESH = "com.easymi.v5driver.ORDER_REFRESH";
    public static final String AUTO_FINISH = "com.easymi.v5driver.AUTO_FINISH";
    public static final String SCHEDULE_FINISH = "com.easymi.v5driver.SCHEDULE_FINISH";

    //固定定位时间 毫秒
    public static final int NORMAL_LOC_TIME = 5000;
    //是否纠偏
    public static final boolean NEED_TRACE = false;
    //是否保存位置信息
    public static final boolean SAVE_LOGO = false;

    public static final String SP_TOKEN = "sp_token";

    public static final String AES_PASSWORD = "aes_password";

    public static final String TYPE_RSA = "type:rsa";
    public static final String RSA = "rsa";
    public static final String TYPE = "type";

    public static final String ONLINE_TIME = "online_time";

    public static final String DOWN_TIME = "down_time";

    public static final String SP_YINSI_AGREED = "SP_YINSI_AGREED";


    /////昭通(班车司机端操作方式 1-滑动 2-点击)
    public static final String BUS_IS_BUTTON = "bus_is_buttom";

}
