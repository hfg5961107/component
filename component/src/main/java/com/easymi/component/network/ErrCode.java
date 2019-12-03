package com.easymi.component.network;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 *
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public enum ErrCode {


    SUCCESS(1, "成功"),
    /**
     * 验证格式出错
     */
    FORMAT_ERR(10000, "验证格式出错"),

    /**
     * 验证码不能为空
     */
    CODE_IS_NULL_ERR(11000, "验证码不能为空"),
    /**
     * 通用-用户名格式错误
     */
    USERNAME_FORMAT_ERR(10001, "用户名格式错误"),
    /**
     * 通用-密码格式错误
     */
    PASSWORD_FORMAT_ERR(10002, "密码格式错误"),
    /**
     * 通用-验证码格式错误
     */
    CODE_FORMAT_ERR(10003, "验证码格式错误"),
    /**
     * 通用-参数不能为空
     */
    PARAMETER_NULL_FORMAT_ERR(10004, "参数不能为空"),
    /**
     * 通用-参数格式错误
     */
    PARAMETER_FORMAT_ERR(10005, "参数格式错误"),
    /**
     * 通用-参数id为空
     */
    ID_NULL_FORMAT_ERR(10006, "参数id为空"),
    /**
     * 通用-appKey为空
     */
    APPKEY_NULL_FORMAT_ERR(10007, "appKey为空"),
    /**
     * 通用-验证码Id格式错误
     */
    CODE_ID_FORMAT_ERR(10008, "验证码Id格式错误"),

    /**
     * 通用-包含不安全的字符
     */
    CONTAINS_NOT_SAFE_CHARACTER(10009, "包含不安全的字符"),
    /**
     * 通用-公司id为空
     */
    COMPANY_ID_ISNULL_CHARACTER(10010, "公司id为空"),
    /**
     * 通用-page为空
     */
    PAGE_NULL_FORMAT_ERR(10011, "page为空"),
    /**
     * 通用-size为空
     */
    SIZE_NULL_FORMAT_ERR(100012, "size为空"),
    /**
     * 数据重复
     */
    DATA_REPEATED(100013, "数据重复"),
    /**
     * 数据不存在
     */
    DATA_NOT_EXIST(100014, "数据不存在"),

    /**
     * 添加错误
     */
    INSERT_VALIDATE_ERR(40001, "添加错误"),
    /**
     * 更新错误
     */
    UPDATE_VALIDATE_ERR(40002, "更新错误"),
    /**
     * 删除错误
     */
    DELETE_VALIDATE_ERR(40003, "删除错误"),
    /**
     * 查询错误
     */
    QUERY_VALIDATE_ERR(40004, "未查到相关信息哦"),

    /**
     * 数据不存在
     */
    QUERY_VALIDATE_NULL(40005, "数据不存在"),

    /**
     * 用户名或者密码错误
     */
    USERNAME_OR_PASSWORD_ERR(40006, "用户名或者密码错误"),
    /**
     * 用户名错误
     */
    USERNAME_ERR(41000, "用户名错误"),

    /**
     * 该用户被加入黑名单
     */
    USERNAME_BLACK_ERR(41001, "该用户被加入黑名单"),

    /**
     * 图形验证码错误
     */
    CODE_VALIDATE_ERR(40001, "图形验证码错误"),

    /**
     * 密码不相同
     */
    RESET_PASSWOED_ERROR_REGISTERED(40028, "密码不相同"),

    /**
     * token已过期
     */
    TOKEN_EXPIRE_REGISTERED(40026, "token已过期"),

    /**
     * 发送短信验证码错误
     */
    SMS_SEND_CODE_ERR(40038, "发送短信验证码错误"),

    /**
     * 发送短信验证码重复发送错误
     */
    SMS_SEND_REPEAT_CODE_ERR(40039, "发送短信验证码重复发送错误"),
    /**
     * 发送短信验证码type类型错误
     */
    SMS_TYPE_VALIDATE_ERR(40039, "发送短信验证码type类型错误"),

    /**
     * 导出失败
     */
    EXPORT_REPEAT_ERR(40042, "导出失败"),

    /**
     * 司机没有开通专车业务
     */
    DRIVER_NOT_SPECIAL(40043, "司机没有开通专车业务"),

    /**
     * 司机与车辆不要重复绑定
     */
    DRIVER_BINDING_REPEATED(40044, "司机与车辆不要重复绑定"),


    /**
     * 捕获到系统异常，未知错误
     */
    UNKNOWN_ERR(80001, "系统异常"),
    /**
     * 线路不存在
     */
    LINE_NOT_EXIST(40045, "线路不存在"),
    /**
     * 线路ID不能为空
     */
    LINE_ID_NOT_NULL(40046, "线路ID不能为空"),
    /**
     * 订单状态错误
     */
    ORDER_STATUS_ERR(40047, "订单状态错误"),
    /**
     * 订单不存在
     */
    ORDER_IS_NULL_ERR(40048, "订单不存在"),

    /**
     * 司机不存在
     */
    DRIVER_NOT_EXIST(40049, "司机不存在"),
    /**
     * 司机余额操作失败
     */
    DRIVER_BALANCE_ERROR(40050, "司机余额操作失败"),

    /**
     * 支付类型错误
     */
    PAY_TYPE_ERROR(40051, "支付类型错误"),

    /**
     * 终端ip错误
     */
    TERMINAL_IP_ERR(40052, "终端ip错误"),

    /**
     * 加密失败
     */
    ENCODE_ERR(40053, "加密失败"),

    /**
     * 请求失败
     */
    REQUEST_ERR(40054, "请求失败"),

    /**
     * 请求超时
     */
    READ_TIMEOUT_ERR(40055, "请求超时"),

    /**
     * 验签失败
     */
    ENCRYPTING_ERR(40056, "验签失败"),

    /**
     * 转换失败
     */
    CHANGE_ERR(40057, "转换失败"),

    /**
     * 支付操作人错误
     */
    PAY_OPERATOR_ERROR(40058, "支付操作人错误"),

    /**
     * 订单创建失败
     */
    ORDER_CREATE_ERROR(40059, "订单创建失败"),
    /**
     * 订单状态错误
     */
    ORDER_STATUS_ERROR(40060, "订单状态错误"),
    /**
     * 订单派单失败
     */
    ORDER_ASSIGN_ERROR(40061, "订单派单失败"),
    /**
     * 订单抢单失败
     */
    ORDER_GRAB_ERROR(40062, "订单抢单失败"),
    /**
     * 订单接单失败
     */
    ORDER_TAKE_ERROR(40063, "订单接单失败"),
    /**
     * 订单到达预约地失败
     */
    ORDER_GO_TO_BOOK_PLACE_ERROR(40064, "订单到达预约地失败"),
    /**
     * 订单拒单失败
     */
    ORDER_REFUSE_ERROR(40065, "订单拒单失败"),
    /**
     * 订单销单失败
     */
    ORDER_CANCEL_ERROR(40066, "订单销单失败"),

    /**
     * 支付状态错误
     */
    PAY_STATUS_ERR(40067, "支付状态错误"),
    /**
     * 站点被线路使用
     */
    STATION_IS_USED(40068, "站点被线路使用"),

    /**
     * 车牌号已存在
     */
    VEHICLE_NUMBER_EXISTS(40069, "车牌号已存在"),
    /**
     * 站点选择错误
     */
    STATION_CHOOSE_ERROR(40070, "站点选择错误"),
    /**
     * 客户余额不足,不允许下单
     */
    PASSENGER_BALANCE_DEFICIENCY(40071, "余额不足,不允许下单"),
    /**
     * 客户不存在
     */
    PASSENGER_NOT_EXIST(40072, "客户不存在"),
    /**
     * 班次不存在
     */
    SCHEDULE_NOT_EXIST(40073, "班次不存在"),
    /**
     * 班次票数不足
     */
    SCHEDULE_TICKET_DEFICIENCY(40074, "班次票数不足,请联系平台客服补充余票"),

    /**
     * 当前司机未冻结
     */
    DRIVER_UN_FROZEN(40075, "当前司机未冻结"),
    /**
     * 当前司机已被冻结
     */
    DRIVER_FROZEN(40076, "当前司机已被冻结"),
    /**
     * 车辆不存在
     */
    CAR_NOT_EXIST(40077, "车辆不存在"),
    /**
     * 公司不存在
     */
    COMPANY_NOT_EXIST(40078, "公司不存在"),
    /**
     * 支付金额错误
     */
    FEE_ERROR(40079, "支付金额错误"),
    /**
     * 该车辆已经是该线路的常用车辆不能重复添加
     */
    LINE_VEHICLE_EXIST(40080, "该车辆已经是该线路的常用车辆不能重复添加"),
    /**
     * 验证码错误
     */
    PICCODE_ERRO(40081, "验证码错误"),
    /**
     * 申请提现失败
     */
    DRIVER_PUT_FORWARD_ERR(40082, "申请提现失败"),

    /**
     * 提现申请不存在
     */
    PUT_FORWARD_NOT_EXIST(40083, "提现申请不存在"),

    /**
     * 提现申请已经处理
     */
    PUT_FORWARD_HAS_DEAL(40084, "提现申请已经处理"),

    /**
     * 订单报销记录已经存在
     */
    ORDER_REIMBURSE_HAS_EXIST(40085, "订单报销记录已经存在"),

    /**
     * 订单报销不存在
     */
    ORDER_REIMBURSE_NOT_EXIST(40086, "订单报销不存在"),

    /**
     * 订单报销已经处理
     */
    ORDER_REIMBURSE_HAS_DEAL(40087, "订单报销已经处理"),
    /**
     * 司机账户余额不足
     */
    DRIVER_BALANCE_NOT_ENOUGH(40088, "司机账户余额不足"),
    /**
     * 专线订单跳过执行失败
     */
    ORDER_SKIP_ERROR(40089, "专线订单跳过执行失败"),
    /**
     * 专线订单出发失败
     */
    ORDER_RUN_ERROR(40090, "专线订单出发失败"),
    /**
     * 专线订单支付失败
     */
    ORDER_PAY_ERROR(40091, "专线订单支付失败"),
    /**
     * 终止任务失败
     */
    STOP_TASK_ERROR(40092, "终止任务失败"),
    /**
     * 订单已经申请过发票
     */
    ORDER_HAS_INVOICE(40093, "订单已经申请过发票"),
    /**
     * 申请发票失败
     */
    INVOICE_ERR(40094, "申请发票失败"),
    /**
     * 订单完成失败
     */
    ORDER_FINISH_ERROR(40095, "订单完成失败"),
    /**
     * 发票申请已经处理
     */
    INVOICE_HAS_DEAL(40096, "发票申请已经处理"),
    /**
     * 发票申请不存在
     */
    INVOICE_NOT_EXIST(40097, "发票申请不存在"),
    /**
     * 脏数据
     */
    VERSION_ERROR(40098, "问题数据"),

    /**
     * 分布式锁，加锁失败
     */
    LOCK_ERROR(40099, "分布式锁，加锁失败"),

    /**
     * 订单到达预约地失败
     */
    ORDER_ARRIVE_BOOK_PLACE_ERROR(40100, "订单到达预约地失败"),

    /**
     * 订单前往目的地失败
     */
    ORDER_GOTO_DESTINATION_ERROR(40101, "订单前往目的地失败"),

    /**
     * 订单到达目的地失败
     */
    ORDER_ARRIVE_DESTINATION_ERROR(40102, "订单到达目的地失败"),

    /**
     * 主账号不允许修改
     */
    ADMIN_IS_MAIN_UPDATE_ERR(40103, "主账号不允许修改"),
    /**
     * 司机已经存在,不能重复添加
     */
    DRIVER_HAD_EXIST(40104, "司机已经存在,不能重复添加"),

    /**
     * 账号重复
     */
    ACCOUNT_REPEAT_ERR(40105, "账号重复"),

    /**
     * 手机号重复
     */
    PHONE_REPEAT_ERR(40106, "手机号重复"),

    /**
     * 同一区域不能存在多个服务机构
     */
    SERVER_REPEAT_ERR(40107, "同一区域不能存在多个服务机构"),

    /**
     * 司机密码错误
     */
    DRIVER_PASSWORD_ERROR(40108, "司机密码错误"),
    /**
     * 开通服务机构已达到上限
     */
    COMPANY_NUM_MAX_ERROR(40109, "开通服务机构已达到上限"),

    /**
     * 优惠券已使用或已失效
     */
    COUPON_STATUS_ERROR(40110, "优惠券已使用或已失效"),

    /**
     * 优惠券业务类型错误
     */
    COUPON_SERVICE_TYPE_ERROR(40111, "优惠券业务类型错误"),

    /**
     * 优惠券类型错误
     */
    COUPON_TYPE_ERROR(40112, "优惠券类型错误"),
    /**
     * 班次已经存在
     */
    SCHEDULE_IS_EXIST(40113, "班次已经存在"),

    /**
     * 不是超级管理员 没有权限
     */
    ADMIN_IS_NOT_MAIN_ERR(40114, "不是超级管理员 没有权限"),

    /**
     * 自动派单自动抢单不能同时打开
     */
    GRAB_DISTRIBUTE_ORDER(40115, "自动派单自动抢单不能同时打开"),

    /**
     * 车型名称已经存在
     */
    VEHICLE_MODELS_HAS_EXIST(40116, "车型名称已经存在"),

    /**
     * 开通业务名称已经存在
     */
    BUSINESS_TYPE_HAS_EXIST(40117, "开通业务名称已经存在"),

    /**
     * 提成名称已经存在
     */
    ROYALTY_RATIO_HAS_EXIST(40118, "提成名称已经存在"),
    /**
     * 收费标准名称已经存在
     */
    CHARGING_HAS_EXIST(40119, "收费标准名称已经存在"),
    /**
     * 客户电话已经存在
     */
    PASSENGER_PHONE_HAS_EXIST(40120, "客户电话已经存在"),

    /**
     * 客户身份证已经存在
     */
    PASSENGER_ID_CARD_HAS_EXIST(40121, "客户身份证已经存在"),
    /**
     * 当前车型正在使用中
     */
    VEHICLE_MODELS_IN_USE(40122, "当前车型正在使用中"),
    /**
     * 当前类型正在使用中
     */
    BUSINESS_TYPE_IN_USE(40123, "当前类型正在使用中"),
    /**
     * 超级权限不能变更
     */
    ADMIN_UPDATE_MAIN_ERR(40124, "超级权限不能变更"),
    /**
     * 司机重复绑定车辆
     */
    DRIVER_BIND_CAR_REPEATED(40125, "司机重复绑定车辆"),
    /**
     * 司机没有绑定车辆
     */
    DRIVER_NOT_BIND_CAR(40127, "司机没有绑定车辆"),

    /**
     * 司机有正在执行的订单
     */
    DRIVER_HAS_RUNNING_ORDER(40128, "司机有正在执行的订单"),

    /**
     * 司机已在线上
     */
    DRIVER_LOGIN_REPEATED(40129, "司机已在线上"),

    /**
     * 提示该角色正在使用，不能删除
     */
    ROLE_OCCUPATION_ERR(40130, "提示该角色正在使用，不能删除"),

    /**
     * 班次已经停售
     */
    SCHEDULE_STOPPED_SELLING(40132, "班次已经停售"),

    /**
     * 提示该角色正在使用，不能删除
     */
    IS_COMMON_VEHICLE_ERR(40133, "同车司机正在线上不能登录"),

    /**
     * 上线状态，不能注销
     */
    CAN_NOT_DELETE_ONLINE(40135, "您处于听单状态，请停止听单后再注销"),

    /**
     * 还有未完成的订单
     */
    ORDER_NOT_FINISH(40138, "还有未完成的订单"),

    /**
     * 订单已被收回
     */
    ORDER_CANCEL(40139, "订单已被收回"),

    /**
     * 司机状态错误
     */
    DRIVER_STATUS_ERRv(40180, "司机状态错误"),

    /**
     * 乘客账号异常，请联系管理员处理
     */
    PASSENGER_IN_BLACK_LIST(40181, "乘客账号异常，请联系管理员处理"),

    /**
     * 评论包含敏感词
     */
    MOMO_SENSITIVE_ERROR(40205, "评论包含敏感词"),

    START_NO_SERVICE(40300, "当前起始地未开通服务机构"),

    /**
     * 实名认证参数错误
     */
    ID_CARD_PARAMETER_ERR(40704,"实名认证失败,请重新输入"),

    /**
     * 实名认证失败,请重新输入
     */
    ID_CARD_RECOGNITION_FAILURE_ERR(40705,"实名认证失败,请输入本人的真实姓名及真实身份证号码"),

    /**
     * 身份证扫描失败
     */
    ID_CARD_SCAN_FAILED(40706,"身份证扫描失败"),
    /**
     * 当前身份证已认证
     */
    CURRENT_ID_IS_AUTHENTICATED(40708,"信息已认证，请不要重复认证"),

    /**
     * 申请中
     */
    APPLYING(50009, "申请中"),

    /**
     * 车辆座位不足
     */
    VEHICLE_NO_HAVE_SEATS(40194, "车辆座位不足"),

//old
    /**
     * 数据不匹配
     */
    NOT_MATCH(20032, "数据已更新"),
    /**
     * 查询失败
     */
    QUERY_ERROR(30011, "查询失败"),
    /**
     * 该服务人员已存在执行中的订单，不能再次接单
     */
    DRIVER_GOTO_PRE_ORDER_CODE(30050, "您已存在执行中的订单，不能再次接单"),
    /**
     * 差一点就抢到了
     */
    GRAB_ORDER_ERROR(30051, "差一点就抢到了"),
    /**
     * 服务人员不存在
     */
    EMPLOY_NOT_EXIST(31001, "服务人员不存在"),

    /**
     * 电话号码格式错误
     */
    PHONE_ERR(10016, "电话号码格式错误"),

    /**
     * 服务机构被禁用
     */
    COMPANY_NDISABLE_ERROR(40402, "服务机构被禁用"),
    /**
     * 平台级账号的级别不能被修改
     */
    ADMIN_MAIN_NOT_UPDATE(40403, "平台级账号的级别不能被修改"),

    /**
     * 此途径站点不允许下车
     */
    STATION_NOT_ALLOW_GET_OFF(40211, "此途径站点不允许下车"),

    /**
     * 班次发车时间已过期
     */
    SCHEDULE_OBSOLETED(40184, "班次发车时间已过期"),
    /**
     * 班次已经存在
     */
    SCHEDULE_HAS_EXIST(40605, "班次已经存在"),

    /**
     * 客车已经前往下一站点
     */
    CURRENDSTATION_ERROR(40604, "客车已经前往下一站点"),
    /**
     * 班次已经售票
     */
    SCHEDULE_HAS_TICKET(40603, "班次已经售票"),

    /**
     * 班次还有订单未执行
     */
    SCHEDULE_STILL_ORDER(40606, "班次还有订单未执行"),
    /**
     * 当前班次已经指派
     */
    SCHEDULE_HAS_ASSIGN(40629,"当前班次已经指派"),

    SEAT_INFO_ERROR(40601, "座位信息出错,请重新选择");

    private int code;
    private String showMsg;

    ErrCode(int code, String showMsg) {
        this.code = code;
        this.showMsg = showMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getShowMsg() {
        return showMsg;
    }

    public void setShowMsg(String showMsg) {
        this.showMsg = showMsg;
    }
}
