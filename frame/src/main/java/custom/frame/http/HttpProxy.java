package custom.frame.http;

import android.content.Context;

import custom.frame.http.data.BaseNetCode;
import custom.frame.http.data.HttpConstants;

/**
 * @author dundun
 */
public class HttpProxy implements HttpConstants, BaseNetCode
{
    private Context context;

    public HttpProxy(Context context)
    {
        this.context = context;
    }

    /**
     * 得到app请求地址
     */
    public String getAPPUrl()
    {
        //        return BASE_BASIC_URL + "/DPView";
        return BASE_BASIC_URL;
    }
}
