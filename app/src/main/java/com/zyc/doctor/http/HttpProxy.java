package com.zyc.doctor.http;

import android.content.Context;

import com.zyc.doctor.BuildConfig;
import com.zyc.doctor.http.bean.BaseNetConfig;
import com.zyc.doctor.http.bean.HttpConstants;

/**
 * @author dundun
 */
public class HttpProxy implements HttpConstants, BaseNetConfig
{
    private Context context;

    public HttpProxy(Context context)
    {
        this.context = context;
    }

    /**
     * 得到app请求地址
     */
    public String getAPPUrl()
    {
        return BuildConfig.BASE_BASIC_URL;
    }
}
