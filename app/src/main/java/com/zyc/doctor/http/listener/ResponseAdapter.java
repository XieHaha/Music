package com.zyc.doctor.http.listener;


import java.io.File;

import com.zyc.doctor.http.Tasks;

/**
 * Created by luozi on 2015/12/29.
 */
public abstract class ResponseAdapter<T> implements ResponseListener<T> {

    @Override
    public void onResponseError(Tasks task, Exception e) {

    }

    @Override
    public void onResponseCodeError(Tasks task, T response) {

    }

    @Override
    public void onResponseStart(Tasks task) {
    }

    @Override
    public void onResponseEnd(Tasks task) {
    }

    @Override
    public void onResponseLoading(Tasks task, boolean isUpload, long total, long current) {

    }

    @Override
    public void onResponseFile(Tasks task, File file) {

    }

    @Override
    public void onResponseCancel(Tasks task) {
    }
}
