package com.zyc.doctor.http.retrofit;

import com.zyc.doctor.http.bean.BaseResponse;
import com.zyc.doctor.http.bean.CooperateDocBean;
import com.zyc.doctor.http.bean.LoginSuccessBean;
import com.zyc.doctor.http.bean.PatientBean;
import com.zyc.doctor.http.bean.RegistrationBean;
import com.zyc.doctor.http.bean.RegistrationTypeBean;
import com.zyc.doctor.http.bean.TransPatientBean;
import com.zyc.doctor.http.bean.Version;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

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
     * 版本更新
     *
     * @param info map参数
     * @return
     */
    @POST("app/version")
    Observable<BaseResponse<Version>> getNewVersion(@Body Map<String, String> info);

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

    /**
     * 获取所有商品
     *
     * @return
     */
    @GET("product/type/all")
    Observable<BaseResponse<List<RegistrationTypeBean>>> getAllProduct();

    /**
     * 获取患者申请列表
     *
     * @param info
     * @return
     */
    @POST("dp/patient/request")
    Observable<BaseResponse<List<PatientBean>>> getApplyPatientList(@Body Map<String, Object> info);

    /**
     * 获取患者列表
     *
     * @param info
     * @return
     */
    @POST("dp/patient")
    Observable<BaseResponse<List<PatientBean>>> getPatientList(@Body Map<String, Object> info);

    /**
     * 转诊记录   包括转入转出
     *
     * @param info
     * @return
     */
    @POST("trans/all/doctor/notes")
    Observable<BaseResponse<List<TransPatientBean>>> getTransferList(@Body Map<String, Object> info);

    /**
     * 开单记录
     *
     * @param info
     * @return
     */
    @POST("order/doctor/orders/list")
    Observable<BaseResponse<List<RegistrationBean>>> getOrderList(@Body Map<String, Object> info);

    /**
     * 获取合作医生列表
     *
     * @param info
     * @return
     */
    @POST("colleborate/doctorList")
    Observable<BaseResponse<List<CooperateDocBean>>> getCooperateList(@Body Map<String, Object> info);

    /**
     * 合作医生申请
     *
     * @param info
     * @return
     */
    @POST("colleborate/applyRequest")
    Observable<BaseResponse<PatientBean>> applyCooperateDoc(@Body Map<String, Object> info);

    /**
     * 取消合作医生关系
     * * doctorId 为操作人id   doctorId2为被操作人id
     *
     * @param info
     * @return
     */
    @GET("colleborate/delete")
    Observable<BaseResponse<String>> cancelCooperateDoc(@QueryMap Map<String, Object> info);

    /**
     * 获取申请合作医生列表
     *
     * @param info
     * @return
     */
    @POST("colleborate/applyList")
    Observable<BaseResponse<List<CooperateDocBean>>> getApplyCooperateList(@Body Map<String, Object> info);
}
