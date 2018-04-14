package com.yht.yihuantong;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.yht.yihuantong.data.CommonData;

import cn.jpush.android.api.JPushInterface;
import custom.frame.bean.LoginSuccessBean;
import custom.frame.utils.SharePreferenceUtil;

/**
 * 2018年4月4日16:40:23
 *
 * @author DUNDUN
 */
public class YihtApplication extends Application {
    private static YihtApplication sApplication;
    private LoginSuccessBean loginSuccessBean;
    /**
     * 临时数据  头像url
     */
    private String headImgUrl;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        //        //环信初始化
        //        EMOptions options = new EMOptions();
        //        // 默认添加好友时，是不需要验证的，改成需要验证
        //        options.setAcceptInvitationAlways(false);
        //        //        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        //        //        options.setAutoTransferMessageAttachments(true);
        //        //        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        //        //        options.setAutoDownloadThumbnail(true);
        //        //初始化
        //        EMClient.getInstance().init(this, options);
        //        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        //        EMClient.getInstance().setDebugMode(true);

        //极光推送
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

    private void init() {
        sApplication = this;
    }

    public static YihtApplication getInstance() {
        return sApplication;
    }


    public LoginSuccessBean getLoginSuccessBean() {
        String userStr = (String) SharePreferenceUtil.get(this, CommonData.KEY_LOGIN_SUCCESS_BEAN, "");
        if (!TextUtils.isEmpty(userStr)) {
            loginSuccessBean = JSON.parseObject(userStr, LoginSuccessBean.class);
        }
        return loginSuccessBean;
    }

    public void setLoginSuccessBean(LoginSuccessBean loginSuccessBean) {
        this.loginSuccessBean = loginSuccessBean;
        SharePreferenceUtil.put(this, CommonData.KEY_LOGIN_SUCCESS_BEAN, loginSuccessBean);
    }

    /**
     * 清楚登录数据
     */
    public void clearLoginSuccessBean()
    {
        SharePreferenceUtil.remove(this,CommonData.KEY_LOGIN_SUCCESS_BEAN);
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    /**
     * app字体不随系统改变而改变
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        //        }
        return resources;
    }
}
