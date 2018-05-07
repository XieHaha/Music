package com.yht.yihuantong.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by dundun on 16/4/14.
 */
public class NetWorkUtils
{
    private ConnectivityManager connectivityManager;
    private WifiManager wifiManager;
    private NetworkInfo info;

    private Context context;

    public NetWorkUtils(Context context) {
        this.context = context;
        init();
    }

    /**
     * 网络状态处理
     */
    private void init() {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 网络连通性判断
     *
     * @return
     */
    public boolean isConnected() {
        info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断wifi连接状态
     *
     * @return
     */
    public boolean isWifiConnected() {
        int state = wifiManager.getWifiState();
        switch (state) {
            case WifiManager.WIFI_STATE_ENABLED:
            case WifiManager.WIFI_STATE_ENABLING:
                return true;
        }
        return false;
    }
}
