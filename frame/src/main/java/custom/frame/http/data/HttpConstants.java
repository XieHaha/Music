package custom.frame.http.data;

public interface HttpConstants
{
    /**
     * 测试服务器
     */
    //    String BASE_BASIC_URL = "http://gray.zychealth.com";
    /**
     * 正式服务器
     */
    String BASE_BASIC_URL = "http://www.zychealth.com";
    /**
     * 超时时间
     */
    int timeoutMS = 30 * 1000;
    /**
     * 重试请求次数
     */
    int retries = 0;
    /**
     * 补偿方针
     */
    float backoffMultiplier = 0f;

    /**
     * 枚举网络请求类型
     */
    enum Method
    {
        POST, GET, PUT, DELETE
    }

    /**
     * HTTP TAG
     */
    String HttpTag = "HttpRequest";
    /**
     * HTTP TAG
     */
    String NULLTag = "  null";
    /**
     * 登录协议
     */
    String BASE_BASIC_PROTOCOL_URL = "http://www.zychealth.com/privacy/privacy-policy.html";
    /**
     * 下载页面
     */
    String BASE_BASIC_DOWNLOAD_URL = "http://www.zychealth.com/downloadapp/index.html?doctorId=";
}
