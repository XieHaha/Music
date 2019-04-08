package com.zyc.doctor.http.retrofit;

import android.content.Context;

import com.google.gson.JsonObject;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CooperateDocBean;
import com.zyc.doctor.data.bean.HospitalBean;
import com.zyc.doctor.data.bean.HospitalProductBean;
import com.zyc.doctor.data.bean.HospitalProductTypeBean;
import com.zyc.doctor.data.bean.LoginSuccessBean;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.http.listener.ResponseListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 提交参数方式
 *
 * @author dundun
 */
public class RequestUtils {
    public static void getSplash(Context context, String client, String deviceSystem, String versionCode,
            final ResponseListener listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("client", client);
        params.put("deviceSystem", deviceSystem);
        params.put("edition", versionCode);
        RetrofitManager.getApiUrlManager()
                       .getSplash(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_SPLASH, listener));
    }

    public static void getNewVersion(Context context, final ResponseListener listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("device_system", "android");
        params.put("client", "doctor");
        RetrofitManager.getApiUrlManager()
                       .getNewVersion(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.UPDATE_VERSION, listener));
    }

    public static void getVerifyCode(Context context, String phoneNum, final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("phoneNum", phoneNum);
        RetrofitManager.getApiUrlManager()
                       .getVerifyCode(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_VERIFY_CODE, listener));
    }

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

    public static void getAllProduct(Context context, final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getAllProduct()
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_ALL_PRODUCT, listener));
    }

    public static void getApplyPatientList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getApplyPatientList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_APPLY_PATIENT_LIST, listener));
    }

    public static void getPatientList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getPatientList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_PATIENTS_LIST, listener));
    }

    public static void getTransferList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getTransferList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_TRANSFER_LIST, listener));
    }

    public static void getOrderList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getOrderList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_ORDER_LIST, listener));
    }

    public static void getCooperateList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getCooperateList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_COOPERATE_DOC_LIST, listener));
    }

    public static void applyCooperateDoc(Context context, String colleborateDoctorId, String doctorId,
            int requestSource, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("colleborateDoctorId", colleborateDoctorId);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .applyCooperateDoc(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.APPLY_COOPERATE_DOC, listener));
    }

    public static void dealDocApply(Context context, String appliedId, String applyId, int proCode, int requestSource,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("appliedId", appliedId);
        merchant.put("applyId", applyId);
        merchant.put("proCode", proCode);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .dealDocApply(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.DEAL_DOC_APPLY, listener));
    }

    public static void cancelCooperateDoc(Context context, String doctorId, String doctorId2,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("id1", doctorId);
        merchant.put("id2", doctorId2);
        RetrofitManager.getApiUrlManager()
                       .cancelCooperateDoc(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.CANCEL_COOPERATE_DOC, listener));
    }

    public static void getApplyCooperateList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getApplyCooperateList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_APPLY_COOPERATE_DOC_LIST, listener));
    }

    public static void uploadImg(Context context, File file, String type,
            final ResponseListener<BaseResponse> listener) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RetrofitManager.getApiUrlManager()
                       .uploadImg(body, type)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.UPLOAD_FILE, listener));
    }

    public static void qualifiyDoc(Context context, String doctorId, String name, String identityNumber, String title,
            String department, String hospital, File idFront, File idEnd, File qualifiedFront, File qualifiedEnd,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("name", name);
        merchant.put("identityNumber", identityNumber);
        merchant.put("hospital", hospital);
        merchant.put("title", title);
        merchant.put("department", department);
        RequestBody idFrontBody = RequestBody.create(MediaType.parse("image/*"), idFront);
        RequestBody idEndBody = RequestBody.create(MediaType.parse("image/*"), idEnd);
        RequestBody qualifiedFrontBody = RequestBody.create(MediaType.parse("image/*"), qualifiedFront);
        RequestBody qualifiedEndBody = RequestBody.create(MediaType.parse("image/*"), qualifiedEnd);
        MultipartBody.Part[] file = new MultipartBody.Part[4];
        file[0] = MultipartBody.Part.createFormData("idFront", idFront.getName(), idFrontBody);
        file[1] = MultipartBody.Part.createFormData("idEnd", idEnd.getName(), idEndBody);
        file[2] = MultipartBody.Part.createFormData("qualifiedFront", qualifiedFront.getName(), qualifiedFrontBody);
        file[3] = MultipartBody.Part.createFormData("qualifiedEnd", qualifiedEnd.getName(), qualifiedEndBody);
        RetrofitManager.getApiUrlManager()
                       .qualifiyDoc(file, merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.QUALIFIY_DOC, listener));
    }

    public static void updateUserInfo(Context context, String doctorId, int fieldId, JsonObject json,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("fieldId", fieldId);
        merchant.put("json", json);
        RetrofitManager.getApiUrlManager()
                       .updateUserInfo(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.UPDATE_USER_INFO, listener));
    }

    public static void getTransferPatientToList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getTransferPatientToList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_PATIENTS_TO_LIST, listener));
    }

    public static void getTransferPatientFromList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getTransferPatientFromList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_PATIENTS_FROM_LIST, listener));
    }

    public static void getTransferByPatient(Context context, String doctorId, String patientId, int pageNo,
            int pageSize, int days, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        //传0 代表不限制时间
        merchant.put("days", days);
        RetrofitManager.getApiUrlManager()
                       .getTransferByPatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_TRANSFER_BY_PATIENT, listener));
    }

    public static void getPatientOrders(Context context, String doctorId, String patientId, int page, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("days", 0);
        params.put("pageNo", page);
        params.put("pageSize", pageSize);
        params.put("patientId", patientId);
        params.put("doctorId", doctorId);
        RetrofitManager.getApiUrlManager()
                       .getPatientOrders(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_PATIENT_ORDER_LIST, listener));
    }

    public static void getPatientLimitCaseList(Context context, String doctorId, String patientId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        RetrofitManager.getApiUrlManager()
                       .getPatientLimitCaseList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_PATIENT_LIMIT_CASE_LIST, listener));
    }

    public static void deletePatientCase(Context context, String patientId, int fieldId, String caseCreatorId,
            String caseOperatorId, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("patientId", patientId);
        merchant.put("fieldId", fieldId);
        merchant.put("caseCreatorId", caseCreatorId);
        merchant.put("caseOperatorId", caseOperatorId);
        RetrofitManager.getApiUrlManager()
                       .deletePatientCase(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.DELETE_PATIENT_CASE, listener));
    }

    public static void getDocInfo(Context context, String doctorId, final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDocInfo(doctorId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_DOC_INFO, listener));
    }

    public static void getPatientInfo(Context context, String patientId,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getPatientInfo(patientId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_PATIENT_INFO, listener));
    }

    public static void getTransferDetailById(Context context, int transferId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> merchant = new HashMap<>(16);
        merchant.put("transferId", transferId);
        RetrofitManager.getApiUrlManager()
                       .getTransferDetailById(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_TRANSFER_DETAIL_BY_ID, listener));
    }

    public static void cancelTransferPatient(Context context, int transferId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> merchant = new HashMap<>(16);
        merchant.put("transferId", transferId);
        RetrofitManager.getApiUrlManager()
                       .cancelTransferPatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.CANCEL_TRANSFER_PATIENT, listener));
    }

    public static void refuseTransferPatient(Context context, int transferId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> merchant = new HashMap<>(16);
        merchant.put("transferId", transferId);
        RetrofitManager.getApiUrlManager()
                       .refuseTransferPatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.REFUSE_TRANSFER_PATIENT, listener));
    }

    public static void recvTransferPatient(Context context, int transferId, String hospitalId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("transferId", transferId);
        merchant.put("hospitalId", hospitalId);
        RetrofitManager.getApiUrlManager()
                       .recvTransferPatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.RECV_TRANSFER_PATIENT, listener));
    }

    public static void addTransferPatient(Context context, String diagnosisInfo, PatientBean patientBean,
            CooperateDocBean cooperateDocBean, LoginSuccessBean loginSuccessBean,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(20);
        params.put("fromDoctorDiagnosisInfo", diagnosisInfo);
        params.put("fromDoctorDepartment", loginSuccessBean.getDepartment());
        params.put("fromDoctorHospitalName", loginSuccessBean.getHospital());
        params.put("fromDoctorId", loginSuccessBean.getDoctorId());
        params.put("fromDoctorImage", loginSuccessBean.getPortraitUrl());
        params.put("fromDoctorName", loginSuccessBean.getName());
        params.put("fromDoctorTitle", loginSuccessBean.getTitle());
        params.put("patientBirthdate", patientBean.getBirthDate());
        params.put("patientId", patientBean.getPatientId());
        params.put("patientIdentityNumber", patientBean.getIdentityNum());
        params.put("patientImage", patientBean.getPatientImgUrl());
        params.put("patientName", patientBean.getName());
        params.put("patientSex", patientBean.getSex());
        params.put("toDoctorDepartment", cooperateDocBean.getDepartment());
        params.put("toDoctorHospitalName", cooperateDocBean.getHospital());
        params.put("toDoctorId", cooperateDocBean.getDoctorId());
        params.put("toDoctorImage", cooperateDocBean.getPortraitUrl());
        params.put("toDoctorName", cooperateDocBean.getName());
        params.put("toDoctorTitle", cooperateDocBean.getTitle());
        RetrofitManager.getApiUrlManager()
                       .addTransferPatient(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.ADD_TRANSFER_PATIENT, listener));
    }

    public static void getHospitalListByDoctorId(Context context, String doctorId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("doctorId", doctorId);
        RetrofitManager.getApiUrlManager()
                       .getHospitalListByDoctorId(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_HOSPITAL_LIST_BY_DOCTORID, listener));
    }

    public static void getCooperateHospitalDoctorList(Context context, String hospitalId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("hospitalId", hospitalId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getCooperateHospitalDoctorList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractBaseObserver<>(context, Tasks.GET_COOPERATE_HOSPITAL_DOCTOR_LIST, listener));
    }

    public static void getHospitalProductListByHospitalId(Context context, String hospitalId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("hospitalId", hospitalId);
        RetrofitManager.getApiUrlManager()
                       .getHospitalProductListByHospitalId(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_HOSPITAL_PRODUCT_LIST_BY_HOSPITALID,
                                                             listener));
    }

    public static void addProductOrderNew(Context context, String diagnosisInfo, LoginSuccessBean loginSuccessBean,
            PatientBean patientBean, HospitalBean curHospital, HospitalProductBean curProduct,
            HospitalProductTypeBean curProductType, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(20);
        params.put("diagnosisInfo", diagnosisInfo);
        params.put("productTypeId", curProductType.getProductTypeId());
        params.put("productTypeName", curProductType.getProductTypeName());
        params.put("doctorId", loginSuccessBean.getDoctorId());
        params.put("doctorName", loginSuccessBean.getName());
        params.put("hospitalName", curHospital.getHospitalName());
        params.put("patientSex", patientBean.getSex());
        params.put("hospitalId", curHospital.getHospitalId());
        params.put("patientId", patientBean.getPatientId());
        params.put("patientName", patientBean.getName());
        params.put("patientBirthDate", patientBean.getBirthDate());
        params.put("productDescription", curProduct.getProductDescription());
        params.put("productId", curProduct.getProductId());
        params.put("productName", curProduct.getProductName());
        params.put("productPrice", curProduct.getProductPrice());
        params.put("productPriceUnit", curProduct.getProductPriceUnit());
        RetrofitManager.getApiUrlManager()
                       .addProductOrderNew(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.ADD_PRODUCT_ORDER_NEW, listener));
    }

    public static void addPatientByScanOrChangePatient(Context context, String doctorId, String fromDoctorId,
            String patientId, int requestSource, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("toDoctorId", doctorId);
        merchant.put("fromDoctorId", fromDoctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .addPatientByScanOrChangePatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT,
                                                             listener));
    }

    public static void deletePatient(Context context, String doctorId, String patientId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        RetrofitManager.getApiUrlManager()
                       .deletePatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.DELETE_PATIENT, listener));
    }

    public static void getCooperateHospitalList(Context context, String doctorId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        RetrofitManager.getApiUrlManager()
                       .getCooperateHospitalList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_COOPERATE_HOSPITAL_LIST, listener));
    }

    public static void getDetailById(Context context, String fieldId, final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("fieldId", fieldId);
        RetrofitManager.getApiUrlManager()
                       .getDetailById(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_CASE_DETAIL_BY_ID, listener));
    }

    public static void getPatientCombine(Context context, String patientId,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getPatientCombine(patientId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.GET_PATIENT_COMBINE, listener));
    }

    public static void addPatientCase(Context context, Map<String, Object> params,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .addPatientCase(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.ADD_PATIENT_CASE, listener));
    }

    public static void updatePatientCase(Context context, Map<String, Object> params,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .updatePatientCase(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.ADD_PATIENT_CASE, listener));
    }

    public static void modifyNickName(Context context, String doctorId, String colleborateDoctorId, String nickname,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("colleborateDoctorId", colleborateDoctorId);
        merchant.put("nickname", nickname);
        RetrofitManager.getApiUrlManager()
                       .modifyNickName(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.MODIFY_NICK_NAME, listener));
    }

    public static void modifyNickNameByPatient(Context context, String doctorId, String patientId, String nickname,
            String from, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("nickname", nickname);
        merchant.put("from", from);
        RetrofitManager.getApiUrlManager()
                       .modifyNickNameByPatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.MODIFY_NICK_NAME_BY_PATIENT, listener));
    }

    public static void addPatientByScan(Context context, String doctorId, String patientId, int requestSource,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .addPatientByScan(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT,
                                                             listener));
    }

    public static void refusePatientApply(Context context, String doctorId, String patientId, int requestSource,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .refusePatientApply(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.REFUSE_PATIENT_APPLY, listener));
    }

    public static void agreePatientApply(Context context, String doctorId, String patientId, int requestSource,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        //操作者id
        merchant.put("fromId", doctorId);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .agreePatientApply(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractBaseObserver<>(context, Tasks.AGREE_PATIENT_APPLY, listener));
    }
}

