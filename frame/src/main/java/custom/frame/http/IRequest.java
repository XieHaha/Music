package custom.frame.http;

import android.content.Context;

import com.android.volley.RequestParams;

import java.io.File;

import custom.frame.bean.BaseResponse;
import custom.frame.http.listener.ResponseListener;

import static custom.frame.http.data.HttpConstants.Method.GET;
import static custom.frame.http.data.HttpConstants.Method.POST;

/**
 * Created by luozi on 2015/12/30.
 * baseRequest include requestString and requestObject and requestList
 */
public class IRequest extends BaseRequest
{
    /**
     * 单例模式
     */
    private volatile static IRequest instance = null;
    /**
     * 公共模块
     */
    private String PUBLIC = "pub";
    private String F = "f";

    /**
     * 单例模式
     */
    public static IRequest getInstance(Context context)
    {
        synchronized (IRequest.class)
        {
            if (instance == null)
            {
            }
            instance = new IRequest(context);
            return instance;
        }
    }

    /**
     * 一个新的单例
     */
    public static IRequest newInstance(Context context)
    {
        return new IRequest(context);
    }

    /**
     * 父类构造函数
     */
    private IRequest(Context context)
    {
        super(context);
    }

    /**
     * 得到基础连接地址
     */
    private RequestParams getBaseMap(String requestName)
    {
        RequestParams params = new RequestParams();
        params.addBodyParameter("m", "app_server");
        params.addBodyParameter("c", "api");
        params.addBodyParameter("a", requestName);
        return params;
    }

    /**
     * 获取验证码
     */
    public Tasks getVerifyCode(String phoneNumber, final ResponseListener<BaseResponse> listener)
    {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phoneNumber);
        return requestBaseResponse(GET, PUBLIC, "sendVerifyCode", Tasks.GET_VERIFY_CODE,
                                   String.class, params, listener);
    }

    /**
     * 上传头像
     */
    public Tasks uploadHeadImg(File filePath, String type,
            final ResponseListener<BaseResponse> listener)
    {
        RequestParams params = new RequestParams();
        params.addBodyParameter("file", filePath);
        params.addBodyParameter("type", type);
        return uploadFile(POST, F, "uploadfile", Tasks.UPLOAD_FILE, String.class, params, listener);
    }
}
