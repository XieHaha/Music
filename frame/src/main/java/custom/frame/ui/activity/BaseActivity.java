package custom.frame.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import custom.frame.R;
import custom.frame.bean.BaseResponse;
import custom.frame.bean.LoginSuccessBean;
import custom.frame.http.IRequest;
import custom.frame.http.Tasks;
import custom.frame.http.listener.ResponseListener;
import custom.frame.log.MLog;
import custom.frame.permission.OnPermissionCallback;
import custom.frame.permission.Permission;
import custom.frame.permission.PermissionHelper;
import custom.frame.ui.ConstantsCommon;
import custom.frame.utils.SharePreferenceUtil;
import custom.frame.utils.ToastUtil;

/**
 * activity基类
 *
 * @author DUNDUN
 */
public abstract class BaseActivity<T> extends Activity
        implements ActivityInterface, ResponseListener<BaseResponse>, View.OnClickListener,
        OnPermissionCallback, ConstantsCommon {
    private ProgressDialog mProgressDialog;

    /**
     * 登录数据
     */
    protected LoginSuccessBean loginSuccessBean;
    protected SharePreferenceUtil sharePreferenceUtil;
    /**
     * 权限管理类
     */
    protected PermissionHelper permissionHelper;
    private boolean isRequest = true;
    private boolean isRequestPhone = true;
    private boolean isRequestCamera = true;
    private boolean isRequestRecord = true;
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
    protected IRequest mIRequest = null;
    /**
     * 返回按钮对象
     */
    protected ImageView backBtn;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        befordCreateView(savedInstanceState);
        int layoutID = getLayoutID();
        if (layoutID != 0) {
            setContentView(layoutID);
        } else {
            setContentView(getLayoutView());
        }
        ButterKnife.bind(this);
        /**
         * 权限管理类
         */
        permissionHelper = PermissionHelper.getInstance(this);
        permissionHelper.request(
                new String[]{Permission.READ_PHONE_STATE, Permission.STORAGE_WRITE});
        init(savedInstanceState);
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
    private void init(@NonNull Bundle savedInstanceState) {
        initBackViews();
        if (isInitBackBtn()) {
            initBackBtn();
        }
        // 数据初始化
        requestList = new ArrayList<>();
        whiteRequestList = new ArrayList<>();
        mIRequest = IRequest.getInstance(this);
        loginSuccessBean = getLoginSuccessBean();
        sharePreferenceUtil = new SharePreferenceUtil(this);
        initView(savedInstanceState);
        initObject(savedInstanceState);
        initData(savedInstanceState);
        initListener();
    }

    private void initBackViews() {
        try {
            backBtn = findViewById(R.id.public_title_bar_back);
        } catch (Exception e) {
            MLog.e(getMTag(), e.getMessage());
        }
    }

    /**
     * 初始化back按钮事件，及title名称赋值
     */
    private void initBackBtn() {
        try {
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            MLog.e(getMTag(), e.getMessage());
        }
    }

    /**
     * 是否初始化返回按钮
     *
     * @return 如果不想baseactivity自动设置监听返回按钮的话就传回null，
     * 系统则不会自动设置监听,但是会初始化控件
     */
    protected boolean isInitBackBtn() {
        return false;
    }

    /**
     * 初始化login数据
     *
     * @return
     */
    public LoginSuccessBean getLoginSuccessBean() {
        String userStr = (String) SharePreferenceUtil.getObject(this, "key_login_success_bean", "");
        if (!TextUtils.isEmpty(userStr)) {
            loginSuccessBean = JSON.parseObject(userStr, LoginSuccessBean.class);
        }
        return loginSuccessBean;
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
     * 修改状态栏为全透明
     *
     * @param activity
     */
    @TargetApi(19)
    public void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 得到返回按钮控件
     */
    public ImageView getBackBtnView() {
        return backBtn;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
        //循环取消未完成的任务
        if (requestList != null) {
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
    public void befordCreateView(@NonNull Bundle savedInstanceState) {
    }

    //=================创建后回调
    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
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

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.public_progress));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
    }

    /**
     * 显示进度条
     *
     * @param msg 提示消息
     */
    public void showProgressDialog(final String msg) {
        showProgressDialog(msg, true);
    }

    /**
     * 显示进度条
     *
     * @param resid 提示消息
     */
    public void showProgressDialog(final int resid) {
        String msg = getResources().getString(resid);
        showProgressDialog(msg, true);
    }

    /**
     * 显示进度条
     *
     * @param msg    提示消息
     * @param cancel 是否可取消
     */
    public void showProgressDialog(final String msg, final boolean cancel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null) {
                    initProgressDialog();
                }
                mProgressDialog.setCancelable(cancel);
                mProgressDialog.setCanceledOnTouchOutside(cancel);
                mProgressDialog.setMessage(msg);
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }
        });
    }

    /**
     * 关闭进度条
     */
    public void closeProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null) {
                    return;
                }
                if (!mProgressDialog.isShowing()) {
                    return;
                }
                mProgressDialog.setCancelable(true);
                mProgressDialog.setCanceledOnTouchOutside(true);
                mProgressDialog.dismiss();
            }
        });
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
    public void onResponseCodeError(Tasks task, BaseResponse response) {
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        closeProgressDialog();
        ToastUtil.toast(this, e.getMessage());
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
    public void onResponseLoading(Tasks task, boolean isUpload, long total, long current) {
    }

    @Override
    public void onResponseFile(Tasks task, File file) {
    }

    @Override
    public void onResponseCancel(Tasks task) {
        //移除任务队列
        if (task != null && requestList != null) {
            requestList.remove(task);
        }
    }


    /**
     * 把json转换成基础响应对象列表类
     */
    public final BaseResponse praseBaseResponseList(JSONObject jsonObject,
                                                    Class<T> classOfT) throws JSONException {
        BaseResponse baseResponse = new BaseResponse().setCode(jsonObject.optInt(EntityCode))
                .setMsg(jsonObject.optString(EntityMsg));
        List<T> list = new ArrayList<>();
        if (jsonObject.opt(EntityData) != null) {
            JSONArray jsonArray = jsonObject.optJSONArray(EntityData);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        list.add(JSON.parseObject(jsonArray.get(i).toString(), classOfT));
                    } catch (Exception e) {
                    }
                }
            }
        }
        baseResponse.setData(list);
        return baseResponse;
    }

    /**
     * 把json转换成基础响应对象类
     *
     * @param jsonObject jsonobject对象
     * @param classOfT   待转换的实体类,为空则data为空
     */
    public final BaseResponse praseBaseResponse(JSONObject jsonObject, Class<T> classOfT)
            throws JSONException {
        Object data = null;
        if (classOfT != null) {
            if (classOfT == String.class) {
                data = jsonObject.optString(EntityData);
            } else {
                if (jsonObject.opt(EntityData) != null) {
                    try {
                        data = JSON.parseObject(jsonObject.optString(EntityData), classOfT);
                    } catch (Exception e) {
                    }
                }
            }
        }
        BaseResponse baseResponse = new BaseResponse().setCode(jsonObject.optInt(EntityCode))
                .setMsg(jsonObject.optString(EntityMsg))
                .setData(data);
        return baseResponse;
    }

    //=============================================请求辅助方法==============================
    protected static final String EntityData = "data";
    protected static final String EntityCode = "code";
    protected static final String EntityMsg = "msg";


    //    --------------------------------权限申请--------------------------//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (permissions == null) {
            return;
        }
        for (String per : permissions) {
            if (Permission.STORAGE_WRITE.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
            if (Permission.READ_PHONE_STATE.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
            if (Permission.CAMERA.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
            if (Permission.RECORD_AUDIO.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
        }
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        isRequest = true;
        isRequestPhone = true;
        isRequestCamera = true;
        isRequestRecord = true;
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        if (permissionName == null) {
            return;
        }
        for (String permission : permissionName) {
            if (Permission.STORAGE_WRITE.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_storage_permission_tip);
                break;
            }
            if (Permission.READ_PHONE_STATE.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_read_phone_state_tip);
                break;
            }
            if (Permission.CAMERA.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_camera_permission_tip);
                break;
            }
            if (Permission.RECORD_AUDIO.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_audio_permission_tip);
                break;
            }
        }
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        isRequest = true;
        isRequestPhone = true;
        isRequestCamera = true;
        isRequestRecord = true;
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (isRequest) {
            isRequest = false;
            permissionHelper.requestAfterExplanation(Permission.STORAGE_WRITE);
        }
        if (isRequestPhone) {
            isRequestPhone = false;
            permissionHelper.requestAfterExplanation(Permission.READ_PHONE_STATE);
        }
        if (isRequestCamera) {
            isRequestCamera = false;
            permissionHelper.requestAfterExplanation(Permission.CAMERA);
        }
        if (isRequestRecord) {
            isRequestRecord = false;
            permissionHelper.requestAfterExplanation(Permission.RECORD_AUDIO);
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        isRequest = true;
        isRequestPhone = true;
        isRequestCamera = true;
        isRequestRecord = true;
    }


}
