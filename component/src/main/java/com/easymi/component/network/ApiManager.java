package com.easymi.component.network;

import android.util.Log;

import com.easymi.component.BuildConfig;
import com.easymi.component.Config;
import com.easymi.component.app.XApp;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.linjiang.pandora.Pandora;

/**
 * Created by xyin on 2016/10/11.
 * 网络请求client.
 */

public class ApiManager {

    private OkHttpClient mOkHttpClient;

    /**
     * 内部静态类实现单例,且在第一次使用时才加载.
     */
    private static class SingletonHolder {
        private static final ApiManager INSTANCE = new ApiManager();
    }

    /**
     * 获取单例实例.
     *
     * @return Api对象
     */
    public static ApiManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 私有化构造方法,配置okhttpClient.
     */
    private ApiManager() {
        File cacheFile = new File(XApp.getInstance().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 2); //2M

        Ssl ssl = new Ssl(XApp.getInstance(), "");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(16000, TimeUnit.MILLISECONDS)
                .connectTimeout(16000, TimeUnit.MILLISECONDS)
                //拦截器顺序不要改 谁改谁是狗
                .addInterceptor(new EncryptInterceptor())
                .addInterceptor(new TokenInterceptor());//Header拦截器
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(Pandora.get().getInterceptor());
        }
        builder.addInterceptor(new ResponseIntercepter());
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS));
        }
        builder.cache(cache);

        Log.e("ApiManager", "ApiManager " + Config.HOST);

        if (Config.HOST.contains("https://")) {
            builder.sslSocketFactory(ssl.getSslSocketFactory(), ssl.getTrustManager());
        }

        mOkHttpClient = builder.build();

    }

    /**
     * 创建一个使用网络访问的api.
     *
     * @param hostUrl 该api的host地址
     * @param service api的class类型
     * @param <T>     实际需要返回类型
     * @return 实际返回的api实例
     */
    public <T> T createApi(String hostUrl, Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create()) //添加一个Gson转化
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //添加一个rxjava转换
                .baseUrl(hostUrl)
                .build();

        return retrofit.create(service);
    }

}
