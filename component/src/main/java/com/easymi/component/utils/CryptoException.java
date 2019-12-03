// Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
package com.easymi.component.utils;

/**
 * 加解密异常
 *
 * @author 肖波
 * @since 1.0
 */
public class CryptoException extends RuntimeException {
    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public CryptoException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 异常信息
     * @param cause   堆栈信息
     */
    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }
}
