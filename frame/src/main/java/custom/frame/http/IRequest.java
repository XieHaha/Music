package custom.frame.http;

import android.content.Context;

import com.android.volley.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.LoginSuccessBean;
import custom.frame.bean.PatientBean;
import custom.frame.bean.PatientCaseBasicBean;
import custom.frame.bean.PatientCaseDetailBean;
import custom.frame.http.listener.ResponseListener;

import static custom.frame.http.data.HttpConstants.Method.GET;

/**
 * Created by luozi on 2015/12/30.
 * baseRequest include requestString and requestObject and requestList
 */
public class IRequest extends BaseRequest {
    /**
     * 单例模式
     */
    private volatile static IRequest instance = null;
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
     * 得到基础连接地址
     */
    private RequestParams getBaseMap(String requestName) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("m", "app_server");
        params.addBodyParameter("c", "api");
        params.addBodyParameter("a", requestName);
        return params;
    }

    /**
     * 获取验证码
     */
    public Tasks getVerifyCode(String phoneNumber, final ResponseListener<BaseResponse> listener) {

        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("phoneNum", phoneNumber);
        return requestBaseResponseByJson("/msg/send", Tasks.GET_VERIFY_CODE,
                String.class, merchant, listener);
    }

    /**
     * 登录 注册
     */
    public Tasks loginAndRegister(String phoneNumber, String code, String role, final ResponseListener<BaseResponse> listener) {
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
        return uploadFile("/f/uploadfile", Tasks.UPLOAD_FILE, file, type, String.class, listener);
    }

    /**
     * 上传基本信息
     */
    public Tasks updateBasicInfo(String doctorId, String name, String portraitUrl, final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("name", name);
        merchant.put("portraitUrl", portraitUrl);
        return requestBaseResponseByJson("/doctor/info/updateinfo", Tasks.UPDATE_BASIC_INFO,
                String.class, merchant, listener);
    }

    /**
     * 上传职业信息
     */
    public Tasks updateJobInfo(String doctorId, String department, String doctorDescription, String hospital, String title, final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
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
    public Tasks updateUserInfo(String doctorId, String name, String portraitUrl, String hospital, String department, String title, String doctorDescription, final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("name", name);
        merchant.put("portraitUrl", portraitUrl);
        merchant.put("department", department);
        merchant.put("doctorDescription", doctorDescription);
        merchant.put("hospital", hospital);
        merchant.put("title", title);
        return requestBaseResponseByJson("/doctor/info/updateall", Tasks.UPDATE_USER_INFO,
                String.class, merchant, listener);
    }

    /**
     * 获取患者列表
     */
    public Tasks getPatientList(String doctorId, int pageNo, int pageSize, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/dp/mypatient", Tasks.GET_PATIENTS_LIST,
                PatientBean.class, merchant, listener);
    }
    /**
     * 医生扫码添加患者或者转诊患者
     */
    public Tasks addPatientByScanOrChangePatient(String doctorId, String patientId, int requestSource, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        return requestBaseResponseByJson("/dp/focuspatient", Tasks.ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT,
                PatientBean.class, merchant, listener);
    }

    /**
     * 获取合作医生列表
     */
    public Tasks getCooperateList(String doctorId, int pageNo, int pageSize, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/colleborate/doctorList", Tasks.GET_COOPERATE_DOC_LIST,
                CooperateDocBean.class, merchant, listener);
    }

    /**
     * 获取申请合作医生列表
     */
    public Tasks getApplyCooperateList(String doctorId, int pageNo, int pageSize, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/colleborate/applyList", Tasks.GET_APPLY_COOPERATE_DOC_LIST,
                CooperateDocBean.class, merchant, listener);
    }

    /**
     * 获取患者申请列表
     */
    public Tasks getApplyPatientList(String doctorId, int pageNo, int pageSize, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/dp/patientrequest", Tasks.GET_APPLY_PATIENT_LIST,
                PatientBean.class, merchant, listener);
    }

    /**
     * 获取医生个人信息
     */
    public Tasks getDocInfo(String doctorId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("doctorId", doctorId);
        return requestBaseResponse(GET, "/doctor/info", Tasks.GET_DOC_INFO,
                CooperateDocBean.class, params, listener);
    }

    /**
     * 拒绝患者申请
     */
    public Tasks refusePatientApply(String doctorId, String patientId, int requestSource, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        return requestBaseResponseByJson("/dp/against", Tasks.REFUSE_PATIENT_APPLY,
                String.class, merchant, listener);
    }

    /**
     * 拒绝患者申请
     */
    public Tasks agreePatientApply(String doctorId, String patientId, int requestSource, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        return requestBaseResponseByJson("/dp/agree", Tasks.AGREE_PATIENT_APPLY,
                String.class, merchant, listener);
    }

    /**
     * 处理医生申请合作（proCode为字符1（表示同意）或字符3（表示拒绝））  appliedId被申请人  applyId申请人
     */
    public Tasks dealDocApply(String appliedId, String applyId, int proCode, int requestSource, final ResponseListener<BaseResponse> listener) {
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
        return requestBaseResponse(GET, "/patient/info", Tasks.GET_PATIENT_INFO,
                PatientBean.class, params, listener);
    }

    /**
     * 删除患者 （取消关注）
     */
    public Tasks deletePatient(String doctorId, String patientId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("doctorId ", doctorId);
        params.addBodyParameter("patientId", patientId);
        return requestBaseResponse(GET, "/dp/cancelfocus", Tasks.DELETE_PATIENT,
                String.class, params, listener);
    }

    /**
     * 获取患者手术信息
     */
    public Tasks getPatientSurgery(String patientId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("patientId", patientId);
        return requestBaseResponseList(GET, "/patient/surgery", Tasks.GET_PATIENT_SURGERY_INFO,
                PatientCaseBasicBean.class, params, listener);
    }

    /**
     * 获取患者诊断信息
     */
    public Tasks getPatientDiagnosis(String patientId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("patientId", patientId);
        return requestBaseResponseList(GET, "/patient/diagnosis", Tasks.GET_PATIENT_DIAGNOSIS_INFO,
                PatientCaseBasicBean.class, params, listener);
    }

    /**
     * 获取患者过敏史信息
     */
    public Tasks getPatientAllergy(String patientId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("patientId", patientId);
        return requestBaseResponseList(GET, "/patient/allergy", Tasks.GET_PATIENT_ALLERGY_INFO,
                PatientCaseBasicBean.class, params, listener);
    }

    /**
     * 获取患者病例列表
     */
    public Tasks getPatientCaseList(String patientId, int pageNo, int pageSize, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("patientId", patientId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        return requestBaseResponseListByJson("/case/patientCase", Tasks.GET_PATIENT_CASE_LIST,
                PatientCaseDetailBean.class, merchant, listener);
    }

    /**
     * 新增患者病例
     */
    public Tasks addPatientCase(String patientId, String checkReport, String patientWords, String currentInfo,
                                String diagnosisInfo, String doctorDep, String hospital, String importantHistory,
                                String reportImgUrl, String treat, String visDate, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>();
        merchant.put("patientId", patientId);
        merchant.put("checkReport", checkReport);
        merchant.put("currentInfo", currentInfo);
        merchant.put("diagnosisInfo", diagnosisInfo);
        merchant.put("doctorDep", doctorDep);
        merchant.put("hospital", hospital);
        merchant.put("importantHistory", importantHistory);
        merchant.put("reportImgUrl", reportImgUrl);
        merchant.put("patientWords", patientWords);
        merchant.put("treat", treat);
        merchant.put("visDate", visDate);
        return requestBaseResponseByJson("/case/save", Tasks.ADD_PATIENT_CASE,
                String.class, merchant, listener);
    }

    /**
     * 更新患者病例
     */
    public Tasks updatePatientCase(String patientId, int caseId, String checkReport, String patientWords, String currentInfo,
                                   String diagnosisInfo, String doctorDep, String hospital, String importantHistory,
                                   String reportImgUrl, String treat, String visDate, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>();
        merchant.put("patientId", patientId);
        merchant.put("caseId", caseId);
        merchant.put("checkReport", checkReport);
        merchant.put("currentInfo", currentInfo);
        merchant.put("diagnosisInfo", diagnosisInfo);
        merchant.put("doctorDep", doctorDep);
        merchant.put("hospital", hospital);
        merchant.put("importantHistory", importantHistory);
        merchant.put("patientWords", patientWords);
        merchant.put("reportImgUrl", reportImgUrl);
        merchant.put("treat", treat);
        merchant.put("visDate", visDate);
        return requestBaseResponseByJson("/case/update", Tasks.UPDATE_PATIENT_CASE,
                String.class, merchant, listener);
    }

}
