package com.zyc.doctor.api;

import android.content.Context;

import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.utils.LogUtils;

/**
 * SDK 初始化
 *
 * @author dundun
 */
public class ApiManager {
    private static final String TAG = ApiManager.class.getName();
    private static Context sContext;
    private static boolean isLogEnable = true;
    private static ApiManager mInstance;

    private ApiManager() {
    }

    public static synchronized ApiManager getInstance() {
        if (null == mInstance) {
            mInstance = new ApiManager();
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context, boolean isEnable) {
        sContext = context;
        isLogEnable = isEnable;
        LogUtils.setIsEnableLog(isEnable);
    }

    public Context getContext() {
        if (null == sContext) {
            LogUtils.w(TAG, "api Context is null, must be initial!");
        }
        return sContext;
    }

    /**
     * 获取是否开启log日志
     */
    public boolean isLogEnable() {
        return isLogEnable;
    }

    public NotifyChangeListenerManager.NotifyChangeListenerServer getServer() {
        return NotifyChangeListenerManager.getInstance();
    }

    public void close() {
        sContext = null;
        mInstance = null;
    }
}
