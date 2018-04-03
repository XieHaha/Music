package custom.frame.http.data;

/**
 * Created by luozi on 2015/12/29.
 */
public interface HttpConstants {
    /**
     * 基础网络连接地址
     */
    String URL_PREFIX = "http://";

    String BASE_IP = "139.224.34.5";
    String BASE_CALLBACK_IP = "139.224.33.114";

    //测试部门虚拟机环境
//    String BASE_IP = "192.168.5.209";
//    String BASE_CALLBACK_IP = "192.168.5.209";

    //开发环境地址
//    String BASE_IP = "192.168.6.133";
//    String BASE_CALLBACK_IP = "192.168.6.133";

//    String BASE_IP = "weixin.mawansan.com";
//    String BASE_CALLBACK_IP = "139.224.33.114";

    String BASE_IP_PORT = "80";
    //        String BASE_IP_PORT = "48080";
//         String BASE_IP_PORT = "8080";
    String BASE_CALLBACK_IP_PORT = "9090";
    /**
     * ip pre name
     */
    String PREFER_NAME = "com.dz.tt.ip";

    //    String HttpBaseUrl = "http://211.149.228.97:8080/index.php";
//    String HttpBaseUrl = "http://192.168.4.185/appserver/";
//    String HttpBaseUrl = "http://192.168.5.209:8080/appserver/";//电桩测试环境
//    String HttpBaseUrl = "http://211.149.250.31/appserver/";//生产环境
//    String HttpBaseUrl = "http://dzv3.dz.tt/index.php";//电桩线上版生成环境
//    String HttpBaseUrl = "http://192.168.5.209/guianServer/";//贵安电桩测试环境
//    String HttpBaseUrl = URL_PREFIX + BASE_IP + ":" + BASE_IP_PORT + "/appserver/";//测试服务器外部暴露端口
//    String HttpBaseUrl = "http://211.149.247.187/appserver/";//7.5演练环境

    //    String alipayCallbackPayUrl = "http://211.149.228.97:9091/ord erService/aliPayCallback";
//    String alipayCallbackRechargeUrl = "http://211.149.228.97:9091/orderService/aliPayRechargeCashCardCallback";
//    String alipayCallbackPayUrl = URL_PREFIX + BASE_CALLBACK_IP + ":" + BASE_CALLBACK_IP_PORT +
//            "/orderService/aliPayCallback";
//    String alipayCallbackRechargeUrl = URL_PREFIX + BASE_CALLBACK_IP + ":" + BASE_CALLBACK_IP_PORT +
//            "/orderService/aliPayRechargeCashCardCallback";

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

    /**
     * 设置基础ip
     */
    void setBaseIp(String baseIp);

    /**
     * 设置击锤回调ip
     */
    void setBaseCallbackIp(String baseCallbackIp);

    /**
     * 设置基础ip端口号
     */
    void setBaseIpPort(String baseIpPort);

    /**
     * 设置回调ip端口号
     */
    void setBaseCallbackIpPort(String baseCallbackIpPort);
}
