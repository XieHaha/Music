package com.zyc.doctor.ui.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zyc.doctor.R;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.LoginSuccessBean;
import com.zyc.doctor.http.listener.ResponseListener;
import com.zyc.doctor.ui.base.activity.AppManager;
import com.zyc.doctor.utils.SharePreferenceUtil;
import com.zyc.doctor.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author dundun
 */
public abstract class BaseFragment<T> extends Fragment
        implements FragmentInterface, ResponseListener<BaseResponse>, View.OnClickListener {
    /**
     * 任务队列列表
     */
    List<Tasks> requestList;
    /**
     * 任务白名单列表
     */
    List<Tasks> whiteRequestList;
    /**
     * 登录数据
     */
    protected LoginSuccessBean loginSuccessBean;
    /**
     * 数据存储
     */
    protected SharePreferenceUtil sharePreferenceUtil;
    /**
     * 注解
     */
    protected Unbinder unbinder;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        beforeCreateView(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        int layoutID = getLayoutID();
        if (layoutID != 0) {
            view = inflater.inflate(getLayoutID(), null);
        }
        else {
            view = getLayoutView();
        }
        unbinder = ButterKnife.bind(this, view);
        init();
        init(view, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 基础初始化
     */
    private void init() {
        requestList = new ArrayList<>();
        whiteRequestList = new ArrayList<>();
        loginSuccessBean = getLoginSuccessBean();
        sharePreferenceUtil = new SharePreferenceUtil(getActivity());
    }

    /**
     * 获取状态栏高度,在页面还没有显示出来之前
     *
     * @param a
     * @return
     */
    public static int getStateBarHeight(Activity a) {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 方法回调顺序
     * 1.initView
     * 2.initClss
     * 3.initData
     * 4.initListener
     *
     * @param savedInstanceState
     */
    private void init(@NonNull View view, @NonNull Bundle savedInstanceState) {
        initView(view, savedInstanceState);
        initObject(savedInstanceState);
        initData(savedInstanceState);
        initListener();
        view.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (isAdded())
                {
                    fillData();
                }
            }
        });
    }

    /**
     * 初始化login数据
     *
     * @return
     */
    public LoginSuccessBean getLoginSuccessBean() {
        String userStr = (String)SharePreferenceUtil.getObject(getActivity(), "key_login_success_bean", "");
        if (!TextUtils.isEmpty(userStr)) {
            loginSuccessBean = new Gson().fromJson(userStr, LoginSuccessBean.class);
        }
        return loginSuccessBean;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(getActivity());
        //循环取消未完成的任务
        if (requestList != null) {
            for (int i = 0; i < requestList.size(); i++) {
                Tasks task = requestList.get(i);
                //如果白名单里有此任务则跳过此任务
                if (whiteRequestList != null && whiteRequestList.contains(task)) {
                    Log.d(getTag(), "跳过白名单task任务: " + task);
                    continue;
                }
                Log.d(getTag(), "移除task任务: " + task);
            }
            //清除所有缓存列表
            requestList.clear();
            if (whiteRequestList != null) {
                whiteRequestList.clear();
            }
        }
        requestList = null;
        whiteRequestList = null;
    }

    @Override
    public void onClick(View v) {
        onClick(v, v.getId());
    }

    /**
     * 单击回调
     *
     * @param v       点击的view
     * @param clickID 点击的控件id
     */
    public void onClick(View v, int clickID) {
    }

    /**
     * 添加白色任务，（在activity摧毁的时候此任务不会被移除请求）
     */
    protected void addWhiteTask(Tasks tasks) {
        whiteRequestList.add(tasks);
    }

    /**
     * 默认不适用此方法，在子类里可以重构他
     */
    @Override
    public View getLayoutView() {
        return null;
    }
    //=====================setContentView 前回调

    @Override
    public void beforeCreateView(@NonNull Bundle savedInstanceState) {
    }

    //=================创建后回调
    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initObject(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initListener() {
    }

    public void fillData()
    {

    }

    /**
     * 得到本类名
     */
    protected String getMTag() {
        return getResources().getString(R.string.app_debug_flag) + "#" + getClass().getSimpleName();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        ToastUtil.toast(getActivity(), e.getMessage());
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        ToastUtil.toast(getContext(), response.getMsg());
    }

    @Override
    public void onResponseStart(Tasks task) {
        //添加任务队列
        if (task != null && requestList != null) {
            requestList.add(task);
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        //移除任务队列
        if (task != null && requestList != null) {
            requestList.remove(task);
        }
    }

    @Override
    public void onResponseCancel(Tasks task) {
        //移除任务队列
        if (task != null && requestList != null) {
            requestList.remove(task);
        }
    }
}
