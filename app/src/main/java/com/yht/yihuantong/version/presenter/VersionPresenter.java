package com.yht.yihuantong.version.presenter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.utils.LogUtils;
import com.yht.yihuantong.utils.NetWorkUtils;
import com.yht.yihuantong.version.ConstantsVersionMode;
import com.yht.yihuantong.version.model.VersionModel;
import com.yht.yihuantong.version.model.VersionModelListener;

import java.io.File;

import custom.frame.bean.Version;
import custom.frame.http.IRequest;
import custom.frame.utils.DirHelper;
import custom.frame.utils.ToastUtil;

/**
 * @author dundun
 */
public class VersionPresenter implements ConstantsVersionMode
{
    private static final String TAG = "VersionPresenter";
    private Context context;
    private IRequest request;
    private VersionModel versionModel;
    private Version nowVersion;
    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private PendingIntent pendingIntent;
    private String url = null;
    /**
     * 文件大小
     */
    private long fileSize;
    //    private boolean isLoading = false;//是否正在下载

    public VersionPresenter(Context context)
    {
        this.context = context;
    }

    public VersionPresenter(Context context, IRequest request)
    {
        this.context = context;
        this.request = request;
    }

    public void init()
    {
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setWhen(System.currentTimeMillis())
               .setPriority(Notification.PRIORITY_DEFAULT)
               .setOngoing(false)
               .setSmallIcon(R.mipmap.ic_launcher);
        pendingIntent = PendingIntent.getActivity(context, UPDATE_VERSION_RESULT, new Intent(),
                                                  PendingIntent.FLAG_UPDATE_CURRENT);
        versionModel = new VersionModel(context, request);
        updateVersionByNetwork();
    }

    /**
     * 根据网络情况判断是否检查更新
     * 断网时不检查更新，启动网络监听广播
     */
    public void updateVersionByNetwork()
    {
        if (new NetWorkUtils(context).isConnected())
        {
            versionModel.getNewestVersion(new VersionModelListener.NewestVersionCallBack()
            {
                @Override
                public void result(Version version)
                {
                    if (version == null) { return; }
                    nowVersion = version;
                    url = nowVersion.getDownloadUrl();
                    checkVersion();
                }

                @Override
                public void error(String s)
                {
                    if (!TextUtils.isEmpty(s)) { ToastUtil.toast(context, s); }
                }
            });
        }
        else
        {
            if (versionViewListener != null)
            {
                versionViewListener.updateNetWorkError();
            }
        }
    }

