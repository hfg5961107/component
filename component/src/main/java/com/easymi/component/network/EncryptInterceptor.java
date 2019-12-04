package com.easymi.component.network;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.easymi.component.Config;
import com.easymi.component.app.XApp;
import com.easymi.component.utils.AesUtil;
import com.easymi.component.utils.EncApi;
import com.easymi.component.utils.RsaUtils;
import com.easymi.component.utils.URLDecoderUtil;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: EncryptInterceptor
 *
 * @Author: hufeng
 * Date: 2018/12/24 下午1:10
 * Description: 拦截请求添加sign
 * History:
 */
public class EncryptInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request originRequest = chain.request();


        //排除登录及其之前接口，其余接口使用拦截器进行aes加密。
        String url = chain.request().url().toString();
        if (!Config.IS_ENCRYPT
                || url.contains(Config.HOST_UP_PIC)
                || (!TextUtils.isEmpty(Config.MQTT_CONNECTION_URL) && url.contains(Config.MQTT_CONNECTION_URL))) {
            return chain.proceed(originRequest);
        }
        Request request = hookRequest(originRequest, TextUtils.equals(originRequest.header(Config.TYPE), Config.RSA));

        if (request == null) {
            return chain.proceed(originRequest);
        }
        return chain.proceed(request);
    }

    /**
     * 拦截原始获取请求字段添加新的字段.
     *
     * @param originRequest 原始请求.
     * @return 处理后的新请求
     */
    private Request hookRequest(@NonNull Request originRequest, boolean isRsa) {
        RequestBody requestBody = originRequest.body();
        if (requestBody == null) {
            return handleGet(originRequest, isRsa);
        } else if (requestBody instanceof FormBody) {
            return handlePost(originRequest, (FormBody) requestBody, isRsa);
        }
        return null;
    }

    /**
     * post方法处理
     *
     * @param originRequest
     * @param originBody
     * @return
     */
    private Request handlePost(@NonNull Request originRequest, FormBody originBody, boolean isRsa) {


        FormBody.Builder newFormBody = new FormBody.Builder();
        for (int i = 0; i < originBody.size(); i++) {

            newFormBody.addEncoded(originBody.encodedName(i),
                    isRsa ? RsaEncrypt(originBody.encodedValue(i)) : encrypt(originBody.encodedValue(i)));
        }

        return originRequest.newBuilder()
                .method(originRequest.method(), newFormBody.build())
                .build();

    }

    /**
     * get方式处理
     *
     * @param originRequest
     * @return
     */
    private Request handleGet(@NonNull Request originRequest, boolean isRsa) {
        HttpUrl httpUrl = originRequest.url();
        HttpUrl.Builder builder = httpUrl.newBuilder();
        for (String queryParameterName : httpUrl.queryParameterNames()) {
            builder.setEncodedQueryParameter(queryParameterName,
                    isRsa ? RsaEncrypt(httpUrl.queryParameter(queryParameterName)) :
                            encrypt(httpUrl.queryParameter(queryParameterName)));
        }
        return originRequest.newBuilder()
                .url(builder.build())
                .build();
    }

//    /**
//     * AES 解密
//     *
//     * @param content 密文
//     * @return
//     */
//
//    public static String aesDecrypt(String content) {
//        return AesUtil.aesDecrypt(content, "");
//    }

    /**
     * 加密.
     */
    private String encrypt(String content) {
        String value = content;
        try {
            //将默认的url编码还原后加密在url编码
            String decoderStr = URLDecoderUtil.decode(content);
            value = EncApi.getInstance().en(XApp.getMyPreferences().getString(Config.AES_PASSWORD, AesUtil.AAAAA), decoderStr);
            value = URLEncoder.encode(value, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    private String RsaEncrypt(String content) {
        String value = content;
        try {
            //将默认的url编码还原后加密在url编码
            String decoderStr = URLDecoderUtil.decode(content);
            value = RsaUtils.rsaEncode(decoderStr);
            value = URLEncoder.encode(value, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
