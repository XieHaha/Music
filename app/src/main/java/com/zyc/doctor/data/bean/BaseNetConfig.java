package com.zyc.doctor.data.bean;

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
    int CODE_101 = 101;
    /**
     * 设置默认超时时间
     */
    int DEFAULT_TIME = 10;
    /**
     * 用户使用协议
     */
    String BASE_BASIC_USER_PROTOCOL_URL = "http://www.zychealth.com/privacy/doctor.html";
    /**
     * 下载页面
     */
    String BASE_BASIC_DOWNLOAD_URL = "http://www.zychealth.com/downloadapp/index.html?doctorId=";
}
