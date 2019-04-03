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

    public static void cancelCooperateDoc(Context context, String doctorId, String doctorId2,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("id1", doctorId);
        merchant.put("id1", doctorId2);
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
}

