package com.zyc.doctor.http.retrofit;

import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.http.bean.BaseNetConfig;
import com.zyc.doctor.http.bean.BaseResponse;
import com.zyc.doctor.http.listener.ResponseListener;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 数据返回统一处理
 *
 * @param <T>
 * @author dundun
 */
public abstract class AbstractDataObserver<T> implements Observer<BaseResponse<T>> {
    private ResponseListener listener;
    private Tasks task;

    @Override
    public void onNext(BaseResponse<T> response) {
        //在这边对 基础数据 进行统一处理
        if (response.getCode() == BaseNetConfig.REQUEST_SUCCESS) {
            if (listener != null) {
                listener.onResponseSuccess(task, response);
            }
        }
        else if (response.getCode() == BaseNetConfig.CODE_MODIFY_CASE_RECORD) {
        }
        else {
        }
    }

    public void setParams(Tasks task, ResponseListener listener) {
        this.task = task;
        this.listener = listener;
    }

    @Override
    public void onError(Throwable e) {//服务器错误信息处理
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
