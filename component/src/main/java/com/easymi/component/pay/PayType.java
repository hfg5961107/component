package com.easymi.component.pay;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright (C) , 2012-2018 , 四川小咖科技有限公司
 *
 * @author Lzh
 * @version 5.0.0.000
 * @date 2018/6/8
 * @since 5.0.0.000
 */
public class PayType {

    @SerializedName("aliPayApp")
    public boolean aliPay;
    public boolean balance;
    @SerializedName("weChatApp")
    public boolean weChat;
}
