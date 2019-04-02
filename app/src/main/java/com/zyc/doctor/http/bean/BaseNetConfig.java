package com.zyc.doctor.http.bean;

/**
 * @author DUNDUN
 * @date 2016/11/29
 */
public interface BaseNetConfig {
    /**
     * 请求成功编码
     */
    int REQUEST_SUCCESS = 200;
    /**
     * 修改患者健康档案 无权限
     */
    int CODE_MODIFY_CASE_RECORD = 101;
    /**
     * 设置默认超时时间
     */
    int DEFAULT_TIME = 10;
}
