package com.zyc.doctor.api.notify;

/**
 * 数据变化接口
 * @author dundun
 */
public interface IChange<T>
{
    void onChange(T data);
}
