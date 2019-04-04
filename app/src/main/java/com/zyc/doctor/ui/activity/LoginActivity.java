package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.DocAuthStatu;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.http.bean.BaseNetConfig;
import com.zyc.doctor.http.bean.BaseResponse;
import com.zyc.doctor.http.bean.LoginSuccessBean;
import com.zyc.doctor.http.bean.Version;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.version.presenter.VersionPresenter;
import com.zyc.doctor.version.view.VersionUpdateDialog;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * 登录注册
 *
 * @author DUNDUN
 */
public class LoginActivity extends BaseActivity
        implements DocAuthStatu, VersionPresenter.VersionViewListener, VersionUpdateDialog.OnEnterClickListener,
                   CustomAdapt {
    @BindView(R.id.act_login_phone)
    EditText etPhone;
    @BindView(R.id.act_login_verifycode)
    EditText etVerifyCode;
    @BindView(R.id.act_login_verify)
    TextView tvGetVerify;
    @BindView(R.id.act_login_protocol_img)
    ImageView ivProtolImg;
    private String phone, verifyCode;
    private int time = 0;
    /**
     * 版本检测
     */
    private VersionPresenter mVersionPresenter;
    /**
     * 版本弹窗
     */
    private VersionUpdateDialog versionUpdateDialog;
    /**
     * 是否获取过验证码
     */
    private boolean isSendVerifyCode = false;
    private static final int MAX_RESEND_TIME = 60;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (time <= 0) {
                tvGetVerify.setClickable(true);
                tvGetVerify.setSelected(true);
                tvGetVerify.setText(R.string.txt_get_verify_code_again);
            }
            else {
                tvGetVerify.setClickable(false);
                tvGetVerify.setSelected(false);
                tvGetVerify.setText(time + "秒后重试");
            }
            return true;
        }
    });

    @Override
    public int getLayoutID() {
        return R.layout.act_login;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        findViewById(R.id.act_login_btn).setOnClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //检查更新
        mVersionPresenter = new VersionPresenter(this);
        mVersionPresenter.setVersionViewListener(this);
        mVersionPresenter.init();
        phone = sharePreferenceUtil.getString(CommonData.KEY_USER_PHONE);
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setText(phone);
            etPhone.setSelection(phone.length());
            tvGetVerify.setSelected(true);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.act_login_protocol).setOnClickListener(this);
        tvGetVerify.setOnClickListener(this);
        ivProtolImg.setOnClickListener(this);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                if (TextUtils.isEmpty(string)) {
                    return;
                }
                if (string.length() == 11) {
                    tvGetVerify.setSelected(true);
                }
                else {
                    tvGetVerify.setSelected(false);
                }
            }
        });
        etVerifyCode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginAndRegister();
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_login_verify:
                if (!tvGetVerify.isSelected()) {
                    return;
                }
                //获取验证码
                phone = etPhone.getText().toString().trim();
                if (!AllUtils.isMobileNumber(phone)) {
                    ToastUtil.toast(this, R.string.toast_txt_phone_error);
                    return;
                }
                //获取验证码
                getVerifyCode();
                break;
            case R.id.act_login_btn:
                loginAndRegister();
                break;
            case R.id.act_login_protocol:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", BaseNetConfig.BASE_BASIC_USER_PROTOCOL_URL);
                startActivity(intent);
                break;
            case R.id.act_login_protocol_img:
                if (ivProtolImg.isSelected()) {
                    ivProtolImg.setSelected(false);
                }
                else {
                    ivProtolImg.setSelected(true);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        RequestUtils.getVerifyCode(this, phone, this);
    }

    /**
     * 登录 注册
     */
    private void loginAndRegister() {
        phone = etPhone.getText().toString().trim();
        verifyCode = etVerifyCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.toast(this, R.string.txt_login_hint);
            return;
        }
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtil.toast(this, R.string.toast_txt_verify_hint);
            return;
        }
        if (!AllUtils.isMobileNumber(phone)) {
            ToastUtil.toast(this, R.string.toast_txt_phone_error);
            return;
        }
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtil.toast(this, R.string.toast_txt_verify_hint);
            return;
        }
        if (!ivProtolImg.isSelected()) {
            ToastUtil.toast(this, R.string.toast_txt_read_protol);
            return;
        }
        showProgressDialog("加载中...");
        RequestUtils.login(this, phone, verifyCode, "d", this);
    }

    @Override
    public void updateVersion(Version version, int mode, boolean isDownLoading) {
        if (mode == -1) {
            YihtApplication.getInstance().setVersionRemind(false);
            return;
        }
        YihtApplication.getInstance().setVersionRemind(true);
        versionUpdateDialog = new VersionUpdateDialog(this);
        versionUpdateDialog.setCancelable(false);
        versionUpdateDialog.setUpdateMode(mode).
                setIsDownNewAPK(isDownLoading).setContent(version.getUpdateDescription());
        versionUpdateDialog.setOnEnterClickListener(this);
        versionUpdateDialog.show();
    }

    @Override
    public void updateLoading(long total, long current) {
        if (versionUpdateDialog != null && versionUpdateDialog.isShowing()) {
            versionUpdateDialog.setProgressValue(total, current);
        }
    }

    /**
     * 当无可用网络时回调
     */
    @Override
    public void updateNetWorkError() {
        //        versionUpdateChecked = true;
    }

    @Override
    public void onEnter(boolean isMustUpdate) {
        mVersionPresenter.getNewAPK(isMustUpdate);
        ToastUtil.toast(this, "开始下载");
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_VERIFY_CODE:
                isSendVerifyCode = true;
                ToastUtil.toast(this, R.string.toast_txt_verifycode_success);
                time = MAX_RESEND_TIME;
                //org.apache.commons.lang3.concurrent.BasicThreadFactory
                final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                                                                                                 new BasicThreadFactory.Builder()
                                                                                                         .namingPattern(
                                                                                                                 "yht-thread-pool-%d")
                                                                                                         .daemon(true)
                                                                                                         .build());
                executorService.scheduleAtFixedRate(() -> {
                    time--;
                    if (time < 0) {
                        time = 0;
                        executorService.shutdownNow();
                    }
                    else {
                        handler.sendEmptyMessage(0);
                    }
                }, 0, 1, TimeUnit.SECONDS);
                break;
            case LOGIN_AND_REGISTER:
                //保存登录成功数据
                loginSuccessBean = (LoginSuccessBean)response.getData();
                YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                //保存登录账号
                sharePreferenceUtil.putString(CommonData.KEY_USER_PHONE, phone);
                EMClient.getInstance().login(loginSuccessBean.getDoctorId(), "111111", new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(() -> {
                            EMClient.getInstance().chatManager().loadAllConversations();
                            EMClient.getInstance().groupManager().loadAllGroups();
                            Log.d("main", "登录聊天服务器成功！");
                            jumpTopage();
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("main", "登录聊天服务器失败！");
                        ToastUtil.toast(LoginActivity.this, "登录聊天服务器失败");
                        //环信登陆失败 清除服务器登录信息
                        YihtApplication.getInstance().clearLoginSuccessBean();
                        closeProgressDialog();
                    }
                });
                break;
            default:
                break;
        }
    }

    private void jumpTopage() {
        closeProgressDialog();
        int checked = loginSuccessBean.getChecked();
        switch (checked) {
            case NONE:
            case VERIFYING:
            case VERIFY_FAILD:
                startActivity(new Intent(this, AuthDocStatuActivity.class));
                break;
            case VERIFY_SUCCESS:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        closeProgressDialog();
        super.onResponseCode(task, response);
        switch (task) {
            case GET_VERIFY_CODE:
                ToastUtil.toast(this, response.getMsg());
                break;
            case LOGIN_AND_REGISTER:
                Toast.makeText(this, response.getMsg(), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    /*************************屏幕适配*/
    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }
}
