package com.zyc.doctor.http.listener;

import com.zyc.doctor.data.Tasks;

/**
 * @author dundun
 * @date 2015/12/29
 */
public abstract class AbstractResponseAdapter<T> implements ResponseListener<T> {
    @Override
    public void onResponseError(Tasks task, Exception e) {
    }

    @Override
    public void onResponseCode(Tasks task, T response) {
    }

    @Override
    public void onResponseStart(Tasks task) {
    }

    @Override
    public void onResponseEnd(Tasks task) {
    }

    @Override
    public void onResponseCancel(Tasks task) {
    }
}
