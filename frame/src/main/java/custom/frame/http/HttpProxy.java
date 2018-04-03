package custom.frame.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import custom.frame.http.data.BaseNetCode;
import custom.frame.http.data.HttpConstants;

/**
 * Created by luozi on 2016/7/13.
 */
public class HttpProxy implements HttpConstants, BaseNetCode {

    public Context context;

    private SharedPreferences mPrefer;
    private SharedPreferences.Editor editor;

    public HttpProxy(Context context) {
        this.context = context;
        mPrefer = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = mPrefer.edit();
    }

    @Override
    public String getBaseIp() {
        String tmp = getString(PRE_BASE_IP, "");
        String ip;
        if (!TextUtils.isEmpty(tmp)) {
            ip = tmp;
        } else {
            ip = BASE_IP;
        }

        return ip;
    }

    /**
     * 得到app请求地址
     */
    public String getAPPUrl() {
        return URL_PREFIX + getBaseIp() + ":" + getBaseIpPort() + "/appserver/";
//        return URL_PREFIX + getBaseIp() + ":" + getBaseIpPort() + "/";
    }

    @Override
    public String getBaseCallbackIp() {
        String tmp = getString(PRE_BASE_CALLBACK_IP, "");
        String ip;
        if (!TextUtils.isEmpty(tmp)) {
            ip = tmp;
        } else {
            ip = BASE_CALLBACK_IP;
        }
        return ip;
    }


    @Override
    public String getPayCallbackUrl() {
        return URL_PREFIX + getBaseCallbackIp() + ":" + getBaseCallbackIpPort() +
                "/orderService/aliPayCallback";
    }

    @Override
    public String getRechargeCallbackUrl() {
        return URL_PREFIX + getBaseCallbackIp() + ":" + getBaseCallbackIpPort() +
                "/orderService/aliPayRechargeCashCardCallback";
    }

    @Override
    public String getBaseIpPort() {
        String tmp = getString(PRE_BASE_IP_PORT, "");
        String port;
        if (!TextUtils.isEmpty(tmp)) {
            port = tmp;
        } else {
            port = BASE_IP_PORT;
        }
        return port;
    }

    @Override
    public String getBaseCallbackIpPort() {
        String tmp = getString(PRE_BASE_CALLBACK_IP_PORT, "");
        String port;
        if (!TextUtils.isEmpty(tmp)) {
            port = tmp;
        } else {
            port = BASE_CALLBACK_IP_PORT;
        }
        return port;
    }

    /**
     * 存放String 值
     */
    public void putString(String name, String values) {
        editor.putString(name, values);
        editor.commit();
    }


    /**
     * 得到xml文件中的键值对名 如果没有值则默认为“”
     */
    public String getString(String name, String defValue) {
        return mPrefer.getString(name, defValue);
    }
}
