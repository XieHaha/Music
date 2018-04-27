package com.yht.yihuantong.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yht.yihuantong.api.notify.INotifyChangeListenerServer;
import com.yht.yihuantong.api.notify.NotifyChangeListenerServer;
import com.yht.yihuantong.utils.LogUtils;

/**
 *  SDK 初始化
 */
public class ApiManager
{
    private static final String TAG = ApiManager.class.getName();
    private static Context sContext;
    private static boolean isLogEnable = true;
    private static ApiManager mInstance;

    private ApiManager()
    {
    }

    public static synchronized ApiManager getInstance()
    {
        if (null == mInstance)
        {
            mInstance = new ApiManager();
        }
        return mInstance;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context)
    {
        this.sContext = context;
    }

    public Context getContext()
    {
        if (null == sContext)
        {
            LogUtils.w(TAG, "api Context is null, must be initial!");
        }
        return sContext;
    }

    /**
     * 设置是否开启log日志
     *
     * @param isEnable
     */
    public void setLogEnable(boolean isEnable)
    {
        isLogEnable = isEnable;
        LogUtils.setIsEnableLog(isEnable);
    }

    /**
     * 获取是否开启log日志
     */
    public boolean isLogEnable()
    {
        return isLogEnable;
    }

    public <T> T getServer(@NonNull Class<T> apiClass)
    {
        if (INotifyChangeListenerServer.class.getSimpleName().equals(apiClass.getSimpleName()))
        {
            return (T)NotifyChangeListenerServer.getInstance();
        }
        return null;
    }

    public void close()
    {
        sContext = null;
        mInstance = null;
    }
}
