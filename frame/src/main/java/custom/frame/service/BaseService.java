package custom.frame.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import custom.base.entity.base.BaseResponse;
import custom.base.utils.ToastUtil;
import custom.frame.R;
import custom.frame.http.IRequest;
import custom.frame.http.Tasks;
import custom.frame.http.listener.ResponseListener;
import custom.frame.log.MLog;

public class BaseService extends Service implements serviceInterface, ResponseListener<BaseResponse> {
    /**
     * 任务队列列表
     */
    List<Tasks> requestList;
    /**
     * 任务白名单列表
     */
    List<Tasks> whiteRequestList;
    /**
     * 网络请求单例
     */
    public IRequest mIRequest = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public final void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public void onDestroy() {
        //循环取消未完成的任务
        if (requestList != null)
            for (int i = 0; i < requestList.size(); i++) {
                Tasks task = requestList.get(i);
                //如果白名单里有此任务则跳过此任务
                if (whiteRequestList != null && whiteRequestList.contains(task)) {
                    MLog.d(getMTag(), "跳过白名单task任务: " + task);
                    continue;
                }
                MLog.d(getMTag(), "移除task任务: " + task);
                mIRequest.cancel(task, this);
            }

        //清除所有缓存列表
        requestList.clear();
        whiteRequestList.clear();
        requestList = null;
        whiteRequestList = null;
        super.onDestroy();
    }

    private void init() {
        requestList = new ArrayList<>();
        whiteRequestList = new ArrayList<>();
        mIRequest = IRequest.getInstance(this);

        initClass();
        initData();
        initListener();
    }

    //=================创建后回调
    @Override
    public void initClass() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    /**
     * 添加白色任务，（在activity摧毁的时候此任务不会被移除请求）
     */
    protected void addWhiteTask(Tasks tasks) {
        whiteRequestList.add(tasks);
    }

    /**
     * 得到本类名
     */
    protected String getMTag() {
        return getResources().getString(R.string.app_debug_flag) + "#" + getClass().getSimpleName();
    }


    //============================================网络回调
    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {

    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        ToastUtil.releaseShow(this, e.getMessage());
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response) {

    }

    @Override
    public void onResponseStart(Tasks task) {
        //添加任务队列
        if (task != null && requestList != null)
            requestList.add(task);
    }

    @Override
    public void onResponseEnd(Tasks task) {
        //移除任务队列
        if (task != null && requestList != null)
            requestList.remove(task);
    }

    @Override
    public void onResponseLoading(Tasks task, boolean isUpload, long total, long current) {

    }

    @Override
    public void onResponseFile(Tasks task, File file) {

    }

    @Override
    public void onResponseCancel(Tasks task) {
        //移除任务队列
        if (task != null && requestList != null)
        {
            requestList.remove(task);
        }
    }

}
