package com.yht.yihuantong;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ease.HxHelper;

import cn.jpush.android.api.JPushInterface;
import custom.frame.bean.LoginSuccessBean;
import custom.frame.utils.SharePreferenceUtil;

/**
 * 2018年4月4日16:40:23
 *
 * @author DUNDUN
 */
public class YihtApplication extends MultiDexApplication
{
    private static YihtApplication sApplication;
    private LoginSuccessBean loginSuccessBean;
    /**
     * 临时数据  头像url
     */
    private String headImgUrl;
    /**
     * ease 临时数据  头像url
     */
    private String easeHeadImgUrl;
    /**
     * ease 临时数据  name
     */
    private String easeName;

    @Override
    public void onCreate()
    {
        MultiDex.install(this);
        super.onCreate();
        initContext();
        initEase();
        initJPush();
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initContext()
    {
        sApplication = this;
    }

    /**
     * 环信初始化
     */
    private void initEase()
    {
        //环信初始化
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(this, options);
//        //设置头像为圆形
//        EaseAvatarOptions avatarOpts = new EaseAvatarOptions();
//        //0：默认，1：圆形，2：矩形
//        avatarOpts.setAvatarShape(1);
//        EaseUI.getInstance().setAvatarOptions(avatarOpts);
//        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);
        //设置有关环信自定义的相关配置

        //titlebar、头像、名字处理
        HxHelper.Opts opts = new HxHelper.Opts();
        opts.showChatTitle = false;
        HxHelper.getInstance().init(this, opts);
        EaseUI.getInstance().setUserProfileProvider(username -> {
            LoginSuccessBean bean = getLoginSuccessBean();
            //如果是当前用户，就设置自己的昵称和头像
            if (null != bean && TextUtils.equals(bean.getDoctorId(), username)) {
                EaseUser eu = new EaseUser(username);
                eu.setNickname(bean.getName());
                eu.setAvatar(bean.getPortraitUrl());
                return eu;
            }
            //否则交给HxHelper处理，从消息中获取昵称和头像
            return HxHelper.getInstance().getUser(username);
        });

    }

    /**
     * 极光推送 初始化
     */
    private void initJPush()
    {
        //极光推送
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

    public static YihtApplication getInstance()
    {
        return sApplication;
    }

    public LoginSuccessBean getLoginSuccessBean()
    {
        String userStr = (String)SharePreferenceUtil.get(this, CommonData.KEY_LOGIN_SUCCESS_BEAN,
                                                         "");
        if (!TextUtils.isEmpty(userStr))
        {
            loginSuccessBean = JSON.parseObject(userStr, LoginSuccessBean.class);
        }
        return loginSuccessBean;
    }

    public void setLoginSuccessBean(LoginSuccessBean loginSuccessBean)
    {
        this.loginSuccessBean = loginSuccessBean;
        SharePreferenceUtil.put(this, CommonData.KEY_LOGIN_SUCCESS_BEAN, loginSuccessBean);
    }

    /**
     * 清楚登录数据
     */
    public void clearLoginSuccessBean()
    {
        SharePreferenceUtil.remove(this, CommonData.KEY_LOGIN_SUCCESS_BEAN);
    }

    public String getHeadImgUrl()
    {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl)
    {
        this.headImgUrl = headImgUrl;
    }

    public String getEaseHeadImgUrl()
    {
        return easeHeadImgUrl;
    }

    public void setEaseHeadImgUrl(String easeHeadImgUrl)
    {
        this.easeHeadImgUrl = easeHeadImgUrl;
    }

    public String getEaseName()
    {
        return easeName;
    }

    public void setEaseName(String easeName)
    {
        this.easeName = easeName;
    }

    /**
     * app字体不随系统改变而改变
     *
     * @return
     */
    @Override
    public Resources getResources()
    {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        //        }
        return resources;
    }
}
