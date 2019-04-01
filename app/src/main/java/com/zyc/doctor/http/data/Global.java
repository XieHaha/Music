package com.zyc.doctor.http.data;

/**
 * @author luozi
 * @date 2015/12/29
 */
public class Global implements Mode {
    /**
     * 单例模式
     */
    private static Global instance = null;
    /**
     * 开发模式，此模式影响后台的输出，及前台显示
     */
    public Launch launchMode = Launch.DEBUG;
    /**
     * 控制模式，此模式影响app的可控性调节
     */
    public Control controlMode = Control.RELEASE;
    /**
     * app 调试标记头
     */
    private String appDebugHeader = "ZYC";

    /**
     * 单例模式
     */
    public static Global getInstance() {
        synchronized (Global.class) {
            if (instance == null) { instance = new Global(); }
            return instance;
        }
    }

    /**
     * 得到当前的开发模式
     */
    public Launch getLaunchMode() {
        return launchMode;
    }

    /**
     * 是否是debug模式
     */
    public boolean isDebugMode() {
        return launchMode == Launch.DEBUG;
    }

    /**
     * 是否是开发者模式
     */
    public boolean isReleaseMode() {
        return launchMode == Launch.RELEASE;
    }

    /**
     * 设置当前的运行模式
     */
    public void setLaunchMode(Launch launchMode) {
        this.launchMode = launchMode;
    }

    /**
     * 得到当前的控制模式
     */
    public Control getControlMode() {
        return controlMode;
    }

    /**
     * 设置当前的运行模式
     */
    public void setControlMode(Control conrolMode) {
        this.controlMode = conrolMode;
    }

    public String getAppDebugHeader() {
        return appDebugHeader;
    }

    public void setAppDebugHeader(String appDebugHeader) {
        this.appDebugHeader = appDebugHeader;
    }
}
