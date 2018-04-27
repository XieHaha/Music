package com.yht.yihuantong.api.notify;

import android.support.annotation.NonNull;

import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;

/**
 * Created by dundun on 18/4/27.
 */
public interface INotifyChangeListenerServer
{
    /**
     * 患者状态监听
     *
     * @param listener     消息状态监听器
     * @param registerType 注册类型
     */
    void registerPatientStatusChangeListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);

    /**
     * 医生状态监听
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerDoctorStatusChangeListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);
}
