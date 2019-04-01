package com.zyc.doctor.http;

import android.content.Context;

import com.zyc.doctor.http.data.BaseNetCode;
import com.zyc.doctor.http.data.HttpConstants;

/**
 * @author dundun
 */
public class HttpProxy implements HttpConstants, BaseNetCode
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
        return BASE_BASIC_URL;
    }
}
