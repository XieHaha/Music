package custom.frame.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import custom.frame.ui.ConstantsCommon;

public abstract class BaseActivity extends AppCompatActivity
        implements ActivityInterface, ResponseListener<BaseResponse>, View.OnClickListener,
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
    /**
     * 返回按钮对象
     */
    private LinearLayout backBtn;
    /**
     * 标题栏文本控件
     */
    private TextView tvTitle;
    /**
     * 标题名称
     */
    @NonNull
    private String titleName;

    @Override
    protected final void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        //状态栏透明
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        AppManager.getInstance().addActivity(this);
        befordCreateView(savedInstanceState);
        int layoutID = getLayoutID();
        if (layoutID != 0)
        {
            setContentView(layoutID);
        }
        else
        {
            setContentView(getLayoutView());
        }
        init();
        init(savedInstanceState);
    }

    /**
     * 基础初始化
     */
    private void init()
    {
        requestList = new ArrayList<>();
        whiteRequestList = new ArrayList<>();
        mIRequest = IRequest.getInstance(this);
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
    private void init(@NonNull Bundle savedInstanceState)
    {
        titleName = isInitBackBtn();
        initBackViews();
        if (isInitBackBtn() != null)
        {
            initBackBtn();
        }
        initView(savedInstanceState);
        initObject(savedInstanceState);
        initData(savedInstanceState);
        initListener();
    }

    private void initBackViews()
    {
        try
        {
            backBtn = (LinearLayout)findViewById(R.id.top_bar_back);
            tvTitle = (TextView)findViewById(R.id.top_bar_title_name);
        }
        catch (Exception e)
        {
            MLog.e(getMTag(), e.getMessage());
        }
    }

    /**
     * 初始化back按钮事件，及title名称赋值
     */
    private void initBackBtn()
    {
        try
        {
            backBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
            tvTitle.setText(titleName);
        }
        catch (Exception e)
        {
            MLog.e(getMTag(), e.getMessage());
        }
    }

    /**
     * 是否初始化返回按钮
     *
     * @return 如果不想baseactivity自动设置监听返回按钮的话就传回null，
     * 系统则不会自动设置监听,但是会初始化控件
     */
    protected String isInitBackBtn()
    {
        return null;
    }

    /**
     * 得到返回按钮控件
     */
    public LinearLayout getBackBtnView()
    {
        return backBtn;
    }

    /**
     * 得到标题文本控件
     */
    public TextView getTitleView()
    {
        return tvTitle;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
        //循环取消未完成的任务
        if (requestList != null)
        {
            for (int i = 0; i < requestList.size(); i++)
            {
                Tasks task = requestList.get(i);
                //如果白名单里有此任务则跳过此任务
                if (whiteRequestList != null && whiteRequestList.contains(task))
                {
                    MLog.d(getMTag(), "跳过白名单task任务: " + task);
                    continue;
                }
                MLog.d(getMTag(), "移除task任务: " + task);
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
    public void initView(@NonNull Bundle savedInstanceState)
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
     * 显示toast
     *
     * @param resId
     */
    protected void showToast(int resId)
    {
        ToastUtil.releaseShow(this, resId);
    }

    protected void showToast(String text)
    {
        ToastUtil.releaseShow(this, text);
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
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
    }

    @Override
    public void onResponseError(Tasks task, Exception e)
    {
        ToastUtil.debugShow(this, "baseActivity：" + e.getMessage());
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
        if (task != null && requestList != null) { requestList.remove(task); }
    }
}
