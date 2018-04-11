package custom.frame.http;

import android.content.Context;

import com.android.volley.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.LoginSuccessBean;
import custom.frame.http.listener.ResponseListener;

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
        RequestParams params = new RequestParams();
        params.addBodyParameter("file", file);
        params.addBodyParameter("type", type);
//        Map<String, Object> merchant = new HashMap<>(16);
//        merchant.put("file", file);
//        merchant.put("type", type);
        return uploadFile("/f/uploadfile", Tasks.UPLOAD_FILE, String.class, params, null, listener);
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
}
