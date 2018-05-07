package com.yht.yihuantong.tools;

import android.content.Context;

import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OnUploadListener;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.JsonObjectRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import java.io.File;

/**
 * 文件传输服务：上传、下载
 */
public class FileTransferServer
{
    private static FileTransferServer sInstance;
    private static Context sContext;
    /**
     * 连接服务器超时
     */
    private static final int CONNET_TIME_OUT = 15 * 1000;
    /**
     * 服务器响应超时
     */
    private static final int READ_TIME_OUT = 15 * 1000;
    /**
     * 并发数
     */
    private static final int THREAD_POOL_SIZE = 3;
    private static RequestQueue uploadrQueue;
    private static DownloadQueue downloadQueue;

    public synchronized static FileTransferServer getInstance(Context context)
    {
        if (null == sInstance)
        {
            sInstance = new FileTransferServer(context);
        }
        return sInstance;
    }

    private FileTransferServer(Context context)
    {
        sContext = context.getApplicationContext();
        uploadrQueue = NoHttp.newRequestQueue(THREAD_POOL_SIZE);
        downloadQueue = NoHttp.newDownloadQueue(THREAD_POOL_SIZE);
        uploadrQueue.start();
        downloadQueue.start();
    }

    public void uploadFile(String jid, String filePath, String fileType, OnResponseListener responseListener,
            OnUploadListener uploadListener)
    {
        uploadFile(0, jid, filePath, fileType, responseListener, uploadListener);
    }

    public void uploadFile(int what, String jid, String filePath, String fileType, OnResponseListener responseListener,
            OnUploadListener uploadListener)
    {
        String url = "";
        Request request = new JsonObjectRequest(url, RequestMethod.POST);
        //todo tag
        request.setTag(filePath);
        request.set("jid", jid);
        request.set("fileType", fileType);
        FileBinary fileBinary = new FileBinary(new File(filePath));
        fileBinary.setUploadListener(what, uploadListener);
        request.set("file", fileBinary);
//        ImHttpServer.getInstance(ApiManager.getInstance().getContext()).addHeader(request);
        uploadrQueue.add(what, request, responseListener);
    }

    public void downloadFile(String url, String savePath, String fileName, DownloadListener downloadListener)
    {
        downloadFile(0, url, savePath, fileName, downloadListener);
    }

    public void downloadFile(int what, String url, String savePath, String fileName, DownloadListener downloadListener)
    {
        DownloadRequest request = NoHttp.createDownloadRequest(url, savePath, fileName, false, true);
//        ImHttpServer.getInstance(ApiManager.getInstance().getContext()).addHeader(request);
        downloadQueue.add(what, request, downloadListener);
    }

    public void cancelUpload(Object sign)
    {
        uploadrQueue.cancelBySign(sign);
    }

    public void cancelAllUpload()
    {
        uploadrQueue.cancelAll();
    }

    public void cancelDownload(Object sign)
    {
        downloadQueue.cancelBySign(sign);
    }

    public void cancelAllDownload()
    {
        downloadQueue.cancelAll();
    }

    public void cancelAll()
    {
        cancelAllUpload();
        cancelAllDownload();
    }

    public void close()
    {
        cancelAll();
        uploadrQueue.stop();
        downloadQueue.stop();
        uploadrQueue = null;
        downloadQueue = null;
        sContext = null;
        sInstance = null;
    }

    public interface UploadFileType
    {
        /**
         * 图片
         */
        String TYPE_IMAG = "0";
        /**
         * 视频
         */
        String TYPE_VIDEO = "1";
        /**
         * 其他
         */
        String TYPE_OTHER = "2";
    }
}
