package com.zyc.doctor.http.listener;


import java.io.File;

import com.zyc.doctor.http.Tasks;

/**
 * Created by luozi on 2015/12/29.
 * 响应监听，泛型
 */
public interface ResponseListener<T> {
    /**
     * 响应成功
     */
    void onResponseSuccess(Tasks task, T response);

    /**
     * 请求错误
     */
    void onResponseError(Tasks task, Exception e);

    /**
     * 请求错误
     */
    void onResponseCodeError(Tasks task, T response);

    /**
     * 请求开始
     */
    void onResponseStart(Tasks task);

    /**
     * 请求结束，无论请求是否成功都会回调
     */
    void onResponseEnd(Tasks task);

    /**
     * 请求结束，无论请求是否成功都会回调
     */
    void onResponseFile(Tasks task, File file);

    /**
     * 请求中....
     * 根据网络框架而定
     */
    void onResponseLoading(Tasks task, boolean isUpload, long total, long current);

    /**
     * 取消请求
     * 根据框架而定，暂时弃用
     */
    void onResponseCancel(Tasks task);
}
