package custom.frame.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestParams;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.Global;
import custom.frame.http.listener.ResponseListener;
import custom.frame.log.MLog;

/**
 * baseRequest include requestString and requestBaseResponse and requestList
 */
public class BaseRequest<T> extends HttpProxy {
    private RequestQueue mQueue = null;

    public BaseRequest(Context context) {
        super(context);
        mQueue = Volley.newRequestQueue(context);
    }

    /**
     * request String 返回的是string
     *
     * @param metoh    请求方式
     * @param task     任务队列id
     * @param params   请求参数
     * @param listener 请求回调
     * @param headers  请求头
     */
    public final Tasks requestString(final Method metoh, String moduleName, String metohName,
                                     final Tasks task, final RequestParams params, final Map<String, String> headers,
                                     final ResponseListener<String> listener) {
        StringRequest stringRequest;
        if (listener != null) {
            listener.onResponseStart(task);
        }
        StringBuilder url = new StringBuilder(appendUrl(moduleName, metohName));
        int method = Request.Method.POST;
        switch (metoh) {
            case GET:
                method = Request.Method.GET;
                if (params != null) {
                    //参数url
                    url.append(MapToGetUrl(params.getStringsParams()));
                }
                break;
            case POST:
                method = Request.Method.POST;
                break;
            case PUT:
                method = Request.Method.PUT;
                break;
            case DELETE:
                method = Request.Method.DELETE;
                if (params != null) {
                    //参数url
                    url.append(MapToGetUrl(params.getStringsParams()));
                }
                break;
            default:
                break;
        }
        //生成请求对象
        stringRequest = new StringRequest(method, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //打印响应日志
                printfReponseLog(task, response);
                if (listener != null) {
                    listener.onResponseSuccess(task, response);
                    listener.onResponseEnd(task);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //打印错误日志
                printfErrorLog(task, error.getMessage());
                if (listener != null) {
                    listener.onResponseError(task, error);
                    listener.onResponseEnd(task);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>(16);
                if (headers != null) {
                    hashMap.putAll(headers);
                }
                return hashMap;
            }
        };
        /**复写重试方针*/
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeoutMS, retries, backoffMultiplier));
        stringRequest.setTag(task);
        mQueue.add(stringRequest);
        return task;
    }

    /**
     * requestBaseResponse 以baseResponse结构的json解析字符串
     *
     * @param metoh    请求方式
     * @param task     任务队列id
     * @param params   请求参数
     * @param classOfT 需转换的类型
     * @param listener 请求回调
     * @param headers  请求头
     */
    public final Tasks requestBaseResponse(final Method metoh, String moduleName, String metohName,
                                           final Tasks task, final Class<T> classOfT, final RequestParams params,
                                           final Map<String, String> headers, final ResponseListener<BaseResponse> listener) {
        StringRequest objectRequest;
        if (listener != null) {
            listener.onResponseStart(task);
        }
        StringBuilder url = new StringBuilder(appendUrl(moduleName, metohName));
        int method = Request.Method.POST;
        switch (metoh) {
            case GET:
                method = Request.Method.GET;
                if (params != null) {
                    //参数url
                    url.append(MapToGetUrl(params.getStringsParams()));
                }
                break;
            case POST:
                method = Request.Method.POST;
                break;
            case PUT:
                method = Request.Method.PUT;
                break;
            case DELETE:
                method = Request.Method.DELETE;
                if (params != null) {
                    //参数url
                    url.append(MapToGetUrl(params.getStringsParams()));
                }
                break;
            default:
                break;
        }
        objectRequest = new StringRequest(method, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    BaseResponse baseResponse = praseBaseResponse(jsonObject, classOfT);
                    //打印响应日志
                    printfReponseLog(task, baseResponse);
                    /**
                     * 如果请求码成功
                     * */
                    if (REQUEST_SUCCESS == baseResponse.getCode()) {
                        if (listener != null) {
                            listener.onResponseSuccess(task, baseResponse);
                            listener.onResponseEnd(task);
                        }
                    } else {
                        /**
                         * 调用请求码异常
                         * */
                        if (listener != null) {
                            listener.onResponseCodeError(task, baseResponse);
                            listener.onResponseEnd(task);
                        }
                    }
                } catch (JSONException je) {
                    //打印错误日志
                    printfErrorLog(task, je.getMessage());
                    if (listener != null) {
                        listener.onResponseError(task, je);
                        listener.onResponseEnd(task);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //打印错误日志
                printfErrorLog(task, error.getMessage());
                if (listener != null) {
                    listener.onResponseError(task, new Exception("网络繁忙，请稍后再试"));
                    listener.onResponseEnd(task);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>(16);
                if (headers != null) {
                    hashMap.putAll(headers);
                }
                hashMap.put("Accept", "application/json");
                hashMap.put("Content-Type", "application/json; charset=UTF-8");
                return hashMap;
            }
        };
        /**复写重试方针*/
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(timeoutMS, retries, backoffMultiplier));

        objectRequest.setTag(task);
        mQueue.add(objectRequest);
        return task;
    }

    /**
     * requestBaseResponse 以baseResponse结构的json解析字符串
     *
     * @param task     任务队列id
     * @param merchant 请求参数
     * @param classOfT 需转换的类型
     * @param listener 请求回调
     */
    public final Tasks requestBaseResponseByJson(String moduleName, final Tasks task, final Class<T> classOfT, final Map<String, Object> merchant,
                                                 final ResponseListener<BaseResponse> listener) {

        JsonRequest<JSONObject> jsonRequest;
        if (listener != null) {
            listener.onResponseStart(task);
        }
        int method = Request.Method.POST;
        final JSONObject jsonObject = new JSONObject(merchant);
        Log.i("test", "params:" + jsonObject.toString());
        String url = appendUrl(moduleName);
        jsonRequest = new JsonObjectRequest(method, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    BaseResponse baseResponse = praseBaseResponse(response, classOfT);
                    /**
                     * 如果请求码成功
                     * */
                    if (REQUEST_SUCCESS == baseResponse.getCode()) {
                        if (listener != null) {
                            listener.onResponseSuccess(task, baseResponse);
                            listener.onResponseEnd(task);
                        }
                    } else {
                        /**
                         * 调用请求码异常
                         * */
                        if (listener != null) {
                            listener.onResponseCodeError(task, baseResponse);
                            listener.onResponseEnd(task);
                        }
                    }
                } catch (JSONException je) {
                    if (listener != null) {
                        listener.onResponseError(task, je);
                        listener.onResponseEnd(task);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    listener.onResponseError(task, new Exception("网络繁忙，请稍后再试"));
                    listener.onResponseEnd(task);
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>(16);
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");

                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return jsonObject.toString().getBytes("UTF-8");

                } catch (Exception e) {
                }
                return null;
            }
        };
        /**复写重试方针*/
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(timeoutMS, retries, backoffMultiplier));
        //设置tag
        jsonRequest.setTag(task);
        //加入请求队列
        mQueue.add(jsonRequest);
        return task;
    }

    /**
     * requestBaseResponseList 以baseResponse结构的json解析字符串
     *
     * @param metoh    请求方式
     * @param task     任务队列id
     * @param params   请求参数
     * @param classOfT 需转换的类型
     * @param listener 请求回调
     * @param headers  请求头
     */
    public final Tasks requestBaseResponseList(final Method metoh, String moduleName,
                                               String metohName, final Tasks task, final Class<T> classOfT, final RequestParams params,
                                               final Map<String, String> headers, final ResponseListener<BaseResponse> listener) {
        StringRequest objectRequest;
        if (listener != null) {
            listener.onResponseStart(task);
        }
        StringBuilder url = new StringBuilder(appendUrl(moduleName, metohName));
        int method = Request.Method.POST;
        switch (metoh) {
            case GET:
                method = Request.Method.GET;
                if (params != null) {
                    //参数url
                    url.append(MapToGetUrl(params.getStringsParams()));
                }
                break;
            case POST:
                method = Request.Method.POST;
                break;
            case PUT:
                method = Request.Method.PUT;
                break;
            case DELETE:
                method = Request.Method.DELETE;
                if (params != null) {
                    //参数url
                    url.append(MapToGetUrl(params.getStringsParams()));
                }
                break;
            default:
                break;
        }
        objectRequest = new StringRequest(method, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    BaseResponse baseResponse = praseBaseResponseList(jsonObject, classOfT);
                    printfReponseLog(task, baseResponse);//打印响应日志
                    /**
                     * 如果请求码成功
                     * */
                    if (REQUEST_SUCCESS == baseResponse.getCode()) {
                        if (listener != null) {
                            listener.onResponseSuccess(task, baseResponse);
                            listener.onResponseEnd(task);
                        }
                    } else {
                        /**
                         * 调用请求码异常
                         * */
                        if (listener != null) {
                            listener.onResponseCodeError(task, baseResponse);
                            listener.onResponseEnd(task);
                        }
                    }
                } catch (JSONException je) {
                    printfErrorLog(task, je.getMessage());//打印错误日志
                    if (listener != null) {
                        listener.onResponseError(task, je);
                        listener.onResponseEnd(task);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printfErrorLog(task, error.getMessage());//打印错误日志
                if (listener != null) {
                    listener.onResponseError(task, new Exception("网络繁忙，请稍后再试"));
                    listener.onResponseEnd(task);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                if (headers != null) {
                    hashMap.putAll(headers);
                }
                return hashMap;
            }
        };
        /**复写重试方针*/
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(timeoutMS, retries, backoffMultiplier));
        if (metoh != Method.GET && metoh != Method.DELETE && params != null) {
//            objectRequest.setRequestParams(params);
        }
        objectRequest.setTag(task);
        mQueue.add(objectRequest);
        return task;
    }


    /**
     * uploadFile 以baseResponse结构的json解析字符串
     *
     * @param task     任务队列id
     * @param params   携带参数
     * @param classOfT 上传返回实体类
     * @param listener 请求回调
     * @param headers  请求头
     */
    protected final Tasks uploadFile(String moduleName, final Tasks task,
                                     final Class<T> classOfT, final RequestParams params, final Map<String, String> headers,
                                     final ResponseListener<BaseResponse> listener) {
        StringRequest objectRequest;
        if (listener != null) {
            listener.onResponseStart(task);
        }
        String url = appendUrl(moduleName);
        int method = Request.Method.POST;

        objectRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    BaseResponse baseResponse = praseBaseResponse(jsonObject, classOfT);
                    /**
                     * 如果请求码成功
                     * */
                    if (REQUEST_SUCCESS == baseResponse.getCode()) {
                        if (listener != null) {
                            listener.onResponseSuccess(task, baseResponse);
                            listener.onResponseEnd(task);
                        }
                    } else {
                        /**
                         * 调用请求码异常
                         * */
                        if (listener != null) {
                            listener.onResponseCodeError(task, baseResponse);
                            listener.onResponseEnd(task);
                        }
                    }
                } catch (JSONException je) {
                    if (listener != null) {
                        listener.onResponseError(task, je);
                        listener.onResponseEnd(task);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    listener.onResponseError(task, new Exception("网络繁忙，请稍后再试"));
                    listener.onResponseEnd(task);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                if (headers != null) {

                    hashMap.putAll(headers);
                }
                return hashMap;
            }
        };

        /**复写重试方针*/
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(timeoutMS, retries, backoffMultiplier));
        objectRequest.setRequestParams(params);

        objectRequest.setTag(task);
        mQueue.add(objectRequest);

        return task;
    }

    /**
     * 根据任务id取消网络任务,传入响应监听和任务监听，与activity的生命周期相关联
     *
     * @param task     任务编号
     * @param listener 响应监听，为处理任务的生命周期
     */
    public final void cancel(Tasks task, final ResponseListener<BaseResponse> listener) {
        if (mQueue != null) {
            mQueue.cancelAll(task);
        }
        if (listener != null) {
            listener.onResponseCancel(task);
        }
        printfCancelLog(task);
    }

    //=============================================请求辅助方法==============================
    public String EntityData = "data";
    public String EntityCode = "code";
    public String EntityMsg = "msg";

    /**
     * 把json转换成基础响应对象类
     *
     * @param jsonObject jsonobject对象
     * @param classOfT   待转换的实体类,为空则data为空
     */
    public final BaseResponse praseBaseResponse(JSONObject jsonObject, Class<T> classOfT)
            throws JSONException {
        Object data = null;
        if (classOfT != null) {
            if (classOfT == String.class) {
                data = jsonObject.optString(EntityData);
            } else {
                if (jsonObject.opt(EntityData) != null) {
                    try {
                        data = JSON.parseObject(jsonObject.optString(EntityData), classOfT);
                    } catch (Exception e) {
                    }
                }
            }
        }
        BaseResponse baseResponse = new BaseResponse().setCode(jsonObject.optInt(
                EntityCode))
                .setMsg(jsonObject.optString(EntityMsg))
                .setData(data);
        return baseResponse;
    }

    /**
     * 把json转换成基础响应对象列表类
     */
    public final BaseResponse praseBaseResponseList(JSONObject jsonObject, Class<T> classOfT)
            throws JSONException {
        BaseResponse baseResponse = new BaseResponse().setCode(jsonObject.optInt(
                EntityCode))
                .setMsg(jsonObject.optString(EntityMsg));
        List<T> list = new ArrayList<>();
        if (jsonObject.opt(EntityData) != null) {
            JSONArray jsonArray = jsonObject.optJSONArray(EntityData);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        list.add(JSON.parseObject(jsonArray.get(i).toString(), classOfT));
                    } catch (Exception e) {
                    }
                }
            }
        }
        baseResponse.setData(list);
        return baseResponse;
    }

    /**
     * 打印request日志
     *
     * @param task 任务名
     */
    private final void printfRequestLog(Tasks task, String url) {
        if (url != null) {
            MLog.d(Global.getInstance().getAPP_DEBUG_HEADER() + "#" + HttpTag,
                    task + " Request url:" + getAPPUrl() + " url = " + url.toString());
        } else {
            MLog.d(Global.getInstance().getAPP_DEBUG_HEADER() + "#" + HttpTag,
                    " Request url:" + getAPPUrl());
        }
    }

    /**
     * 打印reponse success日志
     * <p>
     *
     * @param task 任务名
     * @param o    响应结果
     */
    private final void printfReponseLog(Tasks task, Object o) {
        MLog.d(Global.getInstance().getAPP_DEBUG_HEADER() + "#" + HttpTag,
                task + " Reponse Success = " + (o == null ? NULLTag : o.toString()));
    }

    /**
     * 打印reponse error日志
     *
     * @param task 任务名
     * @param o    响应结果
     */
    private final void printfErrorLog(Tasks task, Object o) {
        MLog.e(Global.getInstance().getAPP_DEBUG_HEADER() + "#" + HttpTag,
                task + " Reponse Error = " + (o == null ? NULLTag : o.toString()));
    }

    /**
     * 打印reponse success日志
     *
     * @param task 任务名
     */
    private final void printfCancelLog(Tasks task) {
        MLog.d(Global.getInstance().getAPP_DEBUG_HEADER() + "#" + HttpTag,
                "Reponse Cancel = " + (task == null ? NULLTag : task));
    }

    /**
     * Map列表转换为url地址
     *
     * @return 转换后的结果String
     */
    private final static String MapToGetUrl(Map<String, RequestParams.StringContent> map) {
        StringBuilder url = new StringBuilder("");
        if (map != null) {
            for (int i = 0; i < map.size(); i++) {
                url.append((map.keySet().toArray()[i].toString()) + "=" +
                        ((RequestParams.StringContent) map.values().toArray()[i]).getValue());
                if (i != (map.size() - 1)) {
                    url.append("&");
                }
            }
        }
        return url.toString();
    }

    /**
     * Map<String, Object> 泛型转换为 Map<String, String>
     *
     * @param map
     */
    private final static Map<String, String> ObjectToStringMap(Map<String, Object> map) {
        if (map != null) {
            Map<String, String> tmp = new HashMap<>();
            for (int i = 0; i < map.size(); i++) {
                tmp.put(map.keySet().toArray()[i].toString(), map.values().toArray()[i].toString());
            }
            return tmp;
        }
        return null;
    }

    /**
     * 连接方法名
     */
    private final String appendUrl(String moduleName, String metohName) {
        StringBuilder url = new StringBuilder(getAPPUrl() + (moduleName == null ? "" : moduleName) +
                (TextUtils.isEmpty(moduleName) ? "" : "/") +
                metohName + "?");
        return url.toString();
    }

    /**
     * 连接方法名
     */
    private final String appendUrl(String moduleName) {
        StringBuilder url = new StringBuilder(getAPPUrl() + (moduleName == null ? "" : moduleName));
        return url.toString();
    }
}
