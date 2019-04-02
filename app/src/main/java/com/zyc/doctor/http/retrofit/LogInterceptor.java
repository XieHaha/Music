package com.zyc.doctor.http.retrofit;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * TODO Log拦截器代码
 *
 * @author dundun
 */
public class LogInterceptor implements Interceptor {
    private String TAG = "OkHttp";

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.e(TAG, "request params:" + request.toString());
        okhttp3.Response response = chain.proceed(chain.request());
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        Log.e(TAG, "response body:" + content);
        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
    }
}
