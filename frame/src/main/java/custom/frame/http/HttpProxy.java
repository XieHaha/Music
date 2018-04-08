package custom.frame.http;

import android.content.Context;

import custom.frame.http.data.BaseNetCode;
import custom.frame.http.data.HttpConstants;

/**
 * Created by luozi on 2016/7/13.
 */
public class HttpProxy implements HttpConstants, BaseNetCode
{
    public Context context;

    public HttpProxy(Context context)
    {
        this.context = context;
    }

    @Override
    public String getBaseIp()
    {
        return BASE_IP;
    }

    /**
     * 得到app请求地址
     */
    public String getAPPUrl()
    {
        return URL_PREFIX + getBaseIp() + ":" + getBaseIpPort() + "/DPView/";
    }

    @Override
    public String getBaseIpPort()
    {
        return BASE_IP_PORT;
    }
}
