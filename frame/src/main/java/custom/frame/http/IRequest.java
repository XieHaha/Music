package custom.frame.http;

import android.content.Context;

import com.android.volley.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CombineBean;
import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.CooperateHospitalDocBean;
import custom.frame.bean.HospitalBean;
import custom.frame.bean.HospitalProductTypeBean;
import custom.frame.bean.LoginSuccessBean;
import custom.frame.bean.PatientBean;
import custom.frame.bean.PatientCaseDetailBean;
import custom.frame.bean.RegistrationBean;
import custom.frame.bean.RegistrationTypeBean;
import custom.frame.bean.TransPatientBean;
import custom.frame.bean.Version;
import custom.frame.http.listener.ResponseListener;

import static custom.frame.http.data.HttpConstants.Method.GET;

/**
 * baseRequest include requestString and requestObject and requestList
 */
public class IRequest extends BaseRequest {
    /**
     * 单例模式
     */
    private static IRequest instance = null;
    /**
     * 公共模块
     */
    private String F = "f";
    private String MSG = "msg";
    private String RELOG = "relog";

    /**
     * 单例模式
     */
    public static IRequest getInstance(Context context) {
        synchronized (IRequest.class) {
            if (instance == null) {
            }
            instance = new IRequest(context);
            return instance;
        }
    }

    /**
     * 一个新的单例
     */
    public static IRequest newInstance(Context context) {
        return new IRequest(context);
    }

    /**
     * 父类构造函数
     */
    private IRequest(Context context) {
        super(context);
    }

    /**
     * 获取环信appkey
     */
    public Tasks getEaseAppKey(final ResponseListener<BaseResponse> listener) {
        return requestBaseResponse(GET, "/huanxin/apiKey", Tasks.GET_EASE_APPKEY, String.class,
                null, listener);
    }

    /**
     * 获取广告页
     */
    public Tasks getSplash(String client, String deviceSystem, String versionCode,
                           final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("client", client);
        merchant.put("deviceSystem", deviceSystem);
        merchant.put("edition", versionCode);
        return requestBaseResponseByJson("/DPInternal/resource/splash", Tasks.GET_SPLASH,
                String.class, merchant, listener);
    }

    /**
     * 获取验证码
     */
    public Tasks getVerifyCode(String phoneNumber, final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("phoneNum", phoneNumber);
        return requestBaseResponseByJson("/msg/send", Tasks.GET_VERIFY_CODE, String.class, merchant,
                listener);
    }

    /**
     * 登录 注册
     */
    public Tasks loginAndRegister(String phoneNumber, String code, String role,
                                  final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("phoneNum", phoneNumber);
        merchant.put("code", code);
        merchant.put("role", role);
        return requestBaseResponseByJson("/relog/relog", Tasks.LOGIN_AND_REGISTER,
                LoginSuccessBean.class, merchant, listener);
    }

    /**
     * 上传头像
     */
    public Tasks uploadHeadImg(File file, String type,
                               final ResponseListener<BaseResponse> listener) {
        return uploadFile("/file/upload", Tasks.UPLOAD_FILE, file, type, String.class, listener);
    }

    /**
     * 上传基本信息
     */
    public Tasks updateBasicInfo(String doctorId, int checked, String name, String portraitUrl,
                                 final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("checked", checked);
        merchant.put("name", name);
        merchant.put("portraitUrl", portraitUrl);
        return requestBaseResponseByJson("/doctor/info/updateinfo", Tasks.UPDATE_BASIC_INFO,
                String.class, merchant, listener);
    }

    /**
     * 上传职业信息
     */
    public Tasks updateJobInfo(String doctorId, int checked, String department,
                               String doctorDescription, String hospital, String title,
                               final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("checked", checked);
        merchant.put("department", department);
        merchant.put("doctorDescription", doctorDescription);
        merchant.put("hospital", hospital);
        merchant.put("title", title);
        return requestBaseResponseByJson("/doctor/info/updatedetail", Tasks.UPDATE_JOB_INFO,
                String.class, merchant, listener);
    }

    /**
     * 更改个人信息
     */
    public Tasks updateUserInfo(String doctorId, int fieldId, com.alibaba.fastjson.JSONObject json,
                                final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("fieldId", fieldId);
        merchant.put("json", json);
        return requestBaseResponseByJson("/doctor/info/update", Tasks.UPDATE_USER_INFO,
                String.class, merchant, listener);
    }

