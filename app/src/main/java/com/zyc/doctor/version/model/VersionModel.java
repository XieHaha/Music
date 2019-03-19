package com.zyc.doctor.version.model;

import android.content.Context;

import com.yanzhenjie.nohttp.download.DownloadListener;
import com.zyc.doctor.tools.FileTransferServer;
import com.zyc.doctor.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.Version;
import custom.frame.http.IRequest;
import custom.frame.http.Tasks;
import custom.frame.http.listener.ResponseListener;
import custom.frame.utils.DirHelper;

/**
 * Created by dundun on 16/6/6.
 */
public class VersionModel implements ResponseListener<BaseResponse>, VersionModelListener {
    private static final String TAG = "VersionModel";
    private Context context;
    private IRequest request;
    private NewestVersionCallBack callBack;
    private DownloadAPKCallBack downloadAPKCallBack;

    public VersionModel(Context context, IRequest request) {
        this.context = context;
        this.request = request;
    }

    @Override
    public void getNewestVersion(NewestVersionCallBack callBack) {
        this.callBack = callBack;
        //获取最新版本
        request.getNewVersion(this);
    }

    @Override
    public void downloadAPK(String url, DownloadAPKCallBack downloadAPKCallBack) {
        this.downloadAPKCallBack = downloadAPKCallBack;
        File file = new File(DirHelper.getPathFile() + "/YHT.apk");
        if (file.exists()) {
            if (!file.delete()) {
                LogUtils.e(TAG, "delete error");
            }
        }
    }

    /**
     * 下载最新的apk
     */
    public void downloadAPK(String url, DownloadListener downloadListener) {
        File file = new File(DirHelper.getPathFile() + "/YHT.apk");
        if (file.exists()) {
            if (!file.delete()) {
                LogUtils.e(TAG, "delete error");
            }
        }
        //        url = "http://gdown.baidu.com/data/wisegame/89eb17d6287ae627/weixin_1300.apk";
        FileTransferServer.getInstance(context).downloadFile(url, DirHelper.getPathFile(), "YHT" +
                ".apk", downloadListener);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        if (callBack != null) {
            ArrayList<Version> list = response.getData();
            if (list != null && list.size() > 0) {
                for (Version bean : list) {
                    if ("android".equals(bean.getDeviceSystem())) {
                        callBack.result(bean);
                    }
                }
            }
        }
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        switch (task) {
            case UPDATE_VERSION:
                if (callBack != null) {
                    callBack.error(e.getMessage());
                }
                break;
            case DOWNLOAD_FILE:
                if (downloadAPKCallBack != null) {
                    downloadAPKCallBack.downloadError(e.getMessage());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response) {
        if (callBack != null) {
            callBack.error(response.getMsg());
        }
    }

    @Override
    public void onResponseFile(Tasks task, File file) {
        if (downloadAPKCallBack != null) {
            downloadAPKCallBack.downEnd(file);
        }
    }

    @Override
    public void onResponseLoading(Tasks task, boolean isUpload, long total, long current) {
        if (downloadAPKCallBack != null) {
            downloadAPKCallBack.downloading(total, current);
        }
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
