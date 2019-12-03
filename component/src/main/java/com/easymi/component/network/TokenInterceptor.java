package com.easymi.component.network;

import android.text.TextUtils;

import com.easymi.component.Config;
import com.easymi.component.app.XApp;
import com.easymi.component.utils.Base64;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * token 拦截器:用来统一添加token参数
 */
public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpUrl httpUrl = chain.request().url();
        Request request = chain.request();
        if ((!TextUtils.isEmpty(Config.MQTT_CONNECTION_URL) && httpUrl.toString().contains(Config.MQTT_CONNECTION_URL))) {
            return chain.proceed(request.newBuilder()
                    .url(httpUrl.newBuilder()
                            .scheme("http")
                            .host(Config.MQTT_HOST)
                            .port(Config.PORT_HTTP)
                            .build())
                    .addHeader("Authorization", "Basic " + Base64.encode((Config.MQTT_USER_NAME + ":" + Config.MQTT_PSW).getBytes()))
                    .addHeader("Accept", "applicaton/json")
                    .build());
        } else {
            String sToken = XApp.getMyPreferences().getString(Config.SP_TOKEN, "");
            Request.Builder builder = request.newBuilder()
                    .addHeader("token", sToken)
                    .addHeader("appKey", Config.APP_KEY);
            if (!TextUtils.isEmpty(Config.VERSION_DATA) && !TextUtils.equals(Config.VERSION_DATA, "version_placeholder")) {
                builder.addHeader("versionData", Config.VERSION_DATA);
            }
            return chain.proceed(builder.build());
        }
    }
}
