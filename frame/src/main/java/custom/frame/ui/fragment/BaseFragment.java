package custom.frame.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import custom.base.entity.base.BaseResponse;
import custom.frame.R;
import custom.frame.http.IRequest;
import custom.frame.http.Tasks;
import custom.frame.http.listener.ResponseListener;
import custom.frame.log.MLog;
import custom.frame.ui.ConstantsCommon;
import custom.frame.ui.activity.AppManager;

/**
 * Created by luozi on 2016/3/7.
 */
public abstract class BaseFragment extends Fragment
        implements FragmentInterface, ResponseListener<BaseResponse>, View.OnClickListener,
                   ConstantsCommon
{
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

    @Override
    public final void onCreate(Bundle savedInstanceState)
    {
        befordCreateView(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view;
        int layoutID = getLayoutID();
        if (layoutID != 0)
        {
            view = inflater.inflate(getLayoutID(), null);
        }
        else
        {
            view = getLayoutView();
        }
        init();
        init(view, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 基础初始化
     */
    private void init()
    {
        requestList = new ArrayList<>();
        whiteRequestList = new ArrayList<>();
        mIRequest = IRequest.getInstance(getContext());
    }

    /**
     * 自定义的界面恢复回调
     */
    public void onFragmentResume()
    {
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
    private void init(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        initView(view, savedInstanceState);
        initObject(savedInstanceState);
        initData(savedInstanceState);
        initListener();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        AppManager.getInstance().removeActivity(getActivity());
        //循环取消未完成的任务
        if (requestList != null)
        {
            for (int i = 0; i < requestList.size(); i++)
            {
                Tasks task = requestList.get(i);
                //如果白名单里有此任务则跳过此任务
                if (whiteRequestList != null && whiteRequestList.contains(task))
                {
                    MLog.d(getTag(), "跳过白名单task任务: " + task);
                    continue;
                }
                MLog.d(getTag(), "移除task任务: " + task);
                mIRequest.cancel(task, this);
            }
        }
        //清除所有缓存列表
        requestList.clear();
        whiteRequestList.clear();
        requestList = null;
        whiteRequestList = null;
    }

    @Override
    public void onClick(View v)
    {
        onClick(v, v.getId());
    }

    /**
     * 单击回调
     *
     * @param v       点击的view
     * @param clickID 点击的控件id
     */
    public void onClick(View v, int clickID)
    {
    }

    /**
     * 添加白色任务，（在activity摧毁的时候此任务不会被移除请求）
     */
    protected void addWhiteTask(Tasks tasks)
    {
        whiteRequestList.add(tasks);
    }

    /**
     * 默认不适用此方法，在子类里可以重构他
     */
    @Override
    public View getLayoutView()
    {
        return null;
    }
    //=====================setContentView 前回调

    @Override
    public void befordCreateView(@NonNull Bundle savedInstanceState)
    {
    }

    //=================创建后回调
    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
    }

    @Override
    public void initObject(@NonNull Bundle savedInstanceState)
    {
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
    }

    @Override
    public void initListener()
    {
    }

    /**
     * 得到本类名
     */
    protected String getMTag()
    {
        return getResources().getString(R.string.app_debug_flag) + "#" + getClass().getSimpleName();
    }

    //============================================网络回调
    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
    }

    @Override
    public void onResponseError(Tasks task, Exception e)
    {
        //        ToastUtil.releaseShow(getContext(), e.getMessage());
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
    }

    @Override
    public void onResponseStart(Tasks task)
    {
        //添加任务队列
        if (task != null && requestList != null)
        {
            requestList.add(task);
        }
    }

    @Override
    public void onResponseEnd(Tasks task)
    {
        //移除任务队列
        if (task != null && requestList != null)
        {
            requestList.remove(task);
        }
    }

    @Override
    public void onResponseLoading(Tasks task, boolean isUpload, long total, long current)
    {
    }

    @Override
    public void onResponseFile(Tasks task, File file)
    {
    }

    @Override
    public void onResponseCancel(Tasks task)
    {
        //移除任务队列
        if (task != null && requestList != null)
        {
            requestList.remove(task);
        }
    }
}
