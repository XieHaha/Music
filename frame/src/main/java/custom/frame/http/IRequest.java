package custom.frame.http;

import android.content.Context;

import com.android.volley.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.LoginSuccessBean;
import custom.frame.http.listener.ResponseListener;

import static custom.frame.http.data.HttpConstants.Method.POST;

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
    public Tasks uploadHeadImg(File filePath, String type,
                               final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("file", filePath);
        params.addBodyParameter("type", type);
        return uploadFile(POST, "/f/uploadfile", Tasks.UPLOAD_FILE, String.class, params, listener);
    }
}