    /**
     * 获取患者列表
     */
    public Tasks getPatientList(String doctorId, int pageNo, int pageSize,
                                final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/dp/patient", Tasks.GET_PATIENTS_LIST,
                PatientBean.class, merchant, listener);
    }

    /**
     * 获取转诊出去患者列表
     */
    public Tasks getTransferPatientToList(String doctorId, int pageNo, int pageSize,
                                          final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/trans/out/doctor/notes", Tasks.GET_PATIENTS_TO_LIST,
                TransPatientBean.class, merchant, listener);
    }

    /**
     * 获取收到转诊患者列表
     */
    public Tasks getTransferPatientFromList(String doctorId, int pageNo, int pageSize,
                                            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/trans/in/doctor/notes", Tasks.GET_PATIENTS_FROM_LIST,
                TransPatientBean.class, merchant, listener);
    }

    /**
     * 医生转诊患者
     */
    public Tasks addPatientByScanOrChangePatient(String doctorId, String fromDoctorId,
                                                 String patientId, int requestSource, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("toDoctorId", doctorId);
        merchant.put("fromDoctorId", fromDoctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        return requestBaseResponseByJson("/dp/trans/focus/doctor",
                Tasks.ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT,
                PatientBean.class, merchant, listener);
    }

    /**
     * 医生扫码添加患者
     */
    public Tasks addPatientByScan(String doctorId, String patientId, int requestSource,
                                  final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        return requestBaseResponseByJson("/dp/scan/focus/patient",
                Tasks.ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT,
                PatientBean.class, merchant, listener);
    }

    /**
     * 合作医生申请
     */
    public Tasks applyCooperateDoc(String colleborateDoctorId, String doctorId, int requestSource,
                                   final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("colleborateDoctorId", colleborateDoctorId);
        merchant.put("requestSource", requestSource);
        return requestBaseResponseByJson("/colleborate/applyRequest", Tasks.APPLY_COOPERATE_DOC,
                PatientBean.class, merchant, listener);
    }

    /**
     * 合作医生添加备注
     */
    public Tasks modifyNickName(String doctorId, String colleborateDoctorId, String nickname,
                                final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("colleborateDoctorId", colleborateDoctorId);
        merchant.put("nickname", nickname);
        return requestBaseResponseByJson("/colleborate/nickname", Tasks.MODIFY_NICK_NAME,
                String.class, merchant, listener);
    }

    /**
     * 患者备注设置
     * 医生发起的修改，from为’d’，修改的是显示病人的昵称；病人发起的修改，from为’p’，修改的是显示医生的昵称
     */
    public Tasks modifyNickNameByPatient(String doctorId, String patientId, String nickname,
                                         String from, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("nickname", nickname);
        merchant.put("from", from);
        return requestBaseResponseByJson("/dp/nickname", Tasks.MODIFY_NICK_NAME_BY_PATIENT,
                String.class, merchant, listener);
    }

    /**
     * 获取合作医生列表
     */
    public Tasks getCooperateList(String doctorId, int pageNo, int pageSize,
                                  final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/colleborate/doctorList",
                Tasks.GET_COOPERATE_DOC_LIST, CooperateDocBean.class,
                merchant, listener);
    }

    /**
     * 取消合作医生关系
     * doctorId 为操作人id   doctorId2为被操作人id
     */
    public Tasks cancelCooperateDoc(String doctorId, String doctorId2,
                                    final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("id1", doctorId);
        params.addBodyParameter("id2", doctorId2);
        return requestBaseResponse(GET, "/colleborate/delete", Tasks.CANCEL_COOPERATE_DOC,
                CooperateDocBean.class, params, listener);
    }

    /**
     * 获取申请合作医生列表
     */
    public Tasks getApplyCooperateList(String doctorId, int pageNo, int pageSize,
                                       final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/colleborate/applyList",
                Tasks.GET_APPLY_COOPERATE_DOC_LIST,
                CooperateDocBean.class, merchant, listener);
    }

    /**
     * 获取患者申请列表
     */
    public Tasks getApplyPatientList(String doctorId, int pageNo, int pageSize,
                                     final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/dp/patient/request", Tasks.GET_APPLY_PATIENT_LIST,
                PatientBean.class, merchant, listener);
    }

    /**
     * 获取医生个人信息
     */
    public Tasks getDocInfo(String doctorId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("doctorId", doctorId);
        return requestBaseResponse(GET, "/doctor/info", Tasks.GET_DOC_INFO, CooperateDocBean.class,
                params, listener);
    }

    /**
     * 拒绝患者申请
     */
    public Tasks refusePatientApply(String doctorId, String patientId, int requestSource,
                                    final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        return requestBaseResponseByJson("/dp/scan/against/patient", Tasks.REFUSE_PATIENT_APPLY,
                String.class, merchant, listener);
    }

    /**
     * 同意患者申请
     */
    public Tasks agreePatientApply(String doctorId, String patientId, int requestSource,
                                   final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("fromId", doctorId);//操作者id
        merchant.put("requestSource", requestSource);
        return requestBaseResponseByJson("/dp/scan/agree/V2.0", Tasks.AGREE_PATIENT_APPLY,
                String.class, merchant, listener);
    }

    /**
     * 处理医生申请合作（proCode为字符1（表示同意）或字符3（表示拒绝））  appliedId被申请人  applyId申请人
     */
    public Tasks dealDocApply(String appliedId, String applyId, int proCode, int requestSource,
                              final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("appliedId", appliedId);
        merchant.put("applyId", applyId);
        merchant.put("proCode", proCode);
        merchant.put("requestSource", requestSource);
        return requestBaseResponseByJson("/colleborate/applyProcess", Tasks.DEAL_DOC_APPLY,
                String.class, merchant, listener);
    }

    /**
     * 获取患者个人信息
     */
    public Tasks getPatientInfo(String patientId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("patientId", patientId);
        return requestBaseResponse(GET, "/patient/info", Tasks.GET_PATIENT_INFO, PatientBean.class,
                params, listener);
    }

    /**
     * 删除患者 （取消关注）
     */
    public Tasks deletePatient(String doctorId, String patientId,
                               final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("doctorId ", doctorId);
        params.addBodyParameter("patientId", patientId);
        return requestBaseResponse(GET, "/dp/cancel/focus", Tasks.DELETE_PATIENT, String.class,
                params, listener);
    }

    /**
     * 获取患者病例列表
     */
    public Tasks getPatientLimitCaseList(String doctorId, String patientId,
                                         final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        return requestBaseResponseListByJson("/case/doctor/query",
                Tasks.GET_PATIENT_LIMIT_CASE_LIST,
                PatientCaseDetailBean.class, merchant, listener);
    }

    /**
     * 好友验证 是否为好友
     */
    public Tasks friendsVerify(String doctorId, String patientId,
                               final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("doctorId ", doctorId);
        params.addBodyParameter("patientId", patientId);
        return requestBaseResponse(GET, "/dp/verify", Tasks.FRIENDS_VERIFY, CooperateDocBean.class,
                params, listener);
    }

    /**
     * 删除患者病例
     */
    public Tasks deletePatientCase(String patientId, int fieldId, String caseCreatorId,
                                   String caseOperatorId, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>();
        merchant.put("patientId", patientId);
        merchant.put("fieldId", fieldId);
        merchant.put("caseCreatorId", caseCreatorId);
        merchant.put("caseOperatorId", caseOperatorId);
        return requestBaseResponseByJson("/case/delete", Tasks.DELETE_PATIENT_CASE, String.class,
                merchant, listener);
    }

    /**
     * 医生资质认证
     */
    public Tasks qualifiyDoc(String doctorId, String name, String identityNumber, String title,
                             String department, String hospital, File idFront, File idEnd, File qualifiedFront,
                             File qualifiedEnd, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>();
        merchant.put("doctorId", doctorId);
        merchant.put("name", name);
        merchant.put("identityNumber", identityNumber);
        merchant.put("hospital", hospital);
        merchant.put("title", title);
        merchant.put("department", department);
        Map<String, File> files = new HashMap<>();
        files.put("idFront", idFront);
        files.put("idEnd", idEnd);
        files.put("qualifiedFront", qualifiedFront);
        files.put("qualifiedEnd", qualifiedEnd);
        return qualityDoc("/doctor/info/qualifiy", Tasks.QUALIFIY_DOC, files, merchant,
                String.class, listener);
    }

    /**
     * 版本更新
     */
    public Tasks getNewVersion(final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("device_system", "android");
        merchant.put("client", "doctor");
        return requestBaseResponseListByJson("/app/version", Tasks.UPDATE_VERSION, Version.class,
                merchant, listener);
    }

    /**
     * 获取医院列表
     */
    public Tasks getHospitalList(String doctorId, int productTypeId,
                                 final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("productTypeId", productTypeId);
        return requestBaseResponseListByJson("/product/info/doctor/hospital",
                Tasks.GET_HOSPITAL_LIST, HospitalBean.class, merchant,
                listener);
    }
    //    /**
    //     * 根据医生id获取医院列表
    //     */
    //    public Tasks getHospitalListByDoctorId(String doctorId,
    //            final ResponseListener<BaseResponse> listener)
    //    {
    //        Map<String, Object> merchant = new HashMap<>(16);
    //        merchant.put("doctorId", doctorId);
    //        return requestBaseResponseListByJson("/doctor/info/hospitals",
    //                                             Tasks.GET_HOSPITAL_LIST_BY_DOCTORID,
    //                                             HospitalBean.class, merchant, listener);
    //    }

    /**
     * 根据医生id获取医院列表  2019年2月21日16:02:40
     */
    public Tasks getHospitalListByDoctorId(String doctorId,
                                           final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        return requestBaseResponseListByJson("/hospital/doctor/relation/list",
                Tasks.GET_HOSPITAL_LIST_BY_DOCTORID,
                HospitalBean.class, merchant, listener);
    }

    /**
     * 获取我的合作医院
     */
    public Tasks getCooperateHospitalListByDoctorId(String doctorId,
                                                    final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        return requestBaseResponseListByJson("/hospital/doctor/relation/collaborate/hospital/list",
                Tasks.GET_COOPERATE_HOSPITAL_LIST_BY_DOCTORID,
                HospitalBean.class, merchant, listener);
    }

    /**
     * 获取合作医院下面的医生
     */
    public Tasks getCooperateHospitalDoctorList(String hospitalId, int pageNo, int pageSize,
                                                final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("hospitalId", hospitalId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/hospital/doctor/relation/internal/doctor/list",
                Tasks.GET_COOPERATE_HOSPITAL_DOCTOR_LIST,
                CooperateHospitalDocBean.class, merchant, listener);
    }

    /**
     * 根据医院id获取商品类型和类型下的商品详情
     */
    public Tasks getHospitalProductListByHospitalId(String hospitalId,
                                                    final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("hospitalId", hospitalId);
        return requestBaseResponseListByJson("/product/info/doctor/hospital/type/product",
                Tasks.GET_HOSPITAL_PRODUCT_LIST_BY_HOSPITALID,
                HospitalProductTypeBean.class, merchant, listener);
    }

    /**
     * 获取所有商品
     */
    public Tasks getAllProduct(final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        return requestBaseResponseList(GET, "/product/type/all", Tasks.GET_ALL_PRODUCT,
                RegistrationTypeBean.class, params, listener);
    }

    /**
     * 获取患者综合病史接口
     */
    public Tasks getPatientCombine(String patientId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("patientId", patientId);
        return requestBaseResponse(GET, "/patient/combine", Tasks.GET_PATIENT_COMBINE,
                CombineBean.class, params, listener);
    }

    /**
     * 我的转诊记录
     */
    public Tasks getTransferPatientHistoryList(String patientId, int pageNo, int pageSize, int days,
                                               final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("patientId", patientId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        //传0 代表不限制时间
        merchant.put("days", days);
        return requestBaseResponseListByJson("/trans/patient/notes",
                Tasks.GET_TRANSFER_PATIENT_HISTORY_LIST,
                TransPatientBean.class, merchant, listener);
    }

    /**
     * 获取某个患者的转诊单
     */
    public Tasks getTransferByPatient(String doctorId, String patientId, int pageNo, int pageSize,
                                      int days, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        //传0 代表不限制时间
        merchant.put("days", days);
        return requestBaseResponseListByJson("/trans/doctor/patient/notes",
                Tasks.GET_TRANSFER_BY_PATIENT, TransPatientBean.class,
                merchant, listener);
    }

    /**
     * 获取患者所有订单
     */
    public Tasks getPatientAllOrders(String patientId, int page, int pageSize,
                                     final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", page);
        params.put("pageSize", pageSize);
        params.put("patientId", patientId);
        return requestBaseResponseListByJson("/order/patient/all/orders",
                Tasks.GET_PATIENT_ALL_ORDER_LIST,
                RegistrationBean.class, params, listener);
    }

    /**
     * 获取当前医生给患者患者所有订单
     */
    public Tasks getPatientOrders(String doctorId, String patientId, int page, int pageSize,
                                  final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>();
        params.put("days", 0);
        params.put("pageNo", page);
        params.put("pageSize", pageSize);
        params.put("patientId", patientId);
        params.put("doctorId", doctorId);
        return requestBaseResponseListByJson("/order/doctor/patient/notes",
                Tasks.GET_PATIENT_ORDER_LIST, RegistrationBean.class,
                params, listener);
    }
}
