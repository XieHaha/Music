package com.zyc.doctor.http.data;

/**
 * @author dundun
 */
public interface HttpConstants {
    /**
     * 测试服务器
     */
    String BASE_BASIC_URL = "http://gray.zychealth.com";
    /**
     * 内测服务器
     */
    //    String BASE_BASIC_URL = "http://test.zychealth.com";
    /**
     * 正式服务器
     */
    //    String BASE_BASIC_URL = "http://www.zychealth.com";
    /**
     * 超时时间
     */
    int TIMEOUT_MS = 30 * 1000;
    /**
     * 重试请求次数
     */
    int RETRIES = 0;
    /**
     * 补偿方针
     */
    float BACKOFF_MULTIPLIER = 0f;

    /**
     * 枚举网络请求类型
     */
    enum Method {
        POST, GET, PUT, DELETE
    }

    /**
     * HTTP TAG
     */
    String HTTP_TAG = "HttpRequest";
    /**
     * HTTP TAG
     */
    String NULL_TAG = "  null";
    /**
     * 登录协议
     */
    String BASE_BASIC_PROTOCOL_URL = "http://www.zychealth.com/privacy/privacy-policy.html";
    /**
     * 用户使用协议
     */
    String BASE_BASIC_USER_PROTOCOL_URL = "http://www.zychealth.com/privacy/doctor.html";
    /**
     * 下载页面
     */
    String BASE_BASIC_DOWNLOAD_URL = "http://www.zychealth.com/downloadapp/index.html?doctorId=";
}
