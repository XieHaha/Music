package com.zyc.doctor.api;

/**
 * 数据变化接口
 */
public interface IChange<T>
{
    void onChange(T data);
}
