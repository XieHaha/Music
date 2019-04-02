package com.zyc.doctor.version.model;

import android.content.Context;

import com.yanzhenjie.nohttp.download.DownloadListener;
import com.zyc.doctor.http.IRequest;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.http.bean.BaseResponse;
import com.zyc.doctor.http.bean.Version;
import com.zyc.doctor.http.listener.ResponseListener;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.utils.DirHelper;
import com.zyc.doctor.utils.FileTransferServer;
import com.zyc.doctor.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * @author dundun
 * @date 16/6/6
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
        RequestUtils.getNewVersion(context, this);
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
        FileTransferServer.getInstance(context)
                          .downloadFile(url, DirHelper.getPathFile(), "YHT" + ".apk", downloadListener);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        if (callBack != null) {
            ArrayList<Version> list = (ArrayList<Version>)response.getData();
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
    public void onResponseCode(Tasks task, BaseResponse response) {
        if (callBack != null) {
            callBack.error(response.getMsg());
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
