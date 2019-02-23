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
    /**
     * 医生转诊申请监听
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerDoctorTransferPatientListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);
    /**
     * 医生认证状态监听
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerDoctorAuthStatusChangeListener(@NonNull IChange<Integer> listener, @NonNull RegisterType registerType);
    /**
     * 最近联系人监听
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerRecentContactChangeListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);
    /**
     * 服务包订单状态
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerOrderStatusChangeListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);
}
