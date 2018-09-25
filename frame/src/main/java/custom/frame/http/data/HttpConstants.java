package custom.frame.http.data;

public interface HttpConstants
{
    /**
     * 测试服务器
     */
    String BASE_BASIC_URL = "http://39.107.249.194:7080/DPView";
    /**
     * 正式服务器
     */
    //    String BASE_BASIC_URL = "http://www.zychealth.com";
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
}
