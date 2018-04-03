package custom.frame.http.data;

public interface HttpConstants
{
    /**
     * 基础网络连接地址
     */
    String URL_PREFIX = "http://";
    String BASE_IP = "139.224.34.5";
    String BASE_CALLBACK_IP = "139.224.33.114";
    String BASE_IP_PORT = "80";
    String BASE_CALLBACK_IP_PORT = "9090";
    /**
     * ip pre name
     */
    String PREFER_NAME = "com.yht.ip";

    /**
     * 得到支付回调地址
     */
    String getPayCallbackUrl();

    /**
     * 得到充值回调地址
     */
    String getRechargeCallbackUrl();

    /**
     * 分页的页数大小
     */
    String PageSize = "20";
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
     * 响应错误提示
     */
    String ResponseErrorHint = "请求失败";

    /**
     * 得到基础ip方法实现
     */
    String getBaseIp();

    /**
     * 得到基础回调ip
     */
    String getBaseCallbackIp();

    /**
     * 得到基础ip端口号
     */
    String getBaseIpPort();

    /**
     * 得到回调ip端口号
     */
    String getBaseCallbackIpPort();
}
