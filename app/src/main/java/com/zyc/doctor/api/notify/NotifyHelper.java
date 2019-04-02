package com.zyc.doctor.api.notify;

import android.content.Context;

import com.zyc.doctor.api.ApiManager;

/**
 * Api获取帮助类
 *
 * @author dundun
 */
public class NotifyHelper {
    private NotifyHelper() {}

    /**
     * 初始化api，须在Application中执行
     */
    public static void init(Context context) {
        ApiManager.getInstance().init(context);
    }

    /**
     * 是否打开log日志
     *
     * @param isEnable true：打开；false：关闭。
     */
    public static void setIsEnableLog(boolean isEnable) {
        ApiManager.getInstance().setLogEnable(isEnable);
    }
}
