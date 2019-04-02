package com.zyc.doctor.http.retrofit;

import com.zyc.doctor.http.bean.BaseResponse;
import com.zyc.doctor.http.bean.LoginSuccessBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author dundun
 */
public interface ApiUrlManager {
    /**
     * 首页广告
     *
     * @param info map参数
     * @return
     */
    @POST("DPInternal/resource/splash")
    Observable<BaseResponse<String>> getSplash(@Body Map<String, String> info);

    /**
     * 获取验证码
     *
     * @param info map参数
     * @return
     */
    @POST("msg/send")
    Observable<BaseResponse<String>> getVerifyCode(@Body Map<String, String> info);

    /**
     * 登录
     *
     * @param info map参数
     * @return
     */
    @POST("relog/relog")
    Observable<BaseResponse<LoginSuccessBean>> login(@Body Map<String, String> info);
}
