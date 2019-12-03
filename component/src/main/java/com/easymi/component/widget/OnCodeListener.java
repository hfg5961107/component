package com.easymi.component.widget;

/**
 * Created by xyin on 2017/10/15.
 */

public interface OnCodeListener {
    /**
     * 完成输入时调用该方法.
     *
     * @param code 输入的验证码
     */
    void onCodeComplete(String code);

}
