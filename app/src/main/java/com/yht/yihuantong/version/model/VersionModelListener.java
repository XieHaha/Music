package com.yht.yihuantong.version.model;

import java.io.File;

import custom.frame.bean.Version;

/**
 * Created by dundun on 16/6/6.
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