    /**
     * 下载apk
     *
     * @param isMustUpdate 判断是否是强制更新
     */
    public void getNewAPK(final boolean isMustUpdate)
    {
        if (versionModel != null)
        {
            //            versionModel.downloadAPK(url, new VersionModelListener.DownloadAPKCallBack() {
            //                @Override
            //                public void downEnd(final File file) {
            //                    //TODO 下载结束 适配7.0
            //                }
            //
            //                @Override
            //                public void downloading(long total, long currnetData) {
            //                    if (isMustUpdate && versionViewListener != null) {
            //                        versionViewListener.updateLoading(total, currnetData);
            //                    } else {
            //                        showCustomProgressNotify((int) total, (int) currnetData);
            //                    }
            //                }
            //
            //                @Override
            //                public void downloadError(String s) {
            //                    ToastUtil.toast(context, s);
            //                }
            //            });
            versionModel.downloadAPK(url, new DownloadListener()
            {
                @Override
                public void onDownloadError(int what, Exception exception)
                {
                    ToastUtil.toast(context, exception.getMessage());
                }

                @Override
                public void onStart(int what, boolean isResume, long rangeSize,
                        Headers responseHeaders, long allCount)
                {
                    fileSize = allCount;
                }

                @Override
                public void onProgress(int what, int progress, long fileCount, long speed)
                {
                    if (isMustUpdate && versionViewListener != null)
                    {
                        versionViewListener.updateLoading(fileSize, fileCount);
                    }
                    else
                    {
                        showCustomProgressNotify((int)fileSize, (int)fileCount);
                    }
                }

                @Override
                public void onFinish(int what, String filePath)
                {
                    File file = new File(filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    {
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        uri = FileProvider.getUriForFile(context, YihtApplication.getInstance().getPackageName() + ".fileprovider",
                                                         file);
                    }
                    else
                    {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    context.startActivity(intent);
                }

                @Override
                public void onCancel(int what)
                {
                }
            });
        }
    }

    /**
     * 显示自定义带进度条通知栏
     */
    private void showCustomProgressNotify(int total, int currnetData)
    {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                                                  R.layout.view_notifycation_content);
        if (currnetData == total)
        {
            remoteViews.setViewVisibility(R.id.custom_progressbar, View.GONE);
            remoteViews.setTextViewText(R.id.view_notifycation_content_txt, "已完成");
            remoteViews.setViewVisibility(R.id.view_notifycation_content_percent, View.GONE);
            manager.cancel(UPDATE_VERSION_RESULT);
        }
        else
        {
            remoteViews.setViewVisibility(R.id.custom_progressbar, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.view_notifycation_content_percent, View.VISIBLE);
            remoteViews.setTextViewText(R.id.view_notifycation_content_percent,
                                        (int)(currnetData / (float)total * 100) + "%");
            remoteViews.setProgressBar(R.id.custom_progressbar, total, currnetData, false);
        }
        builder.setContent(remoteViews)
               .setContentIntent(pendingIntent)
               .setWhen(System.currentTimeMillis());
        manager.notify(100, builder.build());
    }

    /**
     * 版本检测更新
     */
    private void checkVersion()
    {
        String currentVersionName = getVersionName();//当前正在使用的版本
        String newestVersionName = nowVersion.getNewVersion();//获取最新版本
        String lowestVersionName = nowVersion.getMinVersion();//获取最低支持版本
        int mode = UPDATE_NONE;
        boolean isDownAPK = true;
        //小于0  表示获取的新版本号大于当前使用的版本,需要更新
        if (compareVersion(currentVersionName, newestVersionName) < 0)
        {
            //小于0 表示强制更新
            if (compareVersion(currentVersionName, lowestVersionName) < 0)
            {
                mode = UPDATE_MUST;
            }
            else
            {//大于等于0 用户选择更新
                mode = UPDATE_CHOICE;
            }
            //判断本地apk文件的版本号是否最新
            String apkVersionName = getApkVersion(DirHelper.getPathFile() + "/YHT.apk");
            if (apkVersionName != null && compareVersion(newestVersionName, apkVersionName) == 0)
            {
                isDownAPK = false;
            }
        }
        if (versionViewListener != null)
        {
            versionViewListener.updateVersion(nowVersion, mode, isDownAPK);
        }
    }

    private VersionViewListener versionViewListener;

    public interface VersionViewListener
    {
        void updateVersion(Version version, int mode, boolean isDownLoading);

        void updateLoading(long total, long current);

        void updateNetWorkError();
    }

    public void setVersionViewListener(VersionViewListener versionViewListener)
    {
        this.versionViewListener = versionViewListener;
    }

    /**
     * 获取当前应用的版本号
     */
    public String getVersionName()
    {
        try
        {
            return context.getPackageManager()
                          .getPackageInfo(context.getPackageName(), 0).versionName + "";
        }
        catch (NameNotFoundException e)
        {
            LogUtils.w(TAG, "Exception error!", e);
        }
        return null;
    }

    /**
     * 获取apk包的信息：版本号
     *
     * @param absPath apk包的绝对路径
     */
    public String getApkVersion(String absPath)
    {
        if (new File(absPath).exists())
        {
            PackageManager pm = context.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
            if (pkgInfo != null)
            {
                ApplicationInfo appInfo = pkgInfo.applicationInfo;
                /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
                appInfo.sourceDir = absPath;
                appInfo.publicSourceDir = absPath;
                String version = pkgInfo.versionName; // 得到版本信息
                return version;
            }
        }
        return null;
    }

    /**
     * 版本号对比工具
     * 将s1 对比 s2 如果s1大则大于0
     *
     * @param s1
     * @param s2
     */
    private int compareVersion(String s1, String s2)
    {
        if (s1 == null && s2 == null) { return 0; }
        else if (s1 == null) { return -1; }
        else if (s2 == null) { return 1; }
        String[] arr1 = s1.split("[^a-zA-Z0-9]+"), arr2 = s2.split("[^a-zA-Z0-9]+");
        int i1, i2, i3;
        for (int ii = 0, max = Math.min(arr1.length, arr2.length); ii <= max; ii++)
        {
            if (ii == arr1.length) { return ii == arr2.length ? 0 : -1; }
            else if (ii == arr2.length) { return 1; }
            try
            {
                i1 = Integer.parseInt(arr1[ii]);
            }
            catch (Exception x)
            {
                i1 = Integer.MAX_VALUE;
                LogUtils.w(TAG, "Exception error!", x);
            }
            try
            {
                i2 = Integer.parseInt(arr2[ii]);
            }
            catch (Exception x)
            {
                i2 = Integer.MAX_VALUE;
                LogUtils.w(TAG, "Exception error!", x);
            }
            if (i1 != i2)
            {
                return i1 - i2;
            }
            i3 = arr1[ii].compareTo(arr2[ii]);
            if (i3 != 0) { return i3; }
        }
        return 0;
    }
}
