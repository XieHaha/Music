package com.zyc.doctor.http.retrofit;

import android.util.Log;

import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseNetConfig;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.http.listener.ResponseListener;
import com.zyc.doctor.utils.LogUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 数据返回统一处理
 *
 * @param <T>
 * @author dundun
 */
public abstract class AbstractDataObserver<T> implements Observer<BaseResponse<T>> {
    private static final String TAG = "OkHttp error:";
    private ResponseListener listener;
    private Tasks task;

    @Override
    public void onNext(BaseResponse<T> response) {
        // 基础数据 进行统一处理
        if (response.getCode() == BaseNetConfig.REQUEST_SUCCESS) {
            if (listener != null) {
                listener.onResponseSuccess(task, response);
            }
        }
        else {
            if (listener != null) {
                listener.onResponseCode(task, response);
            }
        }
    }

    public void setParams(Tasks task, ResponseListener listener) {
        this.task = task;
        this.listener = listener;
    }

    @Override
    public void onError(Throwable e) {
        //服务器错误信息处理
        if (listener != null) {
            listener.onResponseError(task, new Exception("网络繁忙，请稍后再试"));
            LogUtils.e(TAG, task + "   onError:" + e);
            if (e.getStackTrace() != null) {
                for (StackTraceElement element : e.getStackTrace()) {
                    Log.e(TAG, "onError element:" + element.toString());
                }
            }
        }
    }

    @Override
    public void onComplete() {
        if (listener != null) {
            listener.onResponseEnd(task);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
    }
}
