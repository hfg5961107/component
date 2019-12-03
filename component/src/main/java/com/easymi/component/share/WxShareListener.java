package com.easymi.component.share;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by xyin on 2017/3/6.
 * 微信分享回调接口.
 */

public abstract class WxShareListener implements IWXAPIEventHandler {

    /**
     * 微信发送消息到本应用的回调.
     *
     * @param baseReq baseReq
     */
    @Override
    public void onReq(BaseReq baseReq) {

    }

}
