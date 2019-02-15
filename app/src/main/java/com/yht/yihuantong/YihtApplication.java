package com.yht.yihuantong;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.Settings;
import com.alibaba.fastjson.JSON;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseUser;
import com.yanzhenjie.nohttp.NoHttp;
import com.yht.yihuantong.api.ApiHelper;
import com.yht.yihuantong.chat.HxHelper;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.xiaoyu.IncomingCallService;
import com.yht.yihuantong.utils.RecentContactUtils;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import custom.frame.bean.LoginSuccessBean;
import custom.frame.http.IRequest;
import custom.frame.utils.ImageLoadUtil;
import custom.frame.utils.SharePreferenceUtil;

/**
 * 2018年4月4日16:40:23
 *
 * @author DUNDUN
 */
public class YihtApplication extends LitePalApplication
{
    private static YihtApplication sApplication;
    private LoginSuccessBean loginSuccessBean;
    /**
     * 网络请求单例
     */
    public IRequest mIRequest = null;
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
    /**
     * 版本更新标识
     */
    private boolean versionRemind = false;

    @Override
    public void onCreate()
    {
        MultiDex.install(this);
        super.onCreate();
        initXYSDk();
        mIRequest = IRequest.getInstance(this);
        //监听类初始化
        ApiHelper.init(this);
        //处理文件下载上传
        NoHttp.initialize(this);
        //数据库
        LitePal.initialize(this);
        initContext();
        initEase();
        initJPush();
        initImageLoader();
        initXYSDk();
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
        //最近联系人
        RecentContactUtils.init(this);
        //环信初始化
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(this, options);
        //设置头像为圆形
        EaseAvatarOptions avatarOpts = new EaseAvatarOptions();
        //0：默认，1：圆形，2：矩形
        avatarOpts.setAvatarShape(1);
        EaseUI.getInstance().setAvatarOptions(avatarOpts);
        //        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        //        EMClient.getInstance().setDebugMode(true);
        //设置有关环信自定义的相关配置
        //titlebar、头像、名字处理
        HxHelper.Opts opts = new HxHelper.Opts();
        opts.showChatTitle = false;
        HxHelper.getInstance().init(this, opts, mIRequest);
        EaseUI.getInstance().setUserProfileProvider((username, callback) ->
                                                    {
                                                        LoginSuccessBean bean = getLoginSuccessBean();
                                                        //如果是当前用户，就设置自己的昵称和头像
                                                        if (null != bean &&
                                                            TextUtils.equals(bean.getDoctorId(),
                                                                             username))
                                                        {
                                                            EaseUser eu = new EaseUser(username);
                                                            eu.setNickname(bean.getName());
                                                            eu.setAvatar(bean.getPortraitUrl());
                                                            callback.onSuccess(eu);
                                                            return eu;
                                                        }
                                                        //否则交给HxHelper处理，从消息中获取昵称和头像
                                                        return HxHelper.getInstance()
                                                                       .getUser(username, callback);
                                                    });
    }

    /**
     * 极光推送 初始化
     */
    private void initJPush()
    {
        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    /**
     * ImageLoader 参数化配置
     */
    private void initImageLoader()
    {
        ImageLoadUtil.getInstance().initImageLoader(getApplicationContext());
    }

    /**
     * 小鱼sdk
     */
    private void initXYSDk()
    {
        //        Settings settings = new Settings("BMVNGNNNNMNNT");   //线上环境
        Settings settings = new Settings("23a05bc3dcdaa4ec9936a5c01aa0804757f99a66");   //测试环境
        int pId = Process.myPid();
        String processName = "";
        ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(
                ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> ps = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo p : ps)
        {
            if (p.pid == pId)
            {
                processName = p.processName;
                break;
            }
        }
        // 避免被初始化多次
        if (processName.equals(getPackageName()))
        {
            Log.i("test", " NemoSDK init.....");
            NemoSDK nemoSDK = NemoSDK.getInstance();
            nemoSDK.init(this, settings);
            // 被叫服务，不使用被叫功能的请忽略
            Intent incomingCallService = new Intent(this, IncomingCallService.class);
            startService(incomingCallService);
        }
    }

    public static YihtApplication getInstance()
    {
        return sApplication;
    }

    public LoginSuccessBean getLoginSuccessBean()
    {
        String userStr = (String)SharePreferenceUtil.getObject(this,
                                                               CommonData.KEY_LOGIN_SUCCESS_BEAN,
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
        SharePreferenceUtil.putObject(this, CommonData.KEY_LOGIN_SUCCESS_BEAN, loginSuccessBean);
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

    public boolean isVersionRemind()
    {
        return versionRemind;
    }

    public void setVersionRemind(boolean versionRemind)
    {
        this.versionRemind = versionRemind;
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
