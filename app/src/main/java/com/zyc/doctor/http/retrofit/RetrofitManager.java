package com.zyc.doctor.http.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zyc.doctor.BuildConfig;
import com.zyc.doctor.http.bean.BaseNetConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit封装
 *
 * @author dundun
 */
public class RetrofitManager {
    private static RetrofitServer retrofitServer;
    private static volatile ApiUrlManager apiUrlManager;

    public synchronized static RetrofitServer getInstance() {
        if (retrofitServer == null) {
            retrofitServer = new RetrofitServer();
        }
        return retrofitServer;
    }

    public static ApiUrlManager getApiUrlManager() {
        if (apiUrlManager == null) {
            synchronized (ApiUrlManager.class) {
                apiUrlManager = getInstance().initApiUrlManager();
            }
        }
        return apiUrlManager;
    }

    public static class RetrofitServer {
        private static OkHttpClient okHttpClient;
        private static Retrofit retrofit;

        /**
         * 初始化
         */
        public void init() {
            initOkHttp();
            initRetrofit();
            initApiUrlManager();
        }

        private ApiUrlManager initApiUrlManager() {
            if (retrofit == null) {
                init();
            }
            return retrofit.create(ApiUrlManager.class);
        }

        /**
         * 初始化Retrofit
         */
        private void initRetrofit() {
            retrofit = new Retrofit.Builder().client(okHttpClient)
                                             .baseUrl(BuildConfig.BASE_BASIC_URL)
                                             .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                             .addConverterFactory(GsonConverterFactory.create())
                                             .build();
        }

        /**
         * 初始化okhttp
         */
        private void initOkHttp() {
            okHttpClient = new OkHttpClient().newBuilder()
                                             //设置读取超时时间
                                             .readTimeout(BaseNetConfig.DEFAULT_TIME, TimeUnit.SECONDS)
                                             //设置请求超时时间
                                             .connectTimeout(BaseNetConfig.DEFAULT_TIME, TimeUnit.SECONDS)
                                             //设置写入超时时间
                                             .writeTimeout(BaseNetConfig.DEFAULT_TIME, TimeUnit.SECONDS)
                                             //添加打印拦截器
                                             .addInterceptor(new LogInterceptor())
                                             //设置出现错误进行重新连接。
                                             .retryOnConnectionFailure(true).build();
        }
    }
}

