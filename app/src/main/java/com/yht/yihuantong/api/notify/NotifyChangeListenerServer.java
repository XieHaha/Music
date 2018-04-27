package com.yht.yihuantong.api.notify;

import android.support.annotation.NonNull;

import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;
import com.yht.yihuantong.utils.LogUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotifyChangeListenerServer implements INotifyChangeListenerServer
{
    private final String TAG = NotifyChangeListenerServer.class.getName();
    private static NotifyChangeListenerServer mInstance;
    /**
     * 消息状态修改监听
     */
    private List<IChange<String>> mPatientStatusChangeListeners = new CopyOnWriteArrayList<>();
    /**
     * 接收消息监听
     */
    private List<IChange<String>> mDoctorStatusChangeListeners = new CopyOnWriteArrayList<>();

    private NotifyChangeListenerServer()
    {
    }

    public static NotifyChangeListenerServer getInstance()
    {
        if (mInstance == null)
        {
            synchronized (NotifyChangeListenerServer.class)
            {
                if (mInstance == null)
                {
                    mInstance = new NotifyChangeListenerServer();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void registerPatientStatusChangeListener(@NonNull IChange<String> listener,
            @NonNull RegisterType registerType)
    {
        if (listener == null) { return; }
        if (RegisterType.REGISTER == registerType)
        {
            mPatientStatusChangeListeners.add(listener);
        }
        else
        {
            mPatientStatusChangeListeners.remove(listener);
        }
    }

    @Override
    public void registerDoctorStatusChangeListener(@NonNull IChange<String> listener,
            @NonNull RegisterType registerType)
    {
        if (listener == null) { return; }
        if (RegisterType.REGISTER == registerType)
        {
            mDoctorStatusChangeListeners.add(listener);
        }
        else
        {
            mDoctorStatusChangeListeners.remove(listener);
        }
    }

    public void notifyPatientStatusChange(final String data)
    {
        synchronized (mPatientStatusChangeListeners)
        {
            for (int i = 0, size = mPatientStatusChangeListeners.size(); i < size; i++)
            {
                try
                {
                    final IChange<String> change = mPatientStatusChangeListeners.get(i);
                    if (null != change)
                    {
                        change.onChange(data);
                    }
                }
                catch (Exception e)
                {
                    LogUtils.w(TAG, "notifyStatusChange error", e);
                }
            }
        }
    }

    public void notifyDoctorMessageChange(final String data)
    {
        synchronized (mDoctorStatusChangeListeners)
        {
            for (int i = 0, size = mDoctorStatusChangeListeners.size(); i < size; i++)
            {
                try
                {
                    final IChange<String> change = mDoctorStatusChangeListeners.get(i);
                    if (null != change)
                    {
                        change.onChange(data);
                    }
                }
                catch (Exception e)
                {
                    LogUtils.w(TAG, "notifyMessageChange error", e);
                }
            }
        }
    }
}
