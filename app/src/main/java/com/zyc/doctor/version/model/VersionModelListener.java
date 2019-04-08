package com.zyc.doctor.version.model;

import com.zyc.doctor.http.bean.Version;

import java.io.File;

/**
 * @author dundun
 * @date 16/6/6
 */
public interface VersionModelListener {
    void getNewestVersion(NewestVersionCallBack callBack);

    void downloadAPK(String url, DownloadAPKCallBack downloadAPKCallBack);

    interface NewestVersionCallBack {
        void result(Version version);

        void error(String s);
    }

    interface DownloadAPKCallBack {
        void downEnd(File file);

        void downloading(long total, long currnetData);

        void downloadError(String s);
    }
}
