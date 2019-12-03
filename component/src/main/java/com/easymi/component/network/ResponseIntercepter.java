package com.easymi.component.network;

import com.easymi.component.BuildConfig;
import com.easymi.component.Config;
import com.easymi.component.app.XApp;
import com.easymi.component.utils.AesUtil;
import com.easymi.component.utils.EncApi;
import com.easymi.component.utils.URLDecoderUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class ResponseIntercepter implements Interceptor {

    private static final String MIME = "application/json; charset=utf-8";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        String content = response.body().string();
        if (content.startsWith("{") && content.endsWith("}")) {
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Logger.DEFAULT.log(URLDecoderUtil.stringToJSON(content));
            }
        } else {
            content = URLDecoderUtil.decode(EncApi.getInstance().dec(XApp.getMyPreferences().getString(Config.AES_PASSWORD, AesUtil.AAAAA), content));
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Logger.DEFAULT.log(URLDecoderUtil.stringToJSON(content));
            }
        }

        return response
                .newBuilder()
                .body(ResponseBody.create(MediaType.parse(MIME), content))
                .build();
    }
}
