package com.easymi.component.utils;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class Loader {

    static {
        System.loadLibrary("loader");
    }

    /**
     * 获取xiaoka证书
     *
     * @return String
     */
    public native String getXk();

    /**
     * 获取abc证书.
     *
     * @return String
     */
    public native String getAbc();

    /**
     * 获取密码.
     *
     * @return String
     */
    public native String getPs();

    /**
     * 获取rsa密码.
     *
     * @return String
     */
    public native String getRsaPs();

}
