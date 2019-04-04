package com.zyc.doctor;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseUser;
import com.yanzhenjie.nohttp.NoHttp;
import com.zyc.doctor.api.notify.NotifyHelper;
import com.zyc.doctor.chat.HxHelper;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.http.bean.LoginSuccessBean;
import com.zyc.doctor.http.retrofit.RetrofitManager;
import com.zyc.doctor.utils.ImageLoadUtil;
import com.zyc.doctor.utils.RecentContactUtils;
import com.zyc.doctor.utils.SharePreferenceUtil;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * 2018年4月4日16:40:23
 *
 * @author DUNDUN
 */
public class YihtApplication extends LitePalApplication {
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
    /**
     * 当前正在聊天的id
     */
    private String chatId;
    /**
     * 版本更新标识
     */
    private boolean versionRemind = false;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        initContext();
        initAndroidAutoSize();
        //网络
        RetrofitManager.getInstance().init();
        //监听类初始化
        NotifyHelper.init(this);
        //处理文件下载上传
        NoHttp.initialize(this);
        //数据库
        LitePal.initialize(this);
        //环信
        initEase();
        //极光
        initJPush();
        //
        initImageLoader();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initContext() {
        sApplication = this;
    }

    /**
     * 界面适配
     */
    private void initAndroidAutoSize() {
        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        //在 Demo 中跳转的三方库中的 DefaultErrorActivity 就是在另外一个进程中, 所以要想适配这个 Activity 就需要调用
        // initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance().getUnitsManager().setSupportSP(false).setSupportSubunits(Subunits.MM);
        AutoSizeConfig.getInstance()
                      //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
                      //如果没有这个需求建议不开启
                      .setCustomFragment(true)
                      //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
                      //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
                      .setExcludeFontScale(true);
    }

    /**
     * 环信初始化
     */
    private void initEase() {
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
        //设置有关环信自定义的相关配置  titlebar、头像、名字处理
        HxHelper.Opts opts = new HxHelper.Opts();
        opts.setShowChatTitle(false);
        HxHelper.getInstance().init(this);
        EaseUI.getInstance().setUserProfileProvider((username, callback) -> {
            LoginSuccessBean bean = getLoginSuccessBean();
            //如果是当前用户，就设置自己的昵称和头像
            if (null != bean && TextUtils.equals(bean.getDoctorId(), username)) {
                EaseUser eu = new EaseUser(username);
                eu.setNickname(bean.getName());
                eu.setAvatar(bean.getPortraitUrl());
                callback.onSuccess(eu);
                return eu;
            }
            //否则交给HxHelper处理，从消息中获取昵称和头像
            return HxHelper.getInstance().getUser(username, callback);
        });
    }

    /**
     * 极光推送 初始化
     */
    private void initJPush() {
        //极光推送
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

    /**
     * ImageLoader 参数化配置
     */
    private void initImageLoader() {
        ImageLoadUtil.getInstance().initImageLoader(getApplicationContext());
    }

    public static YihtApplication getInstance() {
        return sApplication;
    }

    public LoginSuccessBean getLoginSuccessBean() {
        String userStr = (String)SharePreferenceUtil.getObject(this, CommonData.KEY_LOGIN_SUCCESS_BEAN, "");
        if (!TextUtils.isEmpty(userStr)) {
            loginSuccessBean = JSON.parseObject(userStr, LoginSuccessBean.class);
        }
        return loginSuccessBean;
    }

    public void setLoginSuccessBean(LoginSuccessBean loginSuccessBean) {
        this.loginSuccessBean = loginSuccessBean;
        SharePreferenceUtil.putObject(this, CommonData.KEY_LOGIN_SUCCESS_BEAN, loginSuccessBean);
    }

    /**
     * 清楚登录数据
     */
    public void clearLoginSuccessBean() {
        SharePreferenceUtil.remove(this, CommonData.KEY_LOGIN_SUCCESS_BEAN);
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getEaseHeadImgUrl() {
        return easeHeadImgUrl;
    }

    public void setEaseHeadImgUrl(String easeHeadImgUrl) {
        this.easeHeadImgUrl = easeHeadImgUrl;
    }

    public String getEaseName() {
        return easeName;
    }

    public void setEaseName(String easeName) {
        this.easeName = easeName;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public boolean isVersionRemind() {
        return versionRemind;
    }

    public void setVersionRemind(boolean versionRemind) {
        this.versionRemind = versionRemind;
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
