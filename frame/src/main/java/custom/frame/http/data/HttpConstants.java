package custom.frame.http.data;

public interface HttpConstants {
    /**
     * 基础网络连接地址
     */
    String URL_PREFIX = "http://";
    String BASE_IP = "39.107.249.194";
    String BASE_IP_PORT = "8080";
    /**
     * 测试服务器
     */
    String BASE_BASIC_URL = "http://39.107.249.194:8080";
    /**
     * 正式服务器
     */
    //    String BASE_BASIC_URL = "http://39.105.117.100:8080";
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
     * 基础ip更改pre  键值对名称
     */
    String PRE_BASE_IP = "PRE_BASE_IP";
    /**
     * 基础ip port更改pre  键值对名称
     */
    String PRE_BASE_IP_PORT = "PRE_BASE_IP_PORT";
    /**
     * 基础回调ip更改pre  键值对名称
     */
    String PRE_BASE_CALLBACK_IP = "PRE_BASE_CALLBACK_IP";
    /**
     * 基础回调ip port更改pre  键值对名称
     */
    String PRE_BASE_CALLBACK_IP_PORT = "PRE_BASE_CALLBACK_IP_PORT";

    /**
     * 枚举网络请求类型
     */
    enum Method {
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
     * 响应错误提示
     */
    String ResponseErrorHint = "请求失败";

    /**
     * 得到基础ip方法实现
     */
    String getBaseIp();

    /**
     * 得到基础ip端口号
     */
    String getBaseIpPort();

}
