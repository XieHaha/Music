package com.zyc.doctor.http.retrofit;

import android.content.Context;

import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.http.bean.BaseResponse;
import com.zyc.doctor.http.listener.ResponseListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 提交参数方式
 *
 * @author dundun
 */
public class RequestUtils {
    /**
     * 获取广告页
     */
    public static void getSplash(Context context, String client, String deviceSystem, String versionCode,
            final ResponseListener listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("client", client);
        params.put("deviceSystem", deviceSystem);
        params.put("edition", versionCode);
        RetrofitManager.getApiUrlManager()
                       .getSplash(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, false, Tasks.GET_SPLASH, listener));
    }

    /**
     * 获取验证码
     */
    public static void getVerifyCode(Context context, String phoneNum, final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("phoneNum", phoneNum);
        RetrofitManager.getApiUrlManager()
                       .getVerifyCode(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_VERIFY_CODE, listener));
    }

    /**
     * Post 登录接口
     *
     * @param context
     * @param listener
     */
    public static void login(Context context, String name, String password, String role,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("phoneNum", name);
        params.put("code", password);
        params.put("role", role);
        RetrofitManager.getApiUrlManager()
                       .login(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.LOGIN_AND_REGISTER, listener));
    }
}

