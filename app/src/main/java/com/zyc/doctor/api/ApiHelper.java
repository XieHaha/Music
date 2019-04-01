package com.zyc.doctor.api;

import android.content.Context;

import com.zyc.doctor.api.notify.NotifyChangeListenerManager;

/**
 * Api获取帮助类
 * @author dundun
 */
public class ApiHelper
{
    private ApiHelper() {}

    /**
     * 初始化api，须在Application中执行
     *
     */
    public static void init(Context context)
    {
        ApiManager.getInstance().init(context);
    }

    /**
     * 获取相关服务
     *
     * @return 模块服务。若该模块不存在，返回为null
     */
    public static NotifyChangeListenerManager.NotifyChangeListenerServer getServer()
    {
        return ApiManager.getInstance().getServer();
    }

    /**
     * 是否打开log日志
     *
     * @param isEnable true：打开；false：关闭。
     */
    public static void setIsEnableLog(boolean isEnable)
    {
        ApiManager.getInstance().setLogEnable(isEnable);
    }

}
