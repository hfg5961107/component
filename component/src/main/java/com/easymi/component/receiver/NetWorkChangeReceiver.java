package com.easymi.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.easymi.component.utils.NetUtil;
import com.easymi.component.utils.StringUtils;


/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: NetWorkChangeReceiver
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description: 网络状态变化广播处理
 * History:
 */

public class NetWorkChangeReceiver extends BroadcastReceiver{

    private OnNetChange event;

    public void setEvent(OnNetChange event) {
        this.event = event;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(StringUtils.isNotBlank(action)){
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                int netWorkState = NetUtil.getNetWorkState(context);
                // 接口回调传过去状态的类型
                if(null != event){
                    event.onNetChange(netWorkState);
                }
            }
        }
    }

    public interface OnNetChange{
        void onNetChange(int status);
    }
}
