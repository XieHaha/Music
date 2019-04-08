package com.zyc.doctor.http.retrofit;

import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CooperateDocBean;
import com.zyc.doctor.data.bean.CooperateHospitalBean;
import com.zyc.doctor.data.bean.CooperateHospitalDocBean;
import com.zyc.doctor.data.bean.HospitalBean;
import com.zyc.doctor.data.bean.HospitalProductTypeBean;
import com.zyc.doctor.data.bean.LoginSuccessBean;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.data.bean.PatientCaseDetailBean;
import com.zyc.doctor.data.bean.RegistrationBean;
import com.zyc.doctor.data.bean.RegistrationTypeBean;
import com.zyc.doctor.data.bean.TransPatientBean;
import com.zyc.doctor.data.bean.Version;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
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
     * 处理医生申请合作（proCode为字符1（表示同意）或字符3（表示拒绝））  appliedId被申请人  applyId申请人
     *
     * @param info
     * @return
     */
    @POST("colleborate/applyProcess")
    Observable<BaseResponse<PatientBean>> dealDocApply(@Body Map<String, Object> info);

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

    /**
     * 上传图片
     *
     * @param file
     * @param type
     * @return
     */
    @Multipart
    @POST("file/upload")
    Observable<BaseResponse<String>> uploadImg(@Part MultipartBody.Part file, @Query("type") String type);

    /**
     * 医生资质认证
     *
     * @param files
     * @param info
     * @return
     */
    @Multipart
    @POST("doctor/info/qualifiy")
    Observable<BaseResponse<String>> qualifiyDoc(@Part MultipartBody.Part[] files, @QueryMap Map<String, Object> info);

    /**
     * 更改个人信息
     *
     * @param info
     * @return
     */
    @POST("doctor/info/update")
    Observable<BaseResponse<String>> updateUserInfo(@Body Map<String, Object> info);

    /**
     * 获取转诊出去患者列表
     *
     * @param info
     * @return
     */
    @POST("trans/out/doctor/notes")
    Observable<BaseResponse<List<TransPatientBean>>> getTransferPatientToList(@Body Map<String, Object> info);

    /**
     * 获取收到转诊患者列表
     *
     * @param info
     * @return
     */
    @POST("trans/in/doctor/notes")
    Observable<BaseResponse<List<TransPatientBean>>> getTransferPatientFromList(@Body Map<String, Object> info);

    /**
     * 获取某个患者的转诊单
     *
     * @param info
     * @return
     */
    @POST("trans/doctor/patient/notes")
    Observable<BaseResponse<List<TransPatientBean>>> getTransferByPatient(@Body Map<String, Object> info);

    /**
     * 获取当前医生给患者患者所有订单
     *
     * @param info
     * @return
     */
    @POST("order/doctor/patient/notes")
    Observable<BaseResponse<List<RegistrationBean>>> getPatientOrders(@Body Map<String, Object> info);

    /**
     * 获取患者病例列表
     *
     * @param info
     * @return
     */
    @POST("case/doctor/query")
    Observable<BaseResponse<List<PatientCaseDetailBean>>> getPatientLimitCaseList(@Body Map<String, Object> info);

    /**
     * 删除患者病例
     *
     * @param info
     * @return
     */
    @POST("case/delete")
    Observable<BaseResponse<String>> deletePatientCase(@Body Map<String, Object> info);

    /**
     * 获取医生个人信息
     *
     * @param doctorId
     * @return
     */
    @GET("doctor/info/{doctorId}")
    Observable<BaseResponse<CooperateDocBean>> getDocInfo(@Path("doctorId") String doctorId);

    /**
     * 获取患者个人信息
     *
     * @param patientId
     * @return
     */
    @GET("patient/info/{patientId}")
    Observable<BaseResponse<PatientBean>> getPatientInfo(@Path("patientId") String patientId);

    /**
     * 获取转诊详情
     *
     * @param info
     * @return
     */
    @POST("trans/single/detail")
    Observable<BaseResponse<TransPatientBean>> getTransferDetailById(@Body Map<String, Integer> info);

    /**
     * 医生转诊患者
     *
     * @param info
     * @return
     */
    @POST("trans/doctor/add/notes")
    Observable<BaseResponse<String>> addTransferPatient(@Body Map<String, Object> info);

    /**
     * 取消转诊
     *
     * @param info
     * @return
     */
    @POST("trans/doctor/cancel/notes")
    Observable<BaseResponse<String>> cancelTransferPatient(@Body Map<String, Integer> info);

    /**
     * 拒绝转诊
     *
     * @param info
     * @return
     */
    @POST("trans/doctor/refuse")
    Observable<BaseResponse<String>> refuseTransferPatient(@Body Map<String, Integer> info);

    /**
     * 接受转诊
     *
     * @param info
     * @return
     */
    @POST("trans/doctor/receive")
    Observable<BaseResponse<String>> recvTransferPatient(@Body Map<String, Object> info);

    /**
     * 根据医生id获取医院列表
     *
     * @param info
     * @return
     */
    @POST("hospital/doctor/relation/list")
    Observable<BaseResponse<List<HospitalBean>>> getHospitalListByDoctorId(@Body Map<String, Object> info);

    /**
     * 获取合作医院下面的医生
     *
     * @param info
     * @return
     */
    @POST("hospital/doctor/relation/internal/doctor/list")
    Observable<BaseResponse<List<CooperateHospitalDocBean>>> getCooperateHospitalDoctorList(
            @Body Map<String, Object> info);

    /**
     * 根据医院id获取商品类型和类型下的商品详情
     *
     * @param info
     * @return
     */
    @POST("product/info/doctor/hospital/type/product")
    Observable<BaseResponse<List<HospitalProductTypeBean>>> getHospitalProductListByHospitalId(
            @Body Map<String, Object> info);

    /**
     * 新增订单
     *
     * @param info
     * @return
     */
    @POST("product/info/doctor/operator/add")
    Observable<BaseResponse<String>> addProductOrderNew(@Body Map<String, Object> info);

    /**
     * 医生转诊患者
     *
     * @param info
     * @return
     */
    @POST("dp/trans/focus/doctor")
    Observable<BaseResponse<String>> addPatientByScanOrChangePatient(@Body Map<String, Object> info);

    /**
     * 删除患者 （取消关注）
     *
     * @param doctorId
     * @param patientId
     * @return
     */
    @GET("dp/cancel/focus/{doctorId}/{patientId}")
    Observable<BaseResponse<String>> deletePatient(@Path("doctorId") String doctorId,
            @Path("patientId") String patientId);

    /**
     * 删除患者 （取消关注）
     *
     * @param info
     * @return
     */
    @POST("hospital/doctor/relation/list")
    Observable<BaseResponse<List<CooperateHospitalBean>>> getCooperateHospitalList(@Body Map<String, String> info);

    /**
     * 获取病例详情
     *
     * @param info
     * @return
     */
    @POST("order/single")
    Observable<BaseResponse<RegistrationBean>> getDetailById(@Body Map<String, String> info);

    /**
     * 获取患者综合病史接口
     *
     * @param patientId
     * @return
     */
    @GET("patient/combine/{patientId}")
    Observable<BaseResponse<RegistrationBean>> getPatientCombine(@Path("patientId") String patientId);

    /**
     * 新增患者病例
     *
     * @param info
     * @return
     */
    @POST("case/save")
    Observable<BaseResponse<String>> addPatientCase(@Body Map<String, Object> info);

    /**
     * 更新患者病例
     *
     * @param info
     * @return
     */
    @POST("case/update")
    Observable<BaseResponse<String>> updatePatientCase(@Body Map<String, Object> info);

    /**
     * 合作医生添加备注
     *
     * @param info
     * @return
     */
    @POST("colleborate/nickname")
    Observable<BaseResponse<String>> modifyNickName(@Body Map<String, Object> info);

    /**
     * 患者备注设置
     * 医生发起的修改，from为’d’，修改的是显示病人的昵称；病人发起的修改，from为’p’，修改的是显示医生的昵称
     *
     * @param info
     * @return
     */
    @POST("dp/nickname")
    Observable<BaseResponse<String>> modifyNickNameByPatient(@Body Map<String, Object> info);

    /**
     * 医生扫码添加患者
     *
     * @param info
     * @return
     */
    @POST("dp/scan/focus/patient")
    Observable<BaseResponse<PatientBean>> addPatientByScan(@Body Map<String, Object> info);

    /**
     * 拒绝患者申请
     *
     * @param info
     * @return
     */
    @POST("dp/scan/against/patient")
    Observable<BaseResponse<String>> refusePatientApply(@Body Map<String, Object> info);

    /**
     * 同意患者申请
     *
     * @param info
     * @return
     */
    @POST("dp/scan/agree/V2.0")
    Observable<BaseResponse<String>> agreePatientApply(@Body Map<String, Object> info);
}
