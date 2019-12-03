package com.easymi.component.utils;


/**
 * Created by yinxin on 2018/3/16.
 * aes加密.
 */

public class EncApi {

    //加载加密模块.
    static {
        System.loadLibrary("ps");
    }

    /**
     * 内部静态类实现单例,且在第一次使用时才加载.
     */
    private static class SingletonHolder {
        private static final EncApi INSTANCE = new EncApi();
    }

    /**
     * 获取单例实例.
     *
     * @return Api对象
     */
    public static EncApi getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public native String en(String password ,String content);

    public native String dec(String password ,String content);

    public native String getPubKey();


}
