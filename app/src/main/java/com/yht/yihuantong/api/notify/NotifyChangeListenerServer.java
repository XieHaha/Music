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
     * 患者添加状态
     */
    private List<IChange<String>> mPatientStatusChangeListeners = new CopyOnWriteArrayList<>();
    /**
     * 医生添加状态
     */
    private List<IChange<String>> mDoctorStatusChangeListeners = new CopyOnWriteArrayList<>();
    /**
     * 医生认证
     */
    private List<IChange<Integer>> mDoctorAuthStatusChangeListeners = new CopyOnWriteArrayList<>();
    /**
     * 转诊申请
     */
    private List<IChange<String>> mDoctorChangePatientListeners = new CopyOnWriteArrayList<>();

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

    @Override
    public void registerDoctorChangePatientListener(@NonNull IChange<String> listener,
            @NonNull RegisterType registerType)
    {
        if (listener == null) { return; }
        if (RegisterType.REGISTER == registerType)
        {
            mDoctorChangePatientListeners.add(listener);
        }
        else
        {
            mDoctorChangePatientListeners.remove(listener);
        }
    }

    @Override
    public void registerDoctorAuthStatusChangeListener(@NonNull IChange<Integer> listener,
            @NonNull RegisterType registerType)
    {
        if (listener == null) { return; }
        if (RegisterType.REGISTER == registerType)
        {
            mDoctorAuthStatusChangeListeners.add(listener);
        }
        else
        {
            mDoctorAuthStatusChangeListeners.remove(listener);
        }
    }

    /**
     * 患者添加
     *
     * @param data
     */
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

    /**
     * 医生添加
     *
     * @param data
     */
    public void notifyDoctorStatusChange(final String data)
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

    /**
     * 转诊申请
     *
     * @param data
     */
    public void notifyDoctorChangePatient(final String data)
    {
        synchronized (mDoctorChangePatientListeners)
        {
            for (int i = 0, size = mDoctorChangePatientListeners.size(); i < size; i++)
            {
                try
                {
                    final IChange<String> change = mDoctorChangePatientListeners.get(i);
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

    /**
     * 医生认证状态
     *
     * @param data
     */
    public void notifyDoctorAuthStatus(final Integer data)
    {
        synchronized (mDoctorAuthStatusChangeListeners)
        {
            for (int i = 0, size = mDoctorAuthStatusChangeListeners.size(); i < size; i++)
            {
                try
                {
                    final IChange<Integer> change = mDoctorAuthStatusChangeListeners.get(i);
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
