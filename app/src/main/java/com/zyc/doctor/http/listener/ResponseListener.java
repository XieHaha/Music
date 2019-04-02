package com.zyc.doctor.http.listener;

import com.zyc.doctor.http.Tasks;

/**
 * @author luozi
 * @date 2015/12/29
 * 响应监听，泛型
 */
public interface ResponseListener<T> {
    /**
     * 响应成功
     * @param task
     * @param response
     */
    void onResponseSuccess(Tasks task, T response);

    /**
     * 请求错误
     * @param task
     * @param e
     */
    void onResponseError(Tasks task, Exception e);

    /**
     * 请求错误
     * @param task
     * @param response
     */
    void onResponseCode(Tasks task, T response);

    /**
     * 请求开始
     * @param task
     */
    void onResponseStart(Tasks task);

    /**
     * 请求结束，无论请求是否成功都会回调
     * @param task
     */
    void onResponseEnd(Tasks task);
    /**
     * 取消请求
     * 根据框架而定，暂时弃用
     */
    void onResponseCancel(Tasks task);
}
